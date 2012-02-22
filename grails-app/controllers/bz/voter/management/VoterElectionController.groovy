package bz.voter.management

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import bz.voter.management.utils.VoterListTypeEnum
import bz.voter.management.utils.PickupTimeEnum

class VoterElectionController {

    def exportService
    def voterElectionService


    def excelFields = ["registrationNumber", "lastName", "firstName", 
                        "pollNumber", "affiliation", "pledge", "voted",
                        "pickupTime", "houseNumber", "street", "municipality",
                        "registrationAddress", "phone1", "phone2", "phone3"]

    def excelLabels = [
            "registrationNumber": "Registration Number",
            "lastName": "Last Name",
            "firstName": "First Name",
            "pollNumber": "Poll Number",
            "affiliation": "Affiliation",
            "pledge": "Pledge",
            "voted": "Voted",
            "pickupTime": "Pickup Time",
            "houseNumber": "House #",
            "street": "Street",
            "municipality": "Municipality",
            "registrationAddress": "Registration Address",
            "phone1": "Phone 1",
            "phone2": "Phone 2",
            "phone3": "Phone 3"
    ]

    def excelParams = ["column.widths": [0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6]]

    def pdfFields = ["registrationNumber", "lastName", "firstName", "pollNumber", "affiliation", "pledge",
                        "voted", "pickupTime", "registrationAddress"]


    def pdfLabels = [
            "registrationNumber": "Registration Number",
            "lastName": "Last Name",
            "firstName": "First Name",
            "pollNumber": "Poll #",
            "affiliation": "Affiliation",
            "pledge": "Pledge",
            "voted": "Voted",
            "pickupTime": "Pickup Time",
            "registrationAddress": "Registration Address"
    ]


    def pdfParams = ["column.widths": [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,0.1, 0.2]]

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


            def registrationNumberFormatter = {domain, value->
                domain.voter.registrationNumber
            }

            def registrationAddressFormatter = {domain,value->
                "${domain.voter.registrationAddress}"
            }

            def houseNumberFormatter = {domain,value->
                "${domain.voter.registrationAddress.houseNumber}"
            }

            def streetFormatter = {domain, value->
                "${domain.voter.registrationAddress.street}"
            }


            def municipalityFormatter = {domain, value->
                "${domain.voter.registrationAddress.municipality}"
            }


            def lastNameFormatter = {domain, value->
                "${domain.voter.lastName}"
            }

            def firstNameFormatter = {domain, value->
                "${domain.voter.firstName}"
            }

            
            def pollNumberFormatter = {domain, value->
                "${domain.voter.pollStation.pollNumber}"
            }


            def affiliationFormatter = {domain,value->
                "${domain.voter.affiliation}"
            }

            def phone1Formatter = {domain,value->
                "${domain.voter.registrationAddress.phoneNumber1}"
            }


            def phone2Formatter = {domain, value->
                "${domain.voter.registrationAddress.phoneNumber2}"
            }


            def phone3Formatter = {domain,value->
                "${domain.voter.registrationAddress.phoneNumber3}"
            }


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
                        registrationNumber: registrationNumberFormatter,
                        registrationAddress: registrationAddressFormatter,
                        firstName:      firstNameFormatter,
                        lastName:       lastNameFormatter,
                        pollNumber:     pollNumberFormatter,
                        affiliation:    affiliationFormatter,
                        voted:          votedFormatter
                    ]
                    break

                case "excel":
                    fields = excelFields
                    labels = excelLabels
                    formatters = [
                        registrationNumber: registrationNumberFormatter,
                        registrationAddress: registrationAddressFormatter,
                        firstName:      firstNameFormatter,
                        lastName:       lastNameFormatter,
                        pollNumber:     pollNumberFormatter,
                        affiliation:    affiliationFormatter,
                        voted:          votedFormatter,
                        houseNumber:    houseNumberFormatter,
                        street:         streetFormatter,
                        municipality:   municipalityFormatter,
                        phone1:         phone1Formatter,
                        phone2:         phone2Formatter,
                        phone3:         phone3Formatter
                    ]
                    break

            }

            switch(params.listType){
                case "PICKUP_TIME":
                    voters = voterElectionService.filterByPickupTimeAndVoted(electionInstance, divisionInstance,(PickupTimeEnum)params.pickupTime,stringToBool(params.voted),0,0)
                    parameters.title = "Voters Pickup Times"
                    break

                case "ALL":
                    voters = voterElectionService.listByElectionAndDivision(electionInstance,divisionInstance,0,0)
                    parameters.title = "${electionInstance.year} Election"
                    break

                case "PLEDGE":
                    voters = voterElectionService.filterByPledgeAndVoted(electionInstance,divisionInstance,
                        Pledge.get(params.pledge),stringToBool(params.voted),0,0)
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
