(ns user
  "Tools for interactive development with the REPL. This file should
   not be included in a production build of the application."
  (:require
    [clojure.java.io :as io]
    [clojure.java.javadoc :refer (javadoc)]
    [clojure.java.jdbc :as jdbc]
    [clojure.pprint :refer (pprint)]
    [clojure.reflect :refer (reflect)]
    [clojure.repl :refer (apropos dir doc find-doc pst source)]
    [clojure.set :as set]
    [clojure.string :as str]
    [clojure.test :as test]
    [clojure.tools.namespace.repl :refer (refresh refresh-all)]
    [clojure.tools.reader.edn :as edn]
    [clojure.core.async :as async :refer [pub sub chan close! timeout <! >! <!! >!! alts! alts!! alt! alt!!]]
    [clojure.set :refer :all]
    [com.stuartsierra.component :as c]
    [ring.server.standalone :refer :all]
    [ring.middleware.file-info :refer :all]
    [ring.middleware.file :refer :all]
    [clj-http.client :as client]
    [edu.ucdenver.ccp.kr.kb :refer :all]
    [edu.ucdenver.ccp.kr.rdf :refer :all]
    [edu.ucdenver.ccp.kr.sparql :refer :all]
    [edu.ucdenver.ccp.kr.sesame.kb :as sesame]
    [taoensso.timbre :as timbre]
    [server.components.db :refer :all]
    [server.components.mom :refer :all]
    [server.components.lov :refer :all]
    [server.components.recommender :refer :all]
    [server.components.ring :refer :all]
    [server.core.db :refer :all]
    [server.core.lov :as lov]
    [server.core.recommender :refer :all]
    [server.routes.app :refer [app-fn]]
    [server.system :refer :all]
    )
  )
(timbre/refer-timbre)

(def system
  "A Var containing an object representing the application under
  development."
  nil)

(declare log-config)

(defn init
  "Creates and initializes the system under development in the Var
  #'system."
  []
  (let [db-opts {:subprotocol "postgresql" 
                 :subname "mydb" 
                 :username "postgres" 
                 :password ""}
        ring-opts {:port 3000
                   :open-browser? false
                   :join true
                   :auto-reload? true}
        recommender-sparql "http://dbpedia.org/sparql"
        log-config log-config]
    (alter-var-root #'system (constantly (new-system db-opts #'app-fn ring-opts recommender-sparql log-config)))
    )
  )

(defn start
  "Starts the system running, updates the Var #'system."
  []
  (alter-var-root #'system c/start)
  (let [mock-recommender (edn/read-string (slurp (io/resource "recommender.samples")))
        recommender (get-in system [:lov :recommender])]
    (reset! recommender mock-recommender)
    )
  )

(defn stop
  "Stops the system if it is currently running, updates the Var
  #'system."
  []
  (alter-var-root #'system
    (fn [s] (when s (c/stop s))))
  )

(defn go
  "Initializes and starts the system running."
  []
  (init)
  (start)
  :ready)

(defn reset
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop)
  (refresh :after 'user/go))

(def log-config {
  :ns-whitelist []
  :ns-blacklist []
  })
