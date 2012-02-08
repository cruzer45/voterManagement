package bz.voter.management

import grails.test.*

import bz.voter.management.utils.FilterType

class VoterServiceIntegrationTests extends GroovyTestCase {


	def voterService
    def voterElectionService

    protected void setUp() {
        super.setUp()
		  def address = new Address(houseNumber:'2',street:'n/a',
		  		municipality: Municipality.findByName('Belmopan')).save()

		  def division = new Division(name:"Belmopan").save()
		  def pollStation = new PollStation()
		  pollStation.pollNumber = 2
		  pollStation.division = division
		  pollStation.save()
		  

    }

    protected void tearDown() {
        super.tearDown()
    }


    void test_Search_Only_One_Name() {
	 	def searchResults = voterService.search('Jules')
		assertEquals 1, searchResults.size()

    }

	 def test_Search_First_And_Last_Names(){
	 	def searchResults = voterService.search('Cesar, Ross')
		assertEquals 1, searchResults.size()
	 }


	 def test_Search_Partial_Names(){
		def searchResults = voterService.search('C , ro ')
		assertEquals 1, searchResults.size()
	 }


	 def test_Search_Empty_String_Should_Return_All_Voters(){
	 	def searchResults = voterService.search("")
		assertEquals Voter.count(), searchResults.size()
	 }


	 def test_List_By_Division(){
	 	def division = Division.findByName('Albert')
	 	def listResults = voterService.listByDivision(division)

		def totalVoters = 0

		PollStation.findAllByDivision(division).each{poll->
			totalVoters += Voter.countByPollStation(poll)
		}

		assertEquals listResults.size(), totalVoters
	 }


	 def test_List_By_Division_With_Max_And_Offset(){
	 	def division = Division.findByName('Albert')
		def listResults = voterService.listByDivision(division, 0, 5)

		assertEquals 5, listResults.size() 

	 }

	
	def test_search_by_division(){
		def division = Division.findByName('Albert')
		def listResults = voterService.searchByDivision('mel',division,,0,10)

		assertEquals 3, listResults.size()
	}

	def test_count_all_voters_in_a_division(){
		def division = Division.findByName('Albert')

		def totalVoters = voterService.countByDivision(division)

		assertEquals 13, totalVoters
	}


	def test_count_by_division_and_search(){

		def division = Division.findByName('Albert')

		def totalVoters = voterService.countByDivisionAndSearch(division, "a")

		assertEquals 13, totalVoters

		def results = voterService.searchByDivision('a',division,0,10)

		assertEquals 10, results.size()
	}


    def test_filter(){
        def division = Division.findByName('Albert')
        def affiliation = Affiliation.findByName('UNKNOWN')

        def unknownVoters = voterService.filter(FilterType.AFFILIATION,affiliation, division,0 ,0) 

        assertEquals 2, unknownVoters.size()
    }


    void test_affiliation_summary(){
        def division = Division.findByName('Albert')
        def election = Election.findByYear(2012) ?: new Election(year:2012, completed: false, electionType: ElectionType.findByName("General")).save()
        voterElectionService.addAllVoters(election)


        def voters = voterService.summaryByAffiliation(election, division)

        assertNotNull voters
    }


}
