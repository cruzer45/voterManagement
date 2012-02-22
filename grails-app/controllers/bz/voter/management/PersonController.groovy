package bz.voter.management

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import bz.voter.management.utils.VoterListTypeEnum
import bz.voter.management.utils.FilterType

class PersonController {

	def exportService
	def voterService

    def excelFields = ["registrationDate", "registrationNumber", "lastName", "firstName",
                        "age", "birthDate", "registrationAddress.houseNumber",
                        "registrationAddress.street", "registrationAddress.municipality",
                        "registrationAddress"]
    
    def pdfFields = ["registrationDate", "registrationNumber", "lastName", "firstName",
                        "age", "birthDate", "registrationAddress"]

    def excelLabels = ["registrationDate": "Registration Date", "registrationNumber": "Registration Number",
                        "firstName": "First Name", "lastName": "Last Name", "birthDate": "DOB",
                        "age": "Age", "registrationAddress.houseNumber": "House #",
                        "registrationAddress.street": "Street", "registrationAddress.municipality": "municipality",
                        "registrationAddress": "Registration Address"]

    def pdfLabels = ["registrationDate": "Registration Date", "registrationNumber": "Registration Number",
                    "firstName": "First Name", "lastName": "Last Name", "birthDate": "DOB",
                    "age": "Age", "registrationAddress": "Registration Address"]


    def pdfParams = ["column.widths": [0.1, 0.1, 0.2, 0.2, 0.1, 0.1, 0.2]]
    def excelParams = ["column.widths": [0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1]]


	 def list() {
	 	if(params?.format && params.format != 'html'){
            def voters
			response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
			response.setHeader("Content-disposition", "attachment; filename=person.${params.extension}")

			List fields  
			Map labels 
			Map parameters 

            switch(params.format){
                case "pdf":
                    fields = pdfFields
                    labels = pdfLabels
                    parameters = pdfParams
                    break

                case "excel":
                    fields = excelFields
                    labels = excelLabels
                    parameters = excelParams
                    break
            }


            def dateFormatter = {domain, value->
                value.format("dd-MMM-yyyy")
            }

            Map formatters = [registrationDate: dateFormatter, birthDate: dateFormatter]

			def divisionInstance = Division.get(params.division)

            switch(params.listType){
                case "ALL":
			        voters = voterService.listByDivision(divisionInstance)
                    parameters.title = "Voters"
                    log.info "Printed list of all Voters as ${params.format}"
                    break

                case "NAME":
                    voters = voterService.searchByDivision(params.searchString, divisionInstance,0 , 0)
                    parameters.title = "Voters with Name that Matches: ${params.searchString}"
                    log.info "Printed voters list: Voters with Name that matches: ${params.searchString} as ${params.format} ."
                    break

                case "AFFILIATION":
                    def filterType = FilterType.AFFILIATION
                    def affiliationInstance = Affiliation.get(params.affiliation.toLong())
                    voters = voterService.filter(filterType.AFFILIATION,(Object)affiliationInstance,divisionInstance,0, 0)
                    parameters.title = "Voters with ${affiliationInstance} Affiliation"
                    log.info "Printed voters list: Voters with ${affiliationInstance} Affiliation as ${params.format} ."
                    break

                case "POLLSTATION":
                    def filterType = FilterType.POLL_STATION
                    def pollStationInstance = PollStation.get(params.pollStation.toLong())
                    voters = voterService.filter(filterType.POLL_STATION,(Object)pollStationInstance,divisionInstance,0, 0)
                    parameters.title = "Poll Station # ${pollStationInstance} "
                    log.info "Printed voters list : Voters at Poll Station # ${pollStationInstance} as ${params.format} ."
                    break

            }


			exportService.export(params.format,response.outputStream,voters, 
				fields, labels, formatters,parameters)

		}
	 }



	 def export() {
        def listType = params.listType
        def extension = (params.format == "pdf") ? "pdf" : "xls"
        def format = params.format

        switch(listType){
            case "ALL":
	 	        redirect(action: "list", params: ["extension" : extension, "format":format,"division" : params.division, 
                            listType: listType])
                break
            
            case "NAME":
	 	        redirect(action: "list", params: ["extension" : extension, "format":format,"division" : params.division, 
                            listType: listType, searchString: params.searchString])
                break

            case "AFFILIATION":
	 	        redirect(action: "list", params: ["extension" : extension, "format":format,
                            "division" : params.division, listType: listType, affiliation: params.affiliation])
                break

            case "POLLSTATION":
	 	        redirect(action: "list", params: ["extension" : extension, "format":format,
                            "division" : params.division, listType: listType, pollStation: params.pollStation])
                break
        }
	 }



}
