package bz.voter.management

class Zone {

	String name
    Voter captain

    String toString(){ name }

    static constraints = {
    	name(unique: true, blank: false)
        captain(unique: true, nullable: true)
    }

    def beforeValidate(){
    	this.name = name?.trim()?.toLowerCase()?.capitalize()
    }


    List<Voter> voters(){
    	Voter.findAllByZone(this)
    }

}
