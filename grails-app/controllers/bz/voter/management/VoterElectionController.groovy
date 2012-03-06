package bz.voter.management

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import bz.voter.management.utils.VoterListTypeEnum
import bz.voter.management.utils.PickupTimeEnum

class VoterElectionController {

    def exportService
    def voterElectionService


    def excelFields = ["registration_number", "last_name", "first_name", 
                        "poll_number", "affiliation", "pledge", "voted",
                        "pickup_time", "house_number", "street", "municipality",
                        "phone1", "phone2", "phone3"]

    def excelLabels = [
            "registration_number": "Registration Number",
            "last_name": "Last Name",
            "first_name": "First Name",
            "poll_number": "Poll Number",
            "affiliation": "Affiliation",
            "pledge": "Pledge",
            "voted": "Voted",
            "pickup_time": "Pickup Time",
            "house_number": "House #",
            "street": "Street",
            "municipality": "Municipality",            
            "phone1": "Phone 1",
            "phone2": "Phone 2",
            "phone3": "Phone 3"
    ]

    def excelParams = ["column.widths": [0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6]]

    def pdfFields = ["registration_number", "last_name", "first_name", "poll_number", "affiliation", "pledge",
                        "voted", "pickup_time", "house_number", "street", "municipality"]


    def pdfLabels = [
            "registration_number": "Registration Number",
            "last_name": "Last Name",
            "first_name": "First Name",
            "poll_number": "Poll #",
            "affiliation": "Affiliation",
            "pledge": "Pledge",
            "voted": "Voted",
            "pickup_time": "Pickup Time",
            "house_number": "House #",
            "street": "Street",
            "municipality": "Municipality",  
    ]


    def pdfParams = ["column.widths": [0.5, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,0.1, 0.5,0.1,0.1]]

    def parameters = [:]
    def formatters = [:]

    def list() {
        def voters
        List fields
        Map labels

        def electionInstance = Election.get(params.election.toLong())
        def divisionInstance = Division.get(params.division.toLong())

	 	if(params?.format && params.format != 'html'){
			response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
			response.setHeader("Content-disposition", "attachment; filename=voters.${params.extension}")

            def votedFormatter = {domain, value->
                def voted
                switch(value){
                    case true:
                        voted = "Yes"
                        break

                    case false:
                        voted = "No"
                        break

                }

                "${voted}"
            }

            switch(params.format){
                
                case "pdf":
                    fields = pdfFields
                    labels = pdfLabels
                    formatters = [                       
                        voted:          votedFormatter
                    ]                    
                    break

                case "excel":
                    fields = excelFields
                    labels = excelLabels  
                    formatters = [                       
                        voted:  votedFormatter                       
                    ]                  
                    break

            }

            switch(params.listType){
                case "PICKUP_TIME":
                    voters = voterElectionService.printByPickupTime(electionInstance,divisionInstance,
                        (PickupTimeEnum)params.pickupTime,stringToBool(params.voted))
                    parameters.title = "Voters Pickup Times"                    
                    break

                case "ALL":
                    voters = voterElectionService.listByElectionAndDivision(electionInstance,divisionInstance,0,0)
                    parameters.title = "${electionInstance.year} Election"
                    break

                case "PLEDGE":                   
                    voters = voterElectionService.printByPledge(electionInstance,divisionInstance,
                        Pledge.get(params.pledge),stringToBool(params.voted))
                    parameters.title = "Pledges for ${electionInstance.year} Election"
                    break
            }

            exportService.export(params.format, response.outputStream,voters,
                fields, labels,formatters, parameters)
        }
    }




    def pickupTime() {
        def extension = (params.format == "pdf") ?: "xls"
        redirect(action: "list", 
            params: [
                "extension":extension, 
                "format": params.format, 
                "division": params.division, 
                "election": params.election,
                "listType": params.listType,
                "voted": params.voted,
                "pickupTime": params.pickupTime])
    }

    def allVoters() {
        def extension = (params.format == "pdf") ?: "xls"
        redirect(action: "list", 
            params: [
                "extension":extension, 
                "format":params.format, 
                "division": params.division, 
                "election": params.election,
                "listType": params.listType
            ])
    }


    def pledges() {
        def extension = (params.format == "pdf") ?: "xls"
        redirect(action: "list", 
            params: [
                "extension":extension, 
                "format":params.format, 
                "division": params.division, 
                "election": params.election,
                "pledge" : params.pledge,
                "voted": params.voted,
                "listType": params.listType
            ])
    }


    public boolean stringToBool(String s){
        if(s.equals('true')){
            return true
        }
        if(s.equals('false')){
            return false
        }

        throw new IllegalArgumentException(s + " is not a bool. Only true are false are.")
    }
}
