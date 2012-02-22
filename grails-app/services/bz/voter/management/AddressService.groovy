package bz.voter.management

class AddressService {

    static transactional = true

	//def messageSource

	/**
	Add a new address
	@param params is a map with the fields required to create an address:
	1. houseNumber
	2. street
	3. municipality
	**/
    def add(def params) {
		 save(params)
    }


	/**
	@param params is map with the required values to save an address:
    <ul>
	    <li>houseNumber</li>
	    <li>street</li>
	    <li>municipality</li>
	    <li>addressType</li>
        <li>phoneNumber1</li>
        <li>phoneNumber2</li>
        <li>phoneNumber3</li>
        <li>person</li>
    </ul>
	@return Instance of address.
	**/
	 def save(def params){
		def addressInstance = params.id ? Address.get(params.id.toLong()) : new Address()		


		//try{
			addressInstance.houseNumber = params.houseNumber ?: addressInstance.houseNumber
			addressInstance.street = params.street ?: addressInstance.street
			addressInstance.municipality = params.municipality ?: addressInstance.municipality
            addressInstance.addressType = params.addressType ?: AddressType.findByName('Registration')
            addressInstance.phoneNumber1 = params.phoneNumber1 ?: addressInstance.phoneNumber1
            addressInstance.phoneNumber2 = params.phoneNumber2 ?: addressInstance.phoneNumber2
            addressInstance.phoneNumber3 = params.phoneNumber3 ?: addressInstance.phoneNumber3
            addressInstance.person = params.person ?: addressInstance.person

         
            Address.withTransaction{status->
            	addressInstance.validate()
				if(addressInstance.hasErrors()){
					log.error "An error occurred while saving address."
					for(error in addressInstance.errors.allErrors){
						log.error error
					}
				}else{
					addressInstance.save()
					log.info 'Saved address: ' + addressInstance
				}
            }
	

		return addressInstance
		
	 }

}
