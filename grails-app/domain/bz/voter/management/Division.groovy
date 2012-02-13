package bz.voter.management

class Division implements Serializable{

	String name
	District district

	String toString() { 
		name
	}

    static constraints = {
	 	name(blank:false, unique:true)
    }

	 def beforeValidate(){
	 	name = name?.trim()
	}
}
