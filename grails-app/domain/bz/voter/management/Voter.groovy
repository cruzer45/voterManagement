package bz.voter.management

import java.util.Locale
import java.text.DateFormat

class Voter implements Serializable{
	

	static transients = ["firstName", "middleName","lastName", "birthDate",
		"age", "sex", "address", "alive", "registrationAddress", "emailAddress"]

	Person person
	Date registrationDate
	String registrationNumber
	IdentificationType identificationType
	PollStation pollStation
	Pledge pledge
	Affiliation affiliation
	Zone zone

    static constraints = {
	 	registrationNumber(blank:false)
		affiliation(nullable: true)
		pledge(nullable: true)
		zone(nullable: true)
    }

	 def beforeValidate(){
	 	registrationNumber = registrationNumber?.trim()
	 }


	 def getNumberOfYearsRegistered(){

	 	Calendar today = Calendar.getInstance()
		Calendar dateOfRegistration = Calendar.getInstance()
		dateOfRegistration.setTime(this.registrationDate)

		int years = today.get(Calendar.YEAR) - dateOfRegistration(Calendar.YEAR)

		return years

	 }


	 String getFirstName(){
	 	this.person.firstName
	 }

	 String getLastName(){
	 	this.person.lastName
	 }

	 String getMiddleName(){
	 	this.person.middleName
	 }

	 def getBirthDate(){
	 	//DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.UK).format(person.birthDate)
        this.person.birthDate
	 }

	 int getAge(){
	 	this.person.age
	 }

	 Sex getSex(){
	 	this.person.sex
	 }


     def isAlive(){
        this.person.alive
     }


     String getAddress(){
        "${this.person.registrationAddress}"
     }


	 static totalVotersByPollStation(PollStation pollStation){
	 	Voter.countByPollStation(pollStation)
	 }

    Address getRegistrationAddress(){
        Address.findByPersonAndAddressType(this.person, AddressType.findByName('Registration'))

    }

    String getEmailAddress(){
        this.person.emailAddress
    }


}
