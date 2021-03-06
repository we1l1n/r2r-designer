# The RDF service

Holds shared data for both the CSV and database transformation.

    'use strict'

    angular.module 'r2rDesignerApp'
      .factory 'Rdf', ->

        propertyLiteralTypeOptions: ['Plain Literal', 'Typed Literal', 'Blank Node']
        propertyLiteralTypes: ['xsd:anyURI', 'xsd:base64Binary', 'xsd:boolean', 'xsd:date', 'xsd:dateTime', 'xsd:decimal', 'xsd:double', 'xsd:duration', 'xsd:float', 'xsd:hexBinary', 'xsd:gDay', 'xsd:gMonth', 'xsd:gMonthDay', 'xsd:gYear', 'xsd:gYearMonth', 'xsd:NOTATION', 'xsd:QName', 'xsd:string', 'xsd:time']

        baseUri: ''
        subjectTemplate: ''
        propertyLiteralSelection: {}
        propertyLiteralTypeSelection: {}
        selectedClasses: {}
        selectedProperties: {}
        suggestions: {}
