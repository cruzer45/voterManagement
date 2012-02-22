package bz.voter.management

class PollStation implements Serializable{

	String pollNumber
	Division division

	static transients = ['name' ]

	String toString(){
		"${pollNumber}"
	}

    static constraints = {
	 	pollNumber(blank: false)

    }

    public boolean equalsTo(other){
        if(!(other instanceof PollStation)){
            return false
        }

        other.id == this.id
    }

	 def getName(){
	 	pollNumber
	 }

}
