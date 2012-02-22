package bz.voter.management.facade

import org.zkoss.zkgrails.*

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import bz.voter.management.Sex
import bz.voter.management.Relation
import bz.voter.management.District
import bz.voter.management.Municipality
import bz.voter.management.Division
import bz.voter.management.PollStation
import bz.voter.management.Affiliation
import bz.voter.management.Pledge
import bz.voter.management.Election
import bz.voter.management.spring.SpringUtil

class UtilsFacade {

    def sessionFactory
   
    /**
    Lists sex values.
    @return List of sex values.
    **/
    List<Sex> listSex() {
        Sex.list()
    }


    /**
    Lists all districts
    @return List of districts
    **/
    List<District> listDistricts(){
        District.list([sort:'name'])
    }


    /**
    List all municipalities
    @return List of municipalities
    **/
    List<Municipality> listMunicipalities(){
        Municipality.list([sort:'name'])
    }


    /**
    List all municipalities in a specific district.
    @param district is the district whose municipalities we want to list.
    @return List of municipalities
    **/
    List<Municipality> listMunicipalitiesByDistrict(district){
        Municipality.findAllByDistrict(district,[sort:'name'])
    }


    /**
    List all polling stations in the system's division
    @return List of polling stations.
    **/
    List<PollStation> listPollingStations(){
        PollStation.findAllByDivision(getSystemDivision())
    }


    Division getSystemDivision(){
        Division.findByName(ConfigurationHolder.config.division)
    }


    List<Relation> listRelations(){
        Relation.list([sort: 'name'])
    }


    List<Affiliation> listAffiliations(){
        Affiliation.list([sort:'name'])
    }


    List<Pledge> listPledges(){
        Pledge.list([sort:'name'])
    }



    List<Election> listElections(){
        Election.list([sort: 'year', order: 'desc'])
    }
}
