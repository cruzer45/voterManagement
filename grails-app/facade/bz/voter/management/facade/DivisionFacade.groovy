package bz.voter.management.facade

import org.zkoss.zk.grails.*

import bz.voter.management.*

class DivisionFacade {

    Division selected

    List<Division> getDivisions() {
        Division.list()
    }



    /**
    Save or update an instance of Division.
    @params Map with values required to save an instance of Division:
    <ul>
    	<li>id</li>
    	<li>name</li>
    	<li>district</li>
    </ul>
    @return Division
    **/
    Division save(params){

    	Division divisionInstance

    	Division.withTransaction{status->
    		divisionInstance = params.id ? Division.get(params.id.toLong()) : new Division()
    		divisionInstance.name = params.name ?: divisionInstance.name
    		divisionInstance.district = params.district ?: divisionInstance.district

    		divisionInstance.validate()

    		if(divisionInstance.hasErrors()){
    			for(error in divisionInstance.errors){
    				log.error
    			}
    		}else{
    			divisionInstance.save()
    		}
    	}

    	return divisionInstance
    }

}
