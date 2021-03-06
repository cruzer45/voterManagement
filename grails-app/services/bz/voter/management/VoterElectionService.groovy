package bz.voter.management

import java.util.Calendar


import bz.voter.management.utils.PickupTimeEnum
//import bz.voter.management.utils.TwentyFourHourEnum
import bz.voter.management.spring.SpringUtil

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class VoterElectionService {

   static transactional = true

   def jdbcTemplate
   def dataSource
   //def namedParameterJdbcTemplate

	static String  QUERY =  "select ve from VoterElection as ve " +
				"inner join ve.voter as v " +
				"inner join v.person as p " +
				"inner join v.pollStation as poll " +
				"where ve.election =:election " +
				"and poll.division =:division " 

	static String  COUNT_BY_SEARCH_QUERY =  "select count(ve.voter) from VoterElection as ve " +
						"inner join ve.voter as v " +
						"inner join v.person as p " +
					 	"inner join v.pollStation as poll " +
						"where ve.election =:election " +
						"and poll.division =:division "

	static String  FILTER_BY_PLEDGE_QUERY  =  "select ve from VoterElection as ve " +
						"inner join ve.voter as v " +
						"inner join v.person as p " +
					 	"inner join v.pollStation as poll " +
						"where ve.election =:election " +
						"and poll.division =:division " +
                                                "and ve.pledge =:pledge " 

   static String FILTER_BY_PLEDGE_QUERY_PRINT  = "select v.registration_number, p.last_name, "+
                                        "p.first_name, poll.poll_number, af.name as affiliation, " +
                                        "pledge.name as pledge, ve.voted, ve.pickup_time, " +
                                        "ra.house_number, ra.street, mun.name as municipality, " +
                                        "ra.phone_number1 as phone1, ra.phone_number2 as phone2, " +
                                        "ra.phone_number3 as phone3 " +
                                        "from voter as v " +
                                        "inner join voter_election as ve on ve.voter_id=v.id " +
                                        "inner join person as p on v.person_id=p.id " +
                                        "inner join address as ra on ra.person_id=p.id " +
                                        "inner join address_type as ad_type on ra.address_type_id=ad_type.id AND ad_type.name = 'Registration' " +
                                        "inner join municipality as mun on ra.municipality_id=mun.id " +
                                        "inner join affiliation as af on v.affiliation_id=af.id " +
                                        "inner join poll_station as poll on v.poll_station_id=poll.id " +
                                        "inner join pledge as pledge on ve.pledge_id=pledge.id " +
                                        "where ve.election_id =:election_id " +
                                        "and poll.division_id=:division_id " +
                                        "and ve.pledge_id =:pledge_id and voted=:voted " +
                                        "order by p.last_name"

        static String FILTER_BY_PICKUP_TIME_QUERY_PRINT = "select v.registration_number, p.last_name, "+
                                        "p.first_name, poll.poll_number, af.name as affiliation, " +
                                        "pledge.name as pledge, ve.voted, ve.pickup_time, " +
                                        "ra.house_number, ra.street, mun.name as municipality, " +
                                        "ra.phone_number1 as phone1, ra.phone_number2 as phone2, " +
                                        "ra.phone_number3 as phone3 " +
                                        "from voter as v " +
                                        "inner join voter_election as ve on ve.voter_id=v.id " +
                                        "inner join person as p on v.person_id=p.id " +
                                        "inner join address as ra on ra.person_id=p.id " +
                                        "inner join address_type as ad_type on ra.address_type_id=ad_type.id AND ad_type.name = 'Registration' " +
                                        "inner join municipality as mun on ra.municipality_id=mun.id " +
                                        "inner join affiliation as af on v.affiliation_id=af.id " +
                                        "inner join poll_station as poll on v.poll_station_id=poll.id " +
                                        "inner join pledge as pledge on ve.pledge_id=pledge.id " +
                                        "where ve.election_id =:election_id " +
                                        "and poll.division_id=:division_id " +
                                        "and ve.pickup_time like :hour || '%' and voted=:voted " +
                                        "order by p.last_name"

	static String  COUNT_BY_PLEDGE =  "select count(ve.voter) as votes_count from VoterElection as ve " +
					"inner join ve.voter as v " +
					"inner join v.person as p " +
					"inner join v.pollStation as poll " +
					"where ve.election =:election " +
					"and poll.division =:division " +
                                        "and ve.pledge =:pledge"


        static String HOURLY_COUNT_BY_POLLSTATION_QUERY = "SELECT count(ve.voter_id) as votes_count, " +
                                        "CASE WHEN EXTRACT(HOUR FROM ve.vote_time) = 1 THEN 1 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 2 THEN 2 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 3 THEN 3 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 4 THEN 4 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 5 THEN 5 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 6 THEN 6 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 7 THEN 7 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 8 THEN 8 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 9 THEN 9 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 10 THEN 10 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 11 THEN 11 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 12 THEN 12 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 13 THEN 13 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 14 THEN 14 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 15 THEN 15 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 16 THEN 16 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 17 THEN 17 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 18 THEN 18 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 19 THEN 19 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 20 THEN 20 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 21 THEN 21 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 22 THEN 22 " +
                                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 23 THEN 23 " +
                                        "ELSE 0 END AS vote_hour " +
                                        "FROM voter_election as ve " +
                                        "inner join voter as v on ve.voter_id=v.id " +
                                        "inner join poll_station as poll on v.poll_station_id = poll.id " +
                                        "WHERE ve.election_id =:election_id " +
                                        "and poll.id =:poll_station_id " +
                                        "and poll.division_id =:division_id " +
                                        "and ve.vote_time IS NOT NULL and ve.voted IS TRUE " +
                                        "GROUP BY vote_hour"


        static String HOURLY_TOTAL_COUNT_BY_AFFILIATION_QUERY = 
                        "SELECT count(ve.voter_id) as votes_count, " +
                        "affiliation.name as affiliation, " +
                        "CASE WHEN (EXTRACT(HOUR FROM ve.vote_time) = 1) THEN 1 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 2 THEN 2 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 3 THEN 3 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 4 THEN 4 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 5 THEN 5 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 6 THEN 6 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 7 THEN 7 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 8 THEN 8 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 9 THEN 9 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 10 THEN 10 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 11 THEN 11 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 12 THEN 12 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 13 THEN 13 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 14 THEN 14 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 15 THEN 15 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 16 THEN 16 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 17 THEN 17 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 18 THEN 18 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 19 THEN 19 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 20 THEN 20 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 21 THEN 21 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 22 THEN 22 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 23 THEN 23 " +
                        "ELSE 0 END AS vote_hour " +
                        "FROM voter_election as ve " +
                        "INNER JOIN voter as v ON ve.voter_id = v.id "+
                        "INNER JOIN poll_station as poll ON v.poll_station_id = poll.id "+
                        "INNER JOIN affiliation as affiliation ON v.affiliation_id = affiliation.id "+
                        "WHERE ve.election_id =:election_id " +
                        "AND poll.division_id = :division_id " +
                        "AND ve.vote_time IS NOT NULL AND ve.voted IS TRUE " +
                        "GROUP BY affiliation, vote_hour " +
                        "ORDER BY vote_hour "


        static String HOURLY_TOTAL_COUNT_BY_PLEDGE_QUERY = 
                        "SELECT COUNT(ve.voter_id) as votes_count, " +
                        "pledge.name as pledge, " +
                        "CASE WHEN (EXTRACT(HOUR FROM ve.vote_time) = 1) THEN 1 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 2 THEN 2 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 3 THEN 3 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 4 THEN 4 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 5 THEN 5 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 6 THEN 6 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 7 THEN 7 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 8 THEN 8 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 9 THEN 9 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 10 THEN 10 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 11 THEN 11 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 12 THEN 12 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 13 THEN 13 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 14 THEN 14 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 15 THEN 15 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 16 THEN 16 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 17 THEN 17 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 18 THEN 18 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 19 THEN 19 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 20 THEN 20 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 21 THEN 21 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 22 THEN 22 " +
                        "WHEN EXTRACT(HOUR FROM ve.vote_time) = 23 THEN 23 " +
                        "ELSE 0 END AS vote_hour " +
                        "FROM voter_election as ve " +
                        "INNER JOIN voter as v ON ve.voter_id = v.id " +
                        "INNER JOIN poll_station AS poll ON v.poll_station_id = poll.id " +
                        "INNER JOIN pledge as pledge ON ve.pledge_id = pledge.id " +
                        "WHERE ve.election_id = :election_id " +
                        "AND poll.division_id = :division_id " +
                        "AND ve.vote_time IS NOT NULL AND ve.voted IS TRUE " +
                        "GROUP BY pledge, vote_hour " +
                        "ORDER BY vote_hour "


        static String PLEDGE_SUMMARY_QUERY = "select count(ve.voter) as total_voters, pledge.name as pledge "+
                        "from VoterElection ve " +
                        "inner join ve.pledge pledge " +
                        "inner join ve.voter voter " +
                        "inner join voter.pollStation as poll " +
                        "where ve.election =:election " +
                        "and poll.division =:division " +
                        "group by pledge.name " +
                        "order by pledge.name"

                                           

   def sessionFactory
   def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP

   def addAllVoters(Election election) {
	 	if(VoterElection.findAllByElection(election).size() < 1){
			//Add all voters to election
			def pledge = Pledge.findByCode('U')
			def sizeOfList = Voter.count()
            Voter.list().eachWithIndex{voter, index ->
                if((voter.registrationDate.toCalendar().get(Calendar.YEAR) <= election.year) && (voter.isAlive())){
                    VoterElection.create(voter,election,pledge)
                }

                if(index % 100 == 0) cleanUpGorm()
            }
		}

    }

    def cleanUpGorm(){
        def session= sessionFactory.getCurrentSession()
        session.flush()
        session.clear()
        propertyInstanceMap.get().clear()
    }


	List<VoterElection> findAllByElection(Election election){
		(List<VoterElection>) VoterElection.findAllByElection(election)
	}

	 /**
	 Search for voters in an election by first name and/or last name.
	 @arg searchString : first name and/or last name separated by a comma
	 @arg election: the election where we want to search for a voter
	 @arg offset the offset for the query
	 @arg max the maximum size of the results returned
	 @return a List of VoterElection
	 **/

	def search(String searchString, Election election, Division division, int offset, int max){
		executeQuery(searchString, QUERY,election,division,offset,max)

	}


	/**
	Lists all voters in a certain division registered to vote in a specific election.
	@arg election is the election 
	@arg division is the division
	@arg offset offset
	@arg max maximum size of the results
	@returns List<VoterElection> of voters registered to vote in a division for a specific election.
	**/
	public List<VoterElection> listByElectionAndDivision(Election election, Division division, int offset, int max){
		def votersList 
        if(max > 0){
            votersList= VoterElection.executeQuery(QUERY, 
			[election: election, 
			division: division,
			offset: offset,
			max: max])
        }else{
            votersList= VoterElection.executeQuery(QUERY, 
			[election: election, 
			division: division])
        }
        return votersList
	}

	def countByElectionAndDivision(Election election, Division division){
		def numberOfVoters = VoterElection.executeQuery(COUNT_BY_SEARCH_QUERY,[
			election: election,
			division: division])

       return numberOfVoters[0]
	}

	 
	 /**
	 Get count of voters in an election by first name and/or last name.
	 @arg searchString : first name and/or last name separated by a comma
	 @arg election: the election where we want to search for a voter
	 @return total number of records/voters returned by a search.
	 **/
	public int countVoters(String searchString,Election election, Division division){
		def result 
		if(searchString.isAllWhitespace()){
			result = countByElectionAndDivision(election,division)
		}else{
			def resultCount = executeQuery(searchString, COUNT_BY_SEARCH_QUERY, election,division,0,0)
            result = resultCount[0]
		}
		return result
	}



	def executeQuery(String searchString, String searchQuery, Election election, Division division, int offset, int max){
	 	def searchParams
		def results
		def query = searchQuery
		def firstName
		def lastName

	 	if(!searchString.isAllWhitespace()){
	 		searchParams = searchString.split(',').collect{it}

			if(searchParams.size() == 1){

				query +=  "and ((lower(p.firstName) like lower(:firstName)) " +
						  "or (lower(p.lastName) like lower(:lastName))) "  

				firstName =  '%' + searchParams[0].trim() + '%'
				lastName = '%' + searchParams[0].trim() + '%' 


			}else{

				query +=  "and (lower(p.firstName) like lower(:firstName) "+
						  "and lower(p.lastName) like lower(:lastName)) " 

				firstName =  '%' + searchParams[0].trim() + '%' 
				lastName =  '%' + searchParams[1].trim() + '%' 

			}

			if(max == 0){
				results = VoterElection.executeQuery("${query}", [
						firstName: firstName, 
						lastName: lastName, 
						division: division,
						election: election])
			}else{
				results = VoterElection.executeQuery("${query}", [
						firstName: firstName, 
						lastName: lastName, 
						division: division,
						election: election,
						offset: offset,
						max: max])
		}

		}else{
			results = listByElectionAndDivision(election,division,offset, max)

		}

		return results
		
	}


    /**
    Filters list of voters by a specific pledge
    @param Election election for which we want to filter voters.
    @param Division division whose voters we wish to filter.
    @param Pledge
    @param int offset
    @param int max
    @return List of VoterElection
    **/
        public List<VoterElection> filterByPledge(Election election, Division division, Pledge pledge, int offset, int max){
                def _voters 
        
                def query = FILTER_BY_PLEDGE_QUERY + " order by p.lastName"
                if(max > 0){
                        _voters = VoterElection.executeQuery(query,[
                                election: election,
                                division: division,
                                pledge: pledge,
                                offset: offset,
                                max: max
                        ])
                }else{
                        _voters = VoterElection.executeQuery(query,[
                                election: election,
                                division: division,
                                pledge: pledge
                        ])
                }

                return _voters
        }


    def printByPledge(Election election, Division division, Pledge pledge, boolean voted){
        dataSource = SpringUtil.getBean('dataSource')
        SqlParameterSource namedParameters = new MapSqlParameterSource("election_id", election.id)
        namedParameters.addValue("division_id", division.id)
        namedParameters.addValue("pledge_id", pledge.id)
        namedParameters.addValue("voted", voted)
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource)

        def results = namedParameterJdbcTemplate.queryForList(FILTER_BY_PLEDGE_QUERY_PRINT,namedParameters )

        return results

    }



    /**
    Filters voters by a specific pledge and whether they voted or not.
    @param Election
    @param Division
    @param Pledge
    @param boolean voted
    @param int offset
    @param int max
    @return List<VoterElection>
    **/
    public List<VoterElection> filterByPledgeAndVoted(Election election, Division division, Pledge pledge, boolean voted, int offset, int max){
        def _voters

        def query = FILTER_BY_PLEDGE_QUERY + " and voted =:voted order by p.lastName"

        if(max>0){
            _voters = VoterElection.executeQuery(query,
                    [
                        election: election,
                        division: division,
                        pledge: pledge,
                        voted: voted,
                        offset: offset,
                        max: max
                    ])
        }else{
            _voters = VoterElection.executeQuery(query,
                      [
                        election: election,
                        division: division,
                        pledge: pledge,
                        voted: voted
                      ])
        }

        return _voters
    }


    
   /**
   Counts the total number of voters with a specific pledge.
   @param Election
   @param Division
   @param Pledge
   @return int total count of voters with a specific pledge.
   **/
   public int countByPledge(Election election, Division division, Pledge pledge){
        def _count = VoterElection.executeQuery(COUNT_BY_PLEDGE,[
                        election: election,
                        division: division,
                        pledge: pledge
                        ])
        return _count[0]
   }



   /**
   Counts the total number of voters with a specific pledge that either voted or not.
   @param Election
   @param Division
   @param Pledge
   @return int
   **/
   public int countByPledgeAndVoted(Election election, Division division, Pledge pledge, boolean voted){
        def query = COUNT_BY_PLEDGE + " and ve.voted =:voted"
        def _count = VoterElection.executeQuery(query,
                [election: election,
                 division: division,
                 pledge: pledge,
                 voted: voted
                ])


        return _count[0]
   }



   /**
   Fiter the voters at an election by pickup time.
   @param Election
   @param Division
   @param PickupTimeEnum
   @param int offset
   @param int max
   @return List<VoterElection>
   **/
   public List<VoterElection> filterByPickupTime(Election election, Division division, PickupTimeEnum pickupTimeEnum, int offset, int max){
        def hourMarks = pickupTimeEnum.value().split('-')
        def results = []


        def query = QUERY + " AND ve.pickupTime  like (:hour)"

        if(max > 0){
            results = VoterElection.executeQuery(query, [
                division: division,
                election: election,
                hour: (hourMarks[0] + ':%'),
                offset: offset,
                max: max
            ])
        }else{
            results = VoterElection.executeQuery(query, [
                division: division,
                election: election,
                hour: (hourMarks[0] + ':%')
            ])
        }

        return results

   }



    def printByPickupTime(Election election, Division division, PickupTimeEnum pickupTimeEnum, boolean voted){
        dataSource = SpringUtil.getBean('dataSource')
        def hourMarks = pickupTimeEnum.value().split('-')
        SqlParameterSource namedParameters = new MapSqlParameterSource("election_id", election.id)
        namedParameters.addValue("division_id", division.id)
        namedParameters.addValue("hour", hourMarks[0])
        namedParameters.addValue("voted", voted)
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource)

        def results = namedParameterJdbcTemplate.queryForList(FILTER_BY_PICKUP_TIME_QUERY_PRINT,namedParameters )

        return results

    }                

    /**
    Filters the voters at an election by Pickup Time and if they voted or not.
    @param Election
    @param Division
    @param PickupTimeEnum
    @param boolean voted
    @param int offset
    @param int max
    @return List<VoterElection>
    **/
    public List<VoterElection> filterByPickupTimeAndVoted(Election election, Division division, PickupTimeEnum pickupTimeEnum, boolean voted, int offset, int max){
        def hourMarks = pickupTimeEnum.value().split('-')
        def results = []

        def query = QUERY + " AND ve.pickupTime like (:hour) AND ve.voted =:voted "

        if(max>0){
            results = VoterElection.executeQuery(query, [
                division: division,
                election: election,
                hour: (hourMarks[0] + ':%'),
                voted: voted,
                offset: offset,
                max: max
                ])
        }else{
            results = VoterElection.executeQuery(query, [
                division: division,
                election: election,
                hour: (hourMarks[0] + ':%'),
                voted: voted])
        }


        return results
    }


    /**
    Counts the voters at an election that were scheduled to be picked up at a certain time 
    and if they voted or not.
    @param Election
    @param Division
    @param PickupTimeEnum
    @param boolean voted
    @return int : count of voters
    **/
    public int countByPickupTimeAndVoted(Election election, Division division, PickupTimeEnum pickupTimeEnum, boolean voted ){
        def hourMark = pickupTimeEnum.value().split('-')

        def query = COUNT_BY_SEARCH_QUERY + " AND ve.pickupTime like (:hour) AND ve.voted =:voted"

        def results = VoterElection.executeQuery(query, [
                        division: division,
                        election: election,
                        hour: (hourMark[0] + ':%'),
                        voted: voted
                        ])

        return results[0]

    }
   


   public int countByPickupTime(Election election, Division division, PickupTimeEnum pickupTimeEnum){
    def hourMark = pickupTimeEnum.value().split('-')

    def query = COUNT_BY_SEARCH_QUERY +  " and ve.pickupTime like (:hour)"

    def results = VoterElection.executeQuery(query, [
                  division: division,
                  election: election,
                  hour: (hourMark[0] + ':%')
               ])

    return results[0]
   }

    
    
    /**
    Counts the total votes by hour and poll station.
    @param Election
    @param Division
    @param PollStation
    @return List :
    <ul>
        <li>votes_count</li>
        <li>vote_hour</li>
    </ul>
    **/
    def countByHourAndPollStation(Election election,Division division, PollStation pollStation){

        SqlParameterSource namedParameters = new MapSqlParameterSource("election_id", election.id)
        namedParameters.addValue("division_id", division.id)
        namedParameters.addValue("poll_station_id", pollStation.id)
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource)

        def results = namedParameterJdbcTemplate.queryForList(HOURLY_COUNT_BY_POLLSTATION_QUERY, namedParameters)

        return results
    }


    /**
    Counts the total number of votes in a division for a given election, and groups it by hours and voter affiliation.
    @param Election
    @param Division
    @return List with map of votes count:
    <ul>
        <li>votes_count</li>
        <li>affiliation</li>
        <li>vote_hour</li>
    </ul>
    **/
    def countTotalHourlyVotesByAffiliation(Election election, Division division){
        SqlParameterSource namedParameters = new MapSqlParameterSource("election_id", election.id)
        namedParameters.addValue("division_id", division.id)
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource)

        def results = namedParameterJdbcTemplate.queryForList(HOURLY_TOTAL_COUNT_BY_AFFILIATION_QUERY,namedParameters )

        return results

    }



    /**
    Summarizes the total number of votes casted by hour grouped by pledge.
    @param Election
    @param Division
    @return List of maps :
    <ul>
        <li>votes_count</li>
        <li>vote_hour</li>
        <li>pledge</li>
    </ul>
    **/
    def countTotalHourlyVotesByPledge(Election election, Division division){
        SqlParameterSource namedParameters = new MapSqlParameterSource("election_id", election.id)
        namedParameters.addValue("division_id", division.id)
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource)

        def results = namedParameterJdbcTemplate.queryForList(HOURLY_TOTAL_COUNT_BY_PLEDGE_QUERY, namedParameters)
        return results
    }


    /**
    Summary of total voters grouped by pledges.
    @param Election
    @param Division
    @return List of map:
        <ul>
            <li>total_voters</li>
            <li>pledge</li>
        </ul>
    **/
    def summaryByPledge(Election election, Division division){
        def results  = []

        def data = VoterElection.executeQuery(PLEDGE_SUMMARY_QUERY , [
                            division: division,
                            election: election])

        for(pledgedVoters in data ){
            def row = [
                total_voters: pledgedVoters[0],
                pledge: pledgedVoters[1]
            ]
            results.push(row)
        }
        return results
    }



}

