import grails.util.Environment

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import org.grails.plugins.excelimport.*

import java.util.*

import bz.voter.management.*
import bz.voter.management.importer.*



class BootStrap {

	def springSecurityService
	def voterService
	//def importFileService

	def sessionFactory
	def grailsApplication
	def messageSource
	def unknownMunicipality

   static String fileName = ConfigurationHolder.config.files.dir + "Sample.xls"
   static String albertFile = "/usr/local/files/albert.xlsx"
   static String caribbeanShoresFile = "/usr/local/files/caribbean_shores.xlsx"
   static String appDivision = ConfigurationHolder.config.division

    def init = { servletContext ->

	 	grailsApplication.domainClasses.each{domainClass ->
			if(domainClass.clazz.name.contains("bz.voter.management")){
				domainClass.metaClass.retrieveErrors = {
					def list = delegate?.errors?.allErrors.collect{messageSource.getMessage(it,null)}
					return list?.join("\n")
				}
			}
		}

		if(Sex.count() == 0){
	 		new Sex(name:'Male', code:'M').save()
	 		new Sex(name:'Female', code:'F').save()
			new Sex(name:'Unknown', code:'U').save()
		}

		if(Pledge.count() == 0){
			new Pledge(name:'Yes',code:'Y').save()
			new Pledge(name:'No',code:'N').save()
			new Pledge(name:'Undecided',code:'U').save()
		}


		if(Affiliation.count() == 0){
			new Affiliation(name:'PUP').save()
			new Affiliation(name:'UDP').save()
			new Affiliation(name:'Unknown').save()
		}

		if(Ethnicity.count() == 0){
			new Ethnicity(name:'Creole').save()
			new Ethnicity(name:'Indian').save()
			new Ethnicity(name:'Garifuna').save()
			new Ethnicity(name:'Chinese').save()
			new Ethnicity(name:'Mestizo').save()
			new Ethnicity(name:'Maya').save()
		}


		if(IdentificationType.count() == 0){
	//	new IdentificationType(name:'Passport').save()
		    new IdentificationType(name:'Social Security Card').save()
		    new IdentificationType(name:'Voter Id').save()
		    new IdentificationType(name:'PASSPORT').save()
            new IdentificationType(name:'BIRTH CERTIFICATE').save()
            new IdentificationType(name:'NATURALIZATION DOC.').save()
            new IdentificationType(name:'BAPTISMAL CERTIFICATE').save()
            new IdentificationType(name:'UNKNOWN').save()
            new IdentificationType(name:'DEED POLL').save()
            new IdentificationType(name:'ADOPTION CERTIFICATE').save()
            new IdentificationType(name:'AFFIDAVIT').save()
            new IdentificationType(name:'SCHEDULE I').save(flush:true)
		}

		if(ElectionType.count() == 0){
			new ElectionType(name:'General',code:'GN').save()
			new ElectionType(name:'Municipal',code:'MN').save()
			new ElectionType(name:'Convention',code:'CN').save()
		}

		if(District.count() == 0){
			new District(name:'Corozal',code: 'CZ').save()
			new District(name:'Orange Walk', code:'OW').save()
			new District(name:'Belize', code:'BZ').save()
			new District(name:'Cayo', code:'CY').save()
			new District(name:'Stann Creek',code:'SC').save()
			new District(name:'Toledo',code:'TO').save()
			new District(name:'Unknown',code:'UN').save(flush:true)
		}


        if(Division.count() == 0){
            new Division(name: appDivision, district: District.findByCode('UN')).save()
        }


		if(Municipality.count() == 0){
			new Municipality(name:'Belmopan',district:District.findByCode('CY')).save()
            new Municipality(name: 'Unknown', district:District.findByCode('CY')).save()
            new Municipality(name: 'Unknown', district:District.findByCode('CZ')).save()
            new Municipality(name: 'Unknown', district:District.findByCode('OW')).save()
            new Municipality(name: 'Unknown', district:District.findByCode('SC')).save()
            new Municipality(name: 'Unknown', district:District.findByCode('TO')).save()
            new Municipality(name: 'Unknown', district:District.findByCode('UN')).save()
			unknownMunicipality = new Municipality(name:'Unknown', district: District.findByCode('BZ')).save(flush:true)
		}


        if(Relation.count() == 0){
            new Relation(name: 'Son').save()
            new Relation(name: 'Daughter').save()
            new Relation(name: 'Mother').save()
            new Relation(name: 'Father').save()
            new Relation(name: 'Grandchild').save()
            new Relation(name: 'Grandparent').save()
            new Relation(name: 'Husband').save()
            new Relation(name: 'Wife').save()
            new Relation(name: 'Aunt').save()
            new Relation(name: 'Uncle').save()
            new Relation(name: 'Nephew').save()
            new Relation(name: 'Niece').save()
        }


        if(AddressType.count() == 0 ){
            new AddressType(name: 'Registration').save()
            new AddressType(name: 'Work').save()
            new AddressType(name: 'Alternate').save()
        }

		def userRole = SecRole.findByAuthority('ROLE_USER') ?: new SecRole(authority: 'ROLE_USER').save(failOnError: true)
        def adminRole = SecRole.findByAuthority('ROLE_ADMIN') ?: new SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)
        def pollStationRole = SecRole.findByAuthority('ROLE_POLL_STATION') ?: new SecRole(authority: 'ROLE_POLL_STATION').save(failOnError: true)
        def officeStationRole = SecRole.findByAuthority('ROLE_OFFICE_STATION') ?: new SecRole(authority: 'ROLE_OFFICE_STATION').save(failOnError: true)
        def printVotersRole = SecRole.findByAuthority('ROLE_PRINT_VOTERS') ?: new SecRole(authority: 'ROLE_PRINT_VOTERS').save(failOnError: true)
        def votersManageRole = SecRole.findByAuthority('ROLE_MANAGE_VOTERS') ?: new SecRole(authority: 'ROLE_MANAGE_VOTERS').save(failOnError: true)
		def adminUser = SecUser.findByUsername('admin') ?: new SecUser(
                username: 'admin',
                password: 'p4ssw0rd',
                enabled: true).save(failOnError: true)
 
