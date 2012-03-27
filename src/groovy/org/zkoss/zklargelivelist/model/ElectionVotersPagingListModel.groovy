package org.zkoss.zklargelivelist.model;

import java.util.List;
import bz.voter.management.*
import bz.voter.management.utils.FilterType
import bz.voter.management.utils.PickupTimeEnum


public class ElectionVotersPagingListModel extends AbstractElectionVotersPagingListModel<VoterElection> {

	def voterElectionService 
    def sessionFactory


    private String searchType
    private static String ALL = "ALL"
    private static String NAME_SEARCH = "NAME"
    private static String PLEDGE_FILTER = "PLEDGE"
    private static String PICKUP_TIME_FILTER = "PICKUP_TIME_FILTER"


    private PickupTimeEnum pickupTimeEnum

	public ElectionVotersPaginListModel(){
	}


	public ElectionVotersPagingListModel(Election election, Division division, int startPageNumber, int pageSize){
		super(election, division, startPageNumber, pageSize)
	}


	public ElectionVotersPagingListModel(String searchString, Election election, Division division, int startPageNumber, int pageSize){
		super(searchString, election, division, startPageNumber, pageSize)
	}

	public ElectionVotersPagingListModel(FilterType filterType ,Object filterObject, Election election, Division division, int startPageNumber, int pageSize){
		super(filterType, filterObject, election, division, startPageNumber, pageSize)
	}

    public ElectionVotersPagingListModel(FilterType filterType, Object filterObject, Election election, Division division, boolean voted, int startPageNumber, int pageSize){
        super(filterType, filterObject, election, division, voted, startPageNumber, pageSize)
    }


	/**
	Gets a list of voters in a specified division whose first and/or last name match the search string.
	@arg search the search string that is used to search for the voters.
	@arg itemStartNumber the offset
	@arg pageSize the size of the results returned
	@returns List<Map> a list of voters. We return a map because zk has issues maintaing
    a hibernate session across requests:
    <ul>
        <li>voterElection</li>
        <li>voter</li>
        <li>registrationDate</li>
        <li>lastName</li>
        <li>firstName</li>
        <li>houseNumber</li>
        <li>street</li>
        <li>municipality</li>
        <li>sex</li>
        <li>age</li>
        <li>birthDate</li>
        <li>pollNumber</li>
        <li>voted</li>
        <li>pollStation</li>
        <li>affiliation</li>
        <li>pickupTime</li>
        <li>pledge</li>
    </ul>
	**/
	@Override
	protected List<Map> getPageData(String search, int itemStartNumber, int pageSize) {
		voterElectionService = new VoterElectionService()
        def votersMap = []
		for(_voterElection in voterElectionService.search(search, getElection(), getDivision(), itemStartNumber, pageSize)){
            def instance = doMap(_voterElection)
            votersMap.push(instance)
        }
        searchType = NAME_SEARCH
        return votersMap
	}


	/**
	Gets a list of voters in a specified division eligible to vote in an election.
	@arg itemStartNumber the offset
	@arg pageSize the size of the results returned
	@returns List<Map> a list of voters. We return a map because zk has issues maintaing
    a hibernate session across requests:
    <ul>
        <li>voterElection</li>
        <li>voter</li>
        <li>registrationDate</li>
        <li>lastName</li>
        <li>firstName</li>
        <li>houseNumber</li>
        <li>street</li>
        <li>municipality</li>
        <li>sex</li>
        <li>age</li>
        <li>birthDate</li>
        <li>pollNumber</li>
        <li>voted</li>
        <li>pollStation</li>
        <li>affiliation</li>
        <li>pickupTime</li>
        <li>pledge</li>
    </ul>
	@Override
	**/
	protected List<Map> getPageData(int itemStartNumber, int pageSize) {
		voterElectionService = new VoterElectionService()
        def votersMap = []
		for(_voterElection in voterElectionService.listByElectionAndDivision(getElection(),getDivision(), itemStartNumber, pageSize )){
            def instance = doMap(_voterElection)
            votersMap.push(instance)
        }

        searchType = ALL
        return votersMap
	}


