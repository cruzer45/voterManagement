package bz.voter.management.facade

import org.zkoss.zkgrails.*

import bz.voter.management.Dependent
import bz.voter.management.Relation
import bz.voter.management.Person
import bz.voter.management.Voter
import bz.voter.management.Relation
import bz.voter.management.Sex

class DependentFacade {

    def sessionFactory


    def save(Voter voter, Person person, Relation relation){
        Depenent.create(voter,person,relation,true)
    }


    def remove(Voter voter, Person person){
        Dependent.remove(voter,person, true)
    }


    /**
    Gets a dependent.
    @param Voter is the voter to whom this dependent belongs to.
    @param Person the individual who is dependent on the voter.
    @return Map with the dependent's information:
    <ul>
        <li>personId</li>
        <li>firstName</li>
        <li>middleName</li>
        <li>lastName</li>
        <li>birthDate</li>
        <li>emailAddress</li>
        <li>sex</li>
        <li>relation</li>
    </ul>
    **/
    def get(Voter voter, Person person){
        def personInstance = Person.get(person.id)
        def dependentInstance = Dependent.get(voter.id, personInstance.id)
        println "Relation: ${dependentInstance.relationId}"
        def _dependent = [
            personId:       personInstance.id,
            firstName:      personInstance.firstName,
            middleName:     personInstance.middleName,
            lastName:       personInstance.lastName,
            birthDate:      personInstance.birthDate,
            emailAddress:   personInstance.emailAddress,
            sex:            Sex.get(personInstance.sex.id),
            relation:       Relation.get(dependentInstance.relationId)            
        ]

        return _dependent
    }


    
    /**
    Updates a dependent's information.
    @param Map with the dependent's information to be updated:
    <ul>
        <li>voterId</li>
        <li>personId</li>
        <li>firstName</li>
        <li>middleName</li>
        <li>lastName</li>
        <li>birthDate</li>
        <li>emailAddress</li>
        <li>sex</li>
        <li>relation</li>
    </ul>
    @return person instance of the dependent.
    **/
    def update(params){
        def voter = Voter.load(params.voterId.toLong())
        def person = Person.load(params.personId.toLong())
        def dependent = Dependent.get(voter,person)
        
        person.firstName    = params.firstName ?: person.firstName
        person.middleName   = params.middleName ?: person.middleName
        person.lastName     = params.lastName ?: person.lastName
        person.birthDate    = params.birthDate ?: person.birthDate 
        params.emailAddress = params.emailAddress ?: person.emailAddress
        params.sex          = params.sex ?: person.sex

        person.validate()

        if(person.hasErrors()){
            log.error person.retrieveErrors()
        }else{
            person.save()
            dependent.relation = params.relation ?: dependent.relation
            dependent.save()
        }

        flushSession()

        return person

    }


    def flushSession(){
        sessionFactory.getCurrent().flush()
        sessionFactory.getCurrent().clear()
    }

}
