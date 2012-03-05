package bz.voter.management

import java.util.Calendar

import bz.voter.management.utils.FilterType
import bz.voter.management.spring.SpringUtil

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class VoterService {

    static transactional = true

	def messageSource //= SpringUtil.getBean('messageSource')

	def personService

	def jdbcTemplate
   	def dataSource// = SpringUtil.getBean('dataSource')

	static String LIST_BY_DIVISION_QUERY = "select v " +
			"from Voter as v " +
			"inner join v.pollStation as poll " +
			"inner join v.person as person " +
			"where  poll.division =:division " +
			"order by person.lastName"
	
	static String LIST_BY_DIVISION_FOR_PRINT_QUERY = "SELECT v.registration_number as registration_number, v.registration_date as registration_date, "+
	 		"p.last_name as last_name , (SELECT EXTRACT(year from AGE(NOW(), p.birth_date))) as age, "+
	 		"p.first_name as first_name, p.birth_date as birth_date, ra.house_number as house_number, "+
	 		"ra.street, mun.name as municipality, " +
	 		"ra.phone_number1, ra.phone_number2, ra.phone_number3 " +
	 		"from voter as v " +
	 		"inner join poll_station as poll on v.poll_station_id = poll.id " +
	 		"inner join person as p on v.person_id = p.id " +
	 		"inner join address as ra on ra.person_id = p.id " +
	 		"inner join address_type as at ON at.id = ra.address_type_id AND at.name = 'Registration' " +
	 		"inner join municipality as mun on ra.municipality_id = mun.id " +
	 		"where poll.division_id =:division_id "	+
	 		"order by p.last_name "	

	static String SEARCH_BY_DIVISION_QUERY = "select v " +
			"from Voter as v " +
			"inner join v.person as p "+
			"inner join v.pollStation as poll " +
			"where poll.division =:division " +
		   "and ((lower(p.firstName) like lower(:firstName)) " 

	static String GET_COUNT_OF_SEARCH_BY_DIVISION_QUERY = "select count(v) " +
			"from Voter as v " +
			"inner join v.person as p "+
			"inner join v.pollStation as poll " +
			"where poll.division =:division " +
		   "and ((lower(p.firstName) like lower(:firstName)) " 

	static String GET_COUNT_OF_ALL_VOTERS_IN_A_DIVISIOIN_QUERY = "select count(v) " +
			"from Voter as v " +
			"inner join v.pollStation as poll " +
			"inner join v.person as person " +
			"where  poll.division =:division " 


	 static String FILTER_BY_AFFILIATION_QUERY = "select v " +
			"from Voter as v " +
			"inner join v.pollStation as poll " +
			"inner join v.person as person " +
			"where  poll.division =:division " +
            "and v.affiliation =:affiliation " +
			"order by person.lastName"

	static String FILTER_BY_AFFILIATION_FOR_PRINT_QUERY = "SELECT v.registration_number as registration_number, " +
			"v.registration_date::date as registration_date, "+
	 		"p.last_name as last_name , (SELECT EXTRACT(year from AGE(NOW(), p.birth_date))) as age, "+
	 		"p.first_name as first_name, p.birth_date::date as birth_date, ra.house_number as house_number, "+
	 		"ra.street, mun.name as municipality, " +
	 		"ra.phone_number1, ra.phone_number2, ra.phone_number3 " +
	 		"from voter as v " +
	 		"inner join poll_station as poll on v.poll_station_id = poll.id " +
	 		"inner join person as p on v.person_id = p.id " +
	 		"inner join address as ra on ra.person_id = p.id " +
	 		"inner join address_type as at ON at.id = ra.address_type_id AND at.name = 'Registration' " +
	 		"inner join municipality as mun on ra.municipality_id = mun.id " +
	 		"where poll.division_id =:division_id "	+
	 		"and v.affiliation_id =:affiliation_id " +
	 		"order by p.last_name "	


	 static String GET_COUNT_BY_AFFILIATION = "select count(v) " +
			"from Voter as v " +
			"inner join v.pollStation as poll " +
			"inner join v.person as person " +
			"where poll.division =:division " +
            "and v.affiliation =:affiliation " 


    static String AFFILIATION_SUMMARY_QUERY = "SELECT COUNT(v.id) as total, " +
            "affiliation.name as party " +
            "from VoterElection ve " +
            "inner join ve.voter v " +
            "inner join v.pollStation poll " +
            "inner join v.affiliation affiliation " +
            "where poll.division =:division " +
            "and ve.election =:election " +
            "group by affiliation.name " +
            "order by affiliation.name "


    static String FILTER_BY_POLLSTATION_QUERY = "select v " +
			"from Voter as v " +
			"inner join v.pollStation as poll " +
			"inner join v.person as person " +
			"where poll.division =:division " +
            "and v.pollStation =:pollStation " +
			"order by person.lastName"

	static String FILTER_BY_POLLSTATION_FOR_PRINT_QUERY = "SELECT v.registration_number as registration_number, " +
			"v.registration_date as registration_date, "+
	 		"p.last_name as last_name , (SELECT EXTRACT(year from AGE(NOW(), p.birth_date))) as age, "+
	 		"p.first_name as first_name, p.birth_date as birth_date, ra.house_number as house_number, "+
	 		"ra.street, mun.name as municipality, " +
	 		"ra.phone_number1, ra.phone_number2, ra.phone_number3 " +
	 		"from voter as v " +
	 		"inner join poll_station as poll on v.poll_station_id = poll.id " +
	 		"inner join person as p on v.person_id = p.id " +
	 		"inner join address as ra on ra.person_id = p.id " +
	 		"inner join address_type as at ON at.id = ra.address_type_id AND at.name = 'Registration' " +
	 		"inner join municipality as mun on ra.municipality_id = mun.id " +
	 		"where poll.division_id =:division_id "	+
	 		"and poll.id =:poll_station_id " +
	 		"order by p.last_name "	



    static String GET_COUNT_BY_POLLSTATION = "select count(v) " +
			"from Voter as v " +
			"inner join v.pollStation as poll " +
			"inner join v.person as person " +
			"where poll.division =:division " +
            "and v.pollStation =:pollStation " 


    static String SEARCH_BY_STREET_QUERY = "select v " +
    		"from Voter as v "+
    		"inner join v.pollStation poll, " +
    		"Address  address " +
    		"inner join address.addressType as addressType " +
    		"where address.street like :street " +
    		"and poll.division =:division " +
    		"and addressType.name = 'Registration' " +
    		"and address.person = v.person " +
    		"order by v.person.lastName "

	/**
	Save a new instance of voters.
	@param Map : a map containing the fields for adding a new voter:
	id
	firstName
	middleName
	lastName
	birthDate
	registrationDate
	registrationNumber
	sex
	houseNumber
	street
	municipality
	homePhone
	workPhone
	cellPhone
	comments
	identificationType
	pollStation
	affiliation

	If id does not exist, then an insert is done, otherwise the existing instance is updated.

	@returns an instance of voter
	
	**/

	def save(def params){


		def errorMessages
		def voterInstance
		def personInstance
		
		if(params.voter?.id){
			voterInstance = params.voter
			params.person = voterInstance.person
		}else{
			voterInstance = new Voter()
		}

		if(!personService){
			personService = new PersonService()
		}
		personInstance = personService.save(params)

		if(personInstance.hasErrors()){
			for(error in personInstance.errors.allErrors){
				errorMessages = errorMessages + "\n" + 
					messageSource.getMessage(error,null)
			}

			voterInstance.errors.rejectValue("person", "person.error", errorMessages)
			
		}else{
			voterInstance.person = personInstance
			voterInstance.registrationDate = new Date().parse('yyyy-MM-dd',params.registrationDate) ?: voterInstance.registrationDate
			voterInstance.registrationNumber = params.registrationNumber ?: voterInstance.registrationNumber
			voterInstance.identificationType = params.identificationType ?: voterInstance.identificationType
			voterInstance.pollStation = params.pollStation ?: voterInstance.pollStation
			voterInstance.affiliation = params.affiliation ?: voterInstance.affiliation

			voterInstance.validate()

			if(voterInstance.hasErrors()){
                log.error "Could not save voter:"
                for(error in voterInstance.errors){
                    log.error error
                    println error
                }
                
			}else{
				voterInstance.save()
			}
		}

		return voterInstance
		
	}


	public Voter add(HashMap params,Election election, boolean flush){
		def voterInstance = save(params)

		if(!voterInstance.hasErrors()){
            log.info "Imported voter: ${voterInstance}"
			def year = voterInstance.registrationDate.toCalendar().get(Calendar.YEAR)

			def elections = Election.findAllByYearGreaterThanEquals(year)

			for(e in elections){
				if(e == election){
					VoterElection.create(voterInstance,election,params.pledge,flush)
				}else{
					VoterElection.create(voterInstance,e,null,flush)
				}
			}
			
		}else{
            log.error "Error importing voter: "
            for(error in voterInstance.errors){
                log.error error
            }
        }

		return voterInstance
	}


	/**
	Searches for voters that match a certain name. 
	The search pattern can be an empty string, in which case a list of all Voters is returned.
	A single string, in which case a search for a voter whose first name of last name match the
	string.
	A string separated by a comma. The first string being the first name and the last string a 
	last name.
	@return A list of results
	**/

	def search(String searchString){
	 	def searchParams
		def results

	 	if(!searchString.isAllWhitespace()){
	 		searchParams = searchString.split(',').collect{it}

			def criteria = Voter.createCriteria()
			results = criteria.list {
				person{
					if(searchParams.size() == 1){
					or {
						ilike("firstName", "%${searchParams[0].trim()}%")
						ilike("lastName", "%${searchParams[0].trim()}%")
					}
				}else{
					and{
						ilike("firstName", "%${searchParams[0].trim()}%")
						ilike("lastName", "%${searchParams[1].trim()}%")
					}
				}
				}
			}
		}else{
			results = Voter.list()
		}

		return results

	 }


	def searchByDivision(String searchString, Division division, int offset, int max){
	 	def searchParams
		def results
		def firstName
		def lastName

		def query = SEARCH_BY_DIVISION_QUERY


	 	if(!searchString.isAllWhitespace()){
	 		searchParams = searchString.split(',').collect{it}

			if(searchParams.size() == 1){
				query = query + 
					" or (lower(p.lastName) like lower(:lastName))) " +
                    " order by p.lastName "

				firstName = "%${searchParams[0].trim()}%"
				lastName = "%${searchParams[0].trim()}%"
			}else{
				query = query + 
					" and (lower(p.lastName) like lower(:lastName))) " +
                    " order by p.lastName "

				firstName = "%${searchParams[0].trim()}%"
				lastName = "%${searchParams[1].trim()}%"
			}

			if(max>0){
				results = Voter.executeQuery(query, [
					division: division,
					firstName: firstName,
					lastName: lastName,
					offset: offset,
					max: max])
			}else{
				results = Voter.executeQuery(query, [
					division: division,
					firstName: firstName,
					lastName: lastName])
			}

		}else{
			results = listByDivision(division, offset, max)
		}

		return results

	}


	 /**
	 This is used for pagination. An offset and maximum number of records are specified.
	 @args division : the Division that we want to get the voters from.
	 @args offset the offset from where to start the query
	 @args max the maximum number of records to acquire
	 **/
	def listByDivision(Division division, int offset, int max){
	 	def results

		results = Voter.executeQuery(LIST_BY_DIVISION_QUERY,
			[division: division,
			 offset: offset,
			 max: max
			])

		return results
	 	
	}


	def listByDivision(Division division){
		Voter.executeQuery(LIST_BY_DIVISION_QUERY,[division:division])
	 }

	def printByDivision(Division division){	 		 
		dataSource = SpringUtil.getBean('dataSource')
	 	SqlParameterSource namedParameters = new MapSqlParameterSource("division_id", division.id)
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource)

        def results = namedParameterJdbcTemplate.queryForList(LIST_BY_DIVISION_FOR_PRINT_QUERY, namedParameters)
        return results
    }


	/**
	Returns the total number of voters in a division.
	@arg division 
	@returns int total number of voters in division.
	**/
	def countByDivision(Division division){
	 	def results = Voter.executeQuery(GET_COUNT_OF_ALL_VOTERS_IN_A_DIVISIOIN_QUERY, 
			[division: division])

		return results[0]
 	}


	def countByDivisionAndSearch(Division division, String searchString){
		def firstName
		def lastName
	 	def searchParams = searchString.split(',').collect{it}

		def query = GET_COUNT_OF_SEARCH_BY_DIVISION_QUERY

		if(searchParams.size() == 1){
			query = query + 
				" or (lower(p.lastName) like lower(:lastName)))"

			firstName = "%${searchParams[0].trim()}%"
			lastName = "%${searchParams[0].trim()}%"
		}else{
			query = query + 
				" and (lower(p.lastName) like lower(:lastName)))"

			firstName = "%${searchParams[0].trim()}%"
			lastName = "%${searchParams[1].trim()}%"
		}

		def results = Voter.executeQuery(query ,
			[division:division, firstName: firstName, lastName: lastName])

		results[0]
	}


    def countByAffiliation(division,affiliation){
        def results = Voter.executeQuery(GET_COUNT_BY_AFFILIATION,
            [division: division,
            affiliation: affiliation
            ])

        return results[0]
    }


    def countByPollStation(Division division, PollStation pollStation){
        def results = Voter.executeQuery(GET_COUNT_BY_POLLSTATION,
            [division: division,
             pollStation: pollStation])

        return results[0]
    }


    /**
    Search all voters who live in a specified street.
    @param String street
    @param Division division
    @return List<Voter>
    **/
    List<Voter> searchByStreet(Division division, String street){
    	Voter.executeQuery(SEARCH_BY_STREET_QUERY,[
    		street: ('%' + street + '%'),
    		division: division])
    }

    
    /**
    Summarize the total registered voters grouped by affiliation.
    @param Election
    @param Division
    @return List with map :
    <ul>
        <li>total</li>
        <li>party</li>
    </ul>
    **/
    def summaryByAffiliation(Election election, Division division){
        def results = []
        def data = Voter.executeQuery(AFFILIATION_SUMMARY_QUERY,
            [division: division,
             election: election
            ])

        for(voter in data){
            def row = [
                total: voter[0],
                party: voter[1]
                ]

            results.push(row)
        }

        return results
    }



    /**
    Filters the voters list by a specific field.
    @param filterType
    @param Division the division whose voters we wish to filte.
    @param int offset query starting point
    @param int max the maximum number of records to fetch
    @return List of voters.
    **/
    def filter(filterType,filterObject,division,int offset, int max){
        def results

        switch(filterType){
            case filterType.AFFILIATION:
                def affiliation = (Affiliation) filterObject
                if(max > 0){
                    results = Voter.executeQuery(FILTER_BY_AFFILIATION_QUERY, 
                        [division: division, 
                        affiliation: affiliation,
                        offset: offset,
                        max: max])
                }else{
                    results = Voter.executeQuery(FILTER_BY_AFFILIATION_QUERY, 
                        [division: division, 
                        affiliation: affiliation])
                }
                break

           case filterType.POLL_STATION:
                def pollStation = (PollStation) filterObject
                if(max>0){
                    results = Voter.executeQuery(FILTER_BY_POLLSTATION_QUERY,
                        [division: division,
                        pollStation: pollStation,
                        offset: offset,
                        max: max])
                }else{
                    results = Voter.executeQuery(FILTER_BY_POLLSTATION_QUERY,
                                [division: division, pollStation: pollStation])
                }

                break
        }


        return results
    }


    def printByAffiliation(Division division, Affiliation affiliation){
    	dataSource = SpringUtil.getBean('dataSource')
    	SqlParameterSource namedParameters = new MapSqlParameterSource("division_id", division.id)
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        namedParameters.addValue("affiliation_id", affiliation.id)

        def results = namedParameterJdbcTemplate.queryForList(FILTER_BY_AFFILIATION_FOR_PRINT_QUERY, namedParameters)
        return results

    }


    def printByPollStation(Division division, PollStation pollStation){
    	dataSource = SpringUtil.getBean('dataSource')
    	SqlParameterSource namedParameters = new MapSqlParameterSource("division_id", division.id)
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
        namedParameters.addValue("poll_station_id", pollStation.id)

        def results = namedParameterJdbcTemplate.queryForList(FILTER_BY_POLLSTATION_FOR_PRINT_QUERY, namedParameters)
        return results
    }


    /**
    Adds a list of voters to a specified Zone.
    @param List<Voter> list of voters that will be added to a zone.
    @param Zone
    **/
    public void addVotersToZone(List<Voter> votersList, Zone zone){
    	for(Voter voter : votersList){
    		voter.zone = zone
    		voter.save()
    	}

    }

    public void removeVotersFromZone(List<Voter> votersList){
    	for(Voter voter : votersList){
    		voter.zone = null
    		voter.save()
    	}
    }

}
