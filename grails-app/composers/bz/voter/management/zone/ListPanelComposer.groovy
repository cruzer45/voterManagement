package bz.voter.management.zone

import org.zkoss.zk.grails.composer.*
import org.zkoss.zul.*
import org.zkoss.zk.ui.event.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen

import bz.voter.management.Zone
import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class ListPanelComposer extends GrailsComposer {

	def addZoneGrid
	def addZoneButton
	def cancelAddButton
	def saveZoneButton
	def zoneNameTextbox
	def zoneFormHeader
	def zonesListbox

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN, ROLE_OFFICE_STATION")){
        	showZonesList()
       	}else{
       		ComposerHelper.permissionDeniedBox()
        }
    }


    def onClick_addZoneButton(){
    	if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN,ROLE_OFFICE_STATION")){
			addZoneGrid.setVisible(true)  
    		zoneFormHeader.setLabel("Add Zone")
    	}else{
    		ComposerHelper.permissionDeniedBox()
    	}
    	  	
    }

    def onClick_cancelAddButton(){
    	hideZoneForm()
    }

    def onClick_saveZoneButton(){
    	if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN, ROLE_OFFICE_STATION")){
    		def zoneInstance = new Zone()
    		zoneInstance.name = zoneNameTextbox.getValue()?.trim()
    		zoneInstance.save()
    		
    		Messagebox.show("Zone saved!", "Zone Message", Messagebox.OK, 
    			Messagebox.INFORMATION,
    			new EventListener(){
    				public void onEvent(Event e){
    					if(Messagebox.ON_OK.equals(e.getName())){
    						showZonesList()
    						
    					}
    				}    				
    			})
    		hideZoneForm()	    		
    	}else{
    		ComposerHelper.permissionDeniedBox()
    	}

    }

    def onClick_zonesListbox(){
    	println "Selected zone: ${zonesListbox.getSelectedItem()?.getValue()}"
    }


    def showZonesList(){
    	ListModel zonesModel = new ListModelList(Zone.list())
    	zonesListbox.setModel(zonesModel)
    }

    def hideZoneForm(){
    	addZoneGrid.setVisible(false)
    	zoneNameTextbox.setValue("")
    }
}