        if (!adminUser.authorities.contains(adminRole)) {
            SecUserSecRole.create adminUser, adminRole
        }


		Environment.executeForCurrentEnvironment{
			development{

			}//End of development
			
			test{
				def division =  Division.findByName('Albert') ?: new Division(name:'Albert').save()
				def election = Election.findByYear(2011) ?: new Election(year: 2011, electionType: ElectionType.findByName('General')).save()
				//importFileService.importVoters(division,election,'Sample.xls')

			}

			production{
			}
		}

		sessionFactory.currentSession.flush()
   } 


    def destroy = {

    }



	 def populateDataFromXLSForProduction(String divisionName, String fileName){
      /*VoterExcelImporter importer3 = new VoterExcelImporter(fileName);
      def votersMapList = importer3.getVoters()
		def division = Division.findByName(divisionName) ?: new Division(name:divisionName).save()
        
      votersMapList.each { Map voterParams ->
		  		def municipality = Municipality.findByName(voterParams.municipality) ?:  unknownMunicipality

		  		def addressParams = [
					houseNumber: voterParams.houseNumber,
					street: voterParams.street,
					municipality:  municipality
				]

				voterParams.sex = Sex.findByCode(voterParams.sex)
				voterParams.identificationType = IdentificationType.findByName(voterParams.identificationType) ?: new IdentificationType(name:'Unknown').save(flush:true)
				voterParams.affiliation = Affiliation.findByName(voterParams.affiliation) ?: Affiliation.findByName('UNKNOWN')
				voterParams.pollStation = PollStation.findByPollNumber(voterParams.pollStation) ?: new PollStation(pollNumber: "${voterParams.pollStation}", division: division).save()
				voterParams.pledge = Pledge.findByName(voterParams.pledge) ?: Pledge.findByName('Undecided')

				voterParams.address = addressParams
				voterService.add(voterParams)
       }*/ 
	 }

	 def populateDataFromXLSForDev(){
      //new VoterExcelImporter(fileName);
	 }

}