    protected List<Map> getPageData(FilterType filterType, Object filterObject, int itemStartNumber, int pageSize){
        voterElectionService = new VoterElectionService()
        def votersMap = []
        def _voterElectionList = []
        switch(filterType){
            case filterType.PLEDGE:
                def pledge = (Pledge) filterObject
                super.setPledge(pledge)
                if(getVoted() == null){
                    _voterElectionList = voterElectionService.filterByPledge(getElection(),getDivision(),pledge,itemStartNumber, pageSize)
                }else{
                    _voterElectionList = voterElectionService.filterByPledgeAndVoted(getElection(),getDivision(),pledge,getVoted(),itemStartNumber, pageSize)
                }
                for(_voterElection in _voterElectionList){
                    def instance =doMap(_voterElection)
                    votersMap.push(instance)
                }
                searchType = PLEDGE_FILTER
                break

           case filterType.PICKUP_TIME:
                
                pickupTimeEnum = (PickupTimeEnum) filterObject
                if(getVoted() == null){
                    _voterElectionList = voterElectionService.filterByPickupTime(getElection(), getDivision(),pickupTimeEnum, itemStartNumber, pageSize)
                }else{
                    _voterElectionList = voterElectionService.filterByPickupTimeAndVoted(getElection(), getDivision(),pickupTimeEnum, getVoted(), itemStartNumber, pageSize)
                }
                for(_voterElection in _voterElectionList)
                {
                    def instance = doMap(_voterElection)
                    votersMap.push(instance)
                }
                searchType = PICKUP_TIME_FILTER
                break
        }

        return votersMap

    }


	/**
	Returns total size of voters returned by a query.
	**/
	@Override
	public int getTotalSize() {

        def totalSize
		def search = getSearchString()
		voterElectionService = new VoterElectionService()
		search = search ? search : ""

        switch(searchType){
            case ALL:
                totalSize = voterElectionService.countVoters(search, getElection(), getDivision())
                break
            case NAME_SEARCH:
                totalSize = voterElectionService.countVoters(search, getElection(), getDivision())
                break

            case PLEDGE_FILTER:
                totalSize = (getVoted() == null) ? voterElectionService.countByPledge(getElection(), getDivision(), getPledge()) : voterElectionService.countByPledgeAndVoted(getElection(), getDivision(), getPledge(), getVoted())
                break

            case PICKUP_TIME_FILTER:
                totalSize = (getVoted() == null)  ?  voterElectionService.countByPickupTime(getElection(), getDivision(), pickupTimeEnum) : voterElectionService.countByPickupTimeAndVoted(getElection(), getDivision(), pickupTimeEnum, getVoted())
                break


        }

		return totalSize

	}


    /**
    Converts a voter instance to a map suitable for a grid.
    @param Voter
    @return Map
    **/
    private def doMap(_voterElection){
        Voter _voter = Voter.get(_voterElection.voterId)
        def _registrationAddress = Address.get(_voter.registrationAddress.id)
        def instance = [
            voterElection:      _voterElection,
            voter:              _voter,
            registrationDate:   _voter.registrationDate,
            registrationNumber: _voter.registrationNumber,
            lastName:           _voter.lastName,
            firstName:          _voter.firstName,
            houseNumber:        _registrationAddress?.houseNumber,
            street:             _registrationAddress?.street,
            municipality:       Municipality.get(_registrationAddress?.municipality.id),
            sex:                Sex.get(_voter.sex.id),
            age:                _voter.age,
            birthDate:          _voter.birthDate,
            pollStation:        _voter.pollStation,
            pollNumber:         _voter.pollStation.pollNumber,
            voted:              _voterElection.voted,
            affiliation:        Affiliation.get(_voter.affiliation.id),
            pickupTime:         _voterElection.pickupTime,
            pledge:             Pledge.get(_voterElection.pledgeId),
            complete:           Election.get(_voterElection.electionId).complete

        ]

        return instance
    }
	

}

