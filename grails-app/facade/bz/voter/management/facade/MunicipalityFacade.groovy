package bz.voter.management.facade

import org.zkoss.zk.grails.*

import bz.voter.management.*

class MunicipalityFacade {

    Municipality selected

    List<Municipality> getMunicipalitys() {
        Municipality.list()
    }


    /**
    Save or update an instance of Municipality.
    @param Map with the values required to save a Municipality.
    <ul>
    	<li>id</li>
    	<li>name</li>
    	<li>district</li>
    </ul>
    **/
    def save(params){
    	Municipality municipality = params.id ? Municipality.get(params.id.toLong()) : new Municipality()
    	
    	Municipality.withTransaction{status->
    		municipality.name = params.name ?: municipality?.name
    		municipality.district = District.load(params.district.id) ?: municipality?.district

    		municipality.validate()

    		if(municipality.hasErrors()){
    			for(error in municipality.errors){
    				log.error error
    			}
    		}else{
    			municipality.save()
    		}
    	}

    	return municipality
    }
}
