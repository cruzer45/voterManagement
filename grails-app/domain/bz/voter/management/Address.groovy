package bz.voter.management

class Address implements Serializable{

    AddressType addressType
	String houseNumber
	String street
	Municipality municipality
    Person person
    String phoneNumber1
    String phoneNumber2
    String phoneNumber3


	String toString(){
		"${houseNumber} ${street} , ${municipality.name}"
	}

    static constraints = {
	 	street(blank: false)
        phoneNumber1(nullable:true)
        phoneNumber2(nullable:true)
        phoneNumber3(nullable:true)
    }

    static mapping = {
    	person fetch: 'join'
    	municipality fetch: 'join'
    	addressType fetch: 'join'
    }


	 def beforeValidate(){
	 	street = street?.trim()?.capitalize()
		houseNumber = houseNumber?.trim()
	 }
}
