package bz.voter.management

class Election implements Serializable{
	
	Integer year
	Date electionDate
	ElectionType electionType
    boolean complete //After an election is complete, no records pertaining to that election can be modified.

    String toString(){
        "${year} : ${electionType.name}"
    }

    static constraints = {
	 	electionDate(nullable: true)
	 	year(validator: {val,obj->
			if(!obj.id && Election.findByYearAndElectionType(val,obj.electionType)){
				return 'custom.error'
			}
		})
    }

    static mapping = {
        electionType fetch: 'join'        
    }


    public boolean equals(other){
        if(!(other instanceof Election)){
            return false
        }

        other.id == this.id
    }


    
    def beforeValidate(){
        this.complete = this.id ? false : this.complete
    }


}
