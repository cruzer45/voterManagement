package bz.voter.management.facade

import org.zkoss.zk.grails.*

import bz.voter.management.*

import org.codehaus.groovy.grails.commons.ConfigurationHolder

class PollStationFacade {

    PollStation selected

    List<PollStation> getPollStations() {
        PollStation.list()
    }


    /**
    Saves a poll station instance.
    @param Map of values needed to save a pollstation instance:
    <ul>
    	<li>id</li>
    	<li>pollNumber</li>
    </ul>
    @return PollStation
    **/
    PollStation save(params){
    	PollStation pollStation 

    	PollStation.withTransaction{status->
    		pollStation = params.id ? PollStation.get(params.id.toLong()) : new PollStation()
    		pollStation.pollNumber = params.pollNumber
    		pollStation.division = Division.findByName(ConfigurationHolder.config.division)

    		pollStation.validate()

    		if(pollStation.hasErrors()){
    			for(error in pollStation.errors){
    				log.error error
    			}
    		}

    		else{
    			pollStation.save()
    		}
    	}

    	return pollStation
    }

    
}
