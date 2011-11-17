package bz.voter.management

class VoterService {

    static transactional = true

	 def messageSource

	/*
	Save a new instance of voters.
	@param params : a map containing the fields for adding a new voter:
	id
	firstName
	middleName
	lastName
	birthDate
	registrationDate
	registrationNumber
	sex
	houseNumber
	street
	municipality
	homePhone
	workPhone
	cellPhone
	comments
	identificationType
	pollStation
	pledge
	affiliation

	If id does not exist, then an insert is done, otherwise the existing instance is updated.

	@returns an instance of voter
	
	*/

    def saveVoter(params) {
		def errorMessages =""
	 	def addressInstance 
	 	def personInstance
	 	def voterInstance

		Voter.withTransaction{status->

		if(params.id){
			voterInstance = Voter.get(params.id)
			personInstance = voterInstance.person
			addressInstance = personInstance.address
		}else{
			voterInstance = new Voter()
			addressInstance = new Address()
			personInstance = new Person()
		}

		addressInstance.houseNumber = params.houseNumber ?: addressInstance.houseNumber
		addressInstance.street = params.street ?: addressInstance.street
		addressInstance.municipality = params.municipality ?: addressInstance.municipality
		addressInstance.validate()

		if(addressInstance.hasErrors()){
			for(error in addressInstance.errors.allErrors){
				log.error error
				errorMessages = errorMessages + "\n" + (messageSource.getMessage(error,null))
			}

			voterInstance.errors.rejectValue("person", "address.error",errorMessages) 
		}else{
			addressInstance.save()
		}


		if(addressInstance.id){

		personInstance.firstName = params.firstName ?: personInstance.firstName
		personInstance.middleName = params.middleName ?: personInstance.middleName
		personInstance.lastName = params.lastName ?: personInstance.lastName
		personInstance.birthDate = params.birthDate ?: personInstance.birthDate
		personInstance.sex = params.sex ?: personInstance.sex
		personInstance.address = addressInstance 
		personInstance.comments = params.comments ?: personInstance.comments
		personInstance.workPhone = params.workPhone ?: personInstance.workPhone
		personInstance.cellPhone = params.cellPhone ?: personInstance.cellPhone
		personInstance.homePhone = params.homePhone ?: personInstance.homePhone

		personInstance.validate()

		if(personInstance.hasErrors()){
			for(error in personInstance.errors.allErrors){
				log.error error
				errorMessages = errorMessages + "\n" + (messageSource.getMessage(error,null))
			}

			voterInstance.errors.rejectValue("person", "person.error",errorMessages) 
			addressInstance.delete()
		}else{
			personInstance.save()
		}

		}


		if(personInstance.id){

		voterInstance.registrationNumber = params.registrationNumber
		voterInstance.registrationDate = params.registrationDate ?: voterInstance.registrationDate
		voterInstance.identificationType = params.identificationType ?: voterInstance.identificationType
		voterInstance.pollStation = params.pollStation ?: voterInstance.pollStation
		voterInstance.pledge = params.pledge ?: voterInstance.pledge
		voterInstance.affiliation = params.affiliation ?: voterInstance.affiliation
		voterInstance.person = personInstance

		voterInstance.validate()

		if(voterInstance.hasErrors()){
			for(error in voterInstance.errors.allErrors){
				log.error error
			}

			personInstance.delete()

		}else{
			voterInstance.save(flush:true)
		}
		}

		}//end of Voter.withTransaction


		return voterInstance

    }
}
