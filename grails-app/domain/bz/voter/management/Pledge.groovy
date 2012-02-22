package bz.voter.management

class Pledge implements Serializable{

	String name
	String code

	String toString(){
		code
	}

    static constraints = {
	 	name(unique:true,blank:false)
		code(unique:true, blank:false,maxSize:1)
    }

	 def beforeValidate(){
	 	name = name?.trim()?.capitalize()
		code = code?.trim()?.capitalize()
	}

	public boolean equals(other){
        if(!(other instanceof Pledge)){
            return false
        }

        other.id == this.id
    }

}
