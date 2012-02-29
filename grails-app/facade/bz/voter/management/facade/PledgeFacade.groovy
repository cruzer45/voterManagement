package bz.voter.management.facade

import org.zkoss.zk.grails.* 

import bz.voter.management.*

class PledgeFacade {

    Pledge selected

    List<Pledge> getPledges() {
        Pledge.list()
    }


    Pledge save(params){
    	Pledge pledge 
    	
    	Pledge.withTransaction{status->
    		pledge = params.id ? Pledge.get(params.id.toLong()) : new Pledge()
    		pledge.name = params.name
    		pledge.code = params.code

    		pledge.validate()

    		if(pledge.hasErrors()){
    			for(error in pledge.errors){
    				log.error error 
    			}
    		}else{
    			pledge.save()
    		}
    	}

    	return pledge
    }
}
