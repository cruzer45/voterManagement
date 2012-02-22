package bz.voter.management

import org.apache.commons.lang.builder.HashCodeBuilder

class Dependent implements Serializable{

    Relation    relation
    Voter       voter
    Person      person

    static constraints = {
    }

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (voter) builder.append(voter.id)
		if (person) builder.append(person.id)
		builder.toHashCode()
	}

	boolean equals(other) {
		if (!(other instanceof Dependent)) {
			return false
		}

		other.voter?.id == voter?.id &&
			other.person?.id == person?.id
	}


    static Dependent get(long voterId, long personId){
        find 'from Dependent where voter.id=:voterId and person.id=:personId',
            [voterId: voterId, personId: personId]
    }


    static Dependent create(Voter voter, Person person, Relation relation, boolean flush=false){
        new Dependent(voter: voter, person: person, relation: relation).save(flush:flush)
    }


    /**
    Deletes a Dependent.
    @param Voter
    @param Person
    @param flush : a boolean
    @return true if successful delete
    **/
    static boolean remove(Voter voter, Person person, boolean flush=false){
        Dependent instance = Dependent.findByVoterAndPerson(voter,person)

        if(!instance){
            return false
        }

        instance.delete(flush:flush)
        return true
    }


    static void removeAll(Voter voter, boolean flush=false){
        executeUpdate "DELETE FROM Dependent WHERE voter=:voter", [voter: voter]
    }


    
    static List<Dependent> getByVoter(Voter voter){
        findAll 'from Dependent where voter.id=:voterId',
            [voterId: voter.id]
    }



	static mapping = {
		id composite: ['voter', 'person']
		version false
        relation fetch:'join'
        voter  fetch:'join'
        person fetch:'join'
	}
}
