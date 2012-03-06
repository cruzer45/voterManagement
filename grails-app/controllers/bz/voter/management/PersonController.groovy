package bz.voter.management

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import bz.voter.management.utils.VoterListTypeEnum
import bz.voter.management.utils.FilterType

class PersonController {

	def exportService
	def voterService

    def excelFields = ["registration_date", "registration_number", "last_name", "first_name",
                        "age", "birth_date", "house_number",
                        "street", "municipality","phone_number1", "phone_number2",
                        "phone_number3"]
    
    def pdfFields = ["registration_date", "registration_number", "last_name", "first_name",
                        "age", "birth_date", "house_number",
                        "street", "municipality"]

    def excelLabels = ["registration_date": "Registration Date", "registration_number": "Registration Number",
                        "first_name": "First Name", "last_name": "Last Name", "birth_date": "DOB",
                        "age": "Age", 
                        "house_number": "House #",
                        "street": "Street", 
                        "municipality": "municipality",
                        "phone_number1" : "Phone 1",
                        "phone_number2" : "Phone 2", "phone_number3" : "Phone 3"]

    def pdfLabels = ["registration_date": "Registration Date", 
                    "registration_number": "Registration Number",
                    "first_name": "First Name", 
                    "last_name": "Last Name", 
                    "birth_date": "DOB",
                    "age": "Age", 
                    "house_number": "House #",
                    "street": "Street", 
                    "municipality": "municipality"]


    def pdfParams = ["column.widths": [0.1, 0.1, 0.2, 0.1, 0.1, 0.1, 0.1,0.1,0.1]]
    def excelParams = ["column.widths": [0.1, 0.1, 0.1, 0.1, 0.05, 0.1, 0.05, 0.1, 0.1, 0.1, 0.05, 0.05]]


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
                //new Date().parse("dd-MMM-yyyy", value)
            }

            

            Map formatters = [registration_date: dateFormatter, birth_date: dateFormatter]
            //Map formatters = [:]

			def divisionInstance = Division.get(params.division)

            switch(params.listType){
                case "ALL":
                    voters = voterService.printByDivision(divisionInstance)
                    parameters.title = "Voters"                    
                    break

                case "NAME":
                    voters = voterService.searchByDivision(params.searchString, divisionInstance,0 , 0)
                    parameters.title = "Voters with Name that Matches: ${params.searchString}"
                    log.info "Printed voters list: Voters with Name that matches: ${params.searchString} as ${params.format} ."
                    break

                case "AFFILIATION":
                    def filterType = FilterType.AFFILIATION
                    def affiliationInstance = Affiliation.get(params.affiliation.toLong())
                    voters = voterService.printByAffiliation(divisionInstance,affiliationInstance)
                    parameters.title = "Voters with ${affiliationInstance} Affiliation"
                    log.info "Printed voters list: Voters with ${affiliationInstance} Affiliation as ${params.format} ."
                    break

                case "POLLSTATION":
                    def filterType = FilterType.POLL_STATION
                    def pollStationInstance = PollStation.get(params.pollStation.toLong())
                    voters = voterService.printByPollStation(divisionInstance,pollStationInstance)
                    parameters.title = "Poll Station # ${pollStationInstance} "
                    log.info "Printed voters list : Voters at Poll Station # ${pollStationInstance} as ${params.format} ."
                    break

            }


			exportService.export(params?.format,response.outputStream,voters, 
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
