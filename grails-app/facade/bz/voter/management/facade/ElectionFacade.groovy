package bz.voter.management.facade

import org.zkoss.zk.grails.*

import bz.voter.management.Election
import bz.voter.management.spring.SpringUtil

class ElectionFacade {

    Election electionInstance

    def voterElectionService

    def electionService = SpringUtil.getBean('electionService')

    def sessionFactory

    List<Election> getElections() {
        Election.list()
    }

    /**
    Saves an election instance.
    @param map with the information needed to save
    an election:
    <ul>
    	<li>year</li>
    	<li>id</li>
    	<li>electionDate</li>
    	<li>electionType</li>
    	<li>complete</li>
    </ul>
    @return Election instance
    **/

    public Election save(params){
    	electionService.save(params)
    }




}
