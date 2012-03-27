package bz.voter.management

import org.springframework.transaction.annotation.Transactional

import bz.voter.management.spring.SpringUtil

class ElectionService {


	def sessionFactory
	def voterElectionService

    
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

    @Transactional
    public Election save(params){
    	Election electionInstance = Election.get(params.id) ?: new Election()
      boolean addVoters = electionInstance?.id ? false : true
		
		electionInstance.year = params.year ?: election?.year  	
		electionInstance.electionDate = params.electionDate ?: electionInstance?.electionDate
		electionInstance.electionType = params.electionType ?: electionInstance.electionType
		electionInstance.complete = params.complete ? params.complete : electionInstance.complete

		electionInstance.validate()

		if(electionInstance.hasErrors()){
			log.error electionInstance.retrieveErrors()
		}else{
			electionInstance.save(flush:true)
         if(addVoters){
            voterElectionService.addAllVoters(electionInstance)		
         }
		}
		
		return electionInstance

    }

    /**
    Lists all the elections in the database.
    @return List of maps.
    **/
    def list(){
    	def elections = []
    	//for(election in Election.list([sort:'year'])){
      for(election in Election.list()){

    		def electionType = election.electionType

    		def data = [
    			id: election.id,
    			year: election.year,
    			electionType: electionType,
    			complete: election.complete
    		]

    		elections.push(data)
    	}

    	return elections
    }


    private void flushSession(){
	 	sessionFactory.currentSession.flush()
	 	sessionFactory.currentSession.clear()
	}

}
