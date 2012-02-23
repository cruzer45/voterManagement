package bz.voter.management

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import groovy.json.JsonSlurper

import bz.voter.management.spring.SpringUtil
//import bz.voter.management.importer.* 

/**
Service that has utilities to import files with voters into the database.
**/

class ImportFileService {

	VoterService voterService = SpringUtil.getBean('voterService')
    def addressService   


	/**
	Imports a list of voters in an excel file into the database. 
	@param division : the division that the voters belongs to.
	@param fileName: the name of the file that contains the voters list. This file should be located in 
	ConfigurationHolder.config.files.dir
	@return void
	**/
    def importVoters(Division division,Election election, String fileName) {	
	   
        def votersMapList = [:]

        withRest(id: "importService", uri: ConfigurationHolder.config.importvoters.serverURL ){
        	auth.basic('admin', 'p4ssw0rd')
        	get(path: "importVoters/${fileName}"){resp,json->
        		println "\njson: ${json}"
        		votersMapList  = new JsonSlurper().parseText(json.toString())
        	}        	 	
        	
        }

		def numberOfVoters = 0

		def flush = false
		def transactionCount = 0
        
      votersMapList.each { Map voterParams ->
		  		def municipality = Municipality.findByName(voterParams.municipality) ?:  Municipality.findByNameAndDistrict('Unknown',District.findByCode('UN'))

		  		def addressParams = [
                    addressType: voterParams.addressType ?: AddressType.findByName('Registration'),
					houseNumber: voterParams.houseNumber,
					street: voterParams.street,
					municipality: Municipality.findByName(voterParams.municipality) ?: Municipality.findByNameAndDistrict('Unknown',District.findByCode('UN')),
                    phoneNumber1: voterParams.workPhone,
                    phoneNumber2: voterParams.homePhone,
                    phoneNumber3: voterParams.cellPhone
				]

				voterParams.sex = Sex.findByCode(voterParams.sex)
				voterParams.identificationType = IdentificationType.findByName(voterParams.identificationType) ?: 
																new IdentificationType(name:'Unknown').save(flush:true)
				voterParams.affiliation = Affiliation.findByName(voterParams.affiliation) ?: Affiliation.findByName('UNKNOWN')
				voterParams.pollStation = PollStation.findByPollNumber(voterParams.pollStation) ?: 
														new PollStation(pollNumber: "${voterParams.pollStation}", division: division).save()
				voterParams.pledge = Pledge.findByName(voterParams.pledge) ?: Pledge.findByCode('U')

				voterParams.address = addressParams
				//if(!voterService) voterService = new VoterService()

				transactionCount += 1

				if(transactionCount == 100 || transactionCount == votersMapList.size()){
					flush = true
					transactionCount = 0
				}

				def voter = voterService.add(voterParams,election, flush)
                addressParams.person = voter.person
                addressService.add(addressParams)

				numberOfVoters = voter.id ? (numberOfVoters + 1) : numberOfVoters
       } 

		 return numberOfVoters

    }

}
