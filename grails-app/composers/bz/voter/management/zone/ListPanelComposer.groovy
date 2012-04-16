package bz.voter.management.zone

import org.zkoss.zk.grails.composer.*
import org.zkoss.zul.*
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen

import bz.voter.management.Zone
import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class ListPanelComposer extends GrailsComposer {

   def zonesListPanel
   def zonesListGrid
   def centerPanel
	def addZoneGrid
	def addZoneButton
	def cancelAddButton
	def saveZoneButton
	def zoneNameTextbox
	def zoneFormHeader
	def zonesListbox
   def zonesRows
   def zoneFacade

   Integer zoneId

   final static String ADD_LABEL = "Add Zone"
   final static String EDIT_LABEL = "Edit Zone"

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN, ROLE_OFFICE_STATION")){
         centerPanel = zonesListGrid?.getParent()
        	showZonesList()
       	}else{
       		ComposerHelper.permissionDeniedBox()
        }
    }


    def onClick_addZoneButton(){
    	if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN,ROLE_OFFICE_STATION")){
         showForm(null)
    	}else{
    		ComposerHelper.permissionDeniedBox()
    	}
    	  	
    }

    def onClick_cancelAddButton(){
    	hideZoneForm()
    }

    def onClick_saveZoneButton(){
    	if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN, ROLE_OFFICE_STATION")){
    		def zoneInstance = Zone.get(zoneId) ?: new Zone()
    		zoneInstance.name = zoneNameTextbox.getValue()?.trim()
    		zoneInstance.save()

         if(zoneInstance.hasErrors()){
            Messagebox.show("${zoneInstance.retrieveErrors()}", "Zone Message", Messagebox.OK,
               Messagebox.ERROR)
         }else{
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
         }
    	}else{
    		ComposerHelper.permissionDeniedBox()
    	}

    }

    def showZonesList(){
      zonesRows.getChildren().clear()
      for(_zone in zoneFacade.getZones()){
            def id = _zone.id
            Toolbarbutton editButton = new Toolbarbutton('','/images/doc_edit.png')
            editButton.setTooltiptext("Edit Zone")
            editButton.addEventListener("onClick", new EventListener(){
               public void onEvent(Event event)throws Exception{
                  showForm([id:_zone.id, name:_zone.name])
               }
            })
            Toolbarbutton captainButton = new Toolbarbutton('','/images/user.png')
            captainButton.setTooltiptext('Zone Captain')
            captainButton.addEventListener("onClick", new EventListener(){
               public void onEvent(Event event) throws Exception{
                  centerPanel.getChildren().clear()
                  Executions.createComponents("/bz/voter/management/zone/captain.zul",centerPanel, [id: id])

               }
            })
            Toolbarbutton votersButton = new Toolbarbutton('','/images/people.png')
            votersButton.setTooltiptext('Voters')
            votersButton.addEventListener("onClick", new EventListener(){
               public void onEvent(Event event) throws Exception{
                  println "Clicked on Voters for ${_zone.name}"
               }
            })
            Row row = new Row()
            row.appendChild(new Label("${_zone?.name}"))
            row.appendChild(editButton)
            row.appendChild(captainButton)
            row.appendChild(votersButton)
            zonesRows.appendChild(row)
      }
    }

    def hideZoneForm(){
    	addZoneGrid.setVisible(false)
      zoneNameTextbox.setConstraint('')
    }

    void showForm(def zone){
      addZoneGrid.setVisible(true)  
      zoneNameTextbox.value = zone?.name 
      zoneId = zone?.id
      if(zone?.id){
         zoneFormHeader.setLabel(EDIT_LABEL)
      }else{
         zoneFormHeader.setLabel(ADD_LABEL)
      }
      zoneNameTextbox.setConstraint('no empty')
    }
}
