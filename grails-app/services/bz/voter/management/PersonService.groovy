package bz.voter.management

class PersonService {

    static transactional = true

	 def messageSource

	/**
	This adds a new instance of person. It forwards the request to save(params).
	**/
    def add(def params) {
	 	save(params)
    }


	/**
	Saves an instance of person
	@param A map of values that match the fields in Person and Address:
    <ul>
	    <li>firstName</li>
	    <li>middleName</li>
	    <li>lastName</li>
	    <li>dateOfBirth</li>
	    <li>sex</li>
        <li>emailAddress</li>
	    <li>ethnicity</li>
    </ul>
	**/
	 def save(def params) {
	 	
		params.birthDate = params.birthDate ? new Date().parse('yyyy-MM-dd', params.birthDate) : null

		saveBasicInformation(params)
	 }




	public Person saveBasicInformation(params){
		def personInstance = params.person?.id ? params.person : new Person()
		def errorMessages

		personInstance.firstName = params.firstName ?: personInstance.firstName
		personInstance.lastName = params.lastName ?: personInstance.lastName
		personInstance.middleName = params.middleName ?: personInstance.middleName
		personInstance.birthDate = params.birthDate ?: personInstance.birthDate
		personInstance.sex = params.sex ?: personInstance.sex
        personInstance.emailAddress = params.emailAddress ?: personInstance.emailAddress
        personInstance.alive = params.alive ?: personInstance.alive

        personInstance.validate()

       if(personInstance.hasErrors()){
			for(error in personInstance.errors.allErrors){
				log.error error
			}

		}else{
			personInstance.save()
		}

		return personInstance

	}


}
