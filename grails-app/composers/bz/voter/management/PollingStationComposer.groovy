package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*

import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class PollingStationComposer extends GrailsComposer {

	def addPollStationButton
	def pollStationSaveButton
	def pollStationCancelButton

	def pollNumberTextbox
	def divisionListbox

	def pollStationIdLabel

	def pollStationsListRows

	def pollStationFormPanel

	def errorMessages
	def messageSource


	def springSecurityService


	private static NEW_TITLE = "New Poll Station"
	private static EDIT_TITLE = "Edit Poll Station"


    def afterCompose = { window ->
	 	if(springSecurityService.isLoggedIn()){
	 		showPollStationsList()
		}else{
			execution.sendRedirect('/login')
		}
    }


	def onClick_addPollStationButton(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
			showPollStationForm(null)
		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}


	def onClick_pollStationCancelButton(){
		hidePollStationForm()
	}


	def onClick_pollStationSaveButton(){

		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){

		def pollStationInstance

		pollStationInstance = (pollStationIdLabel.getValue()) ? (PollStation.get(pollStationIdLabel.getValue())) : (new PollStation())

		pollStationInstance.pollNumber = pollNumberTextbox.getValue()?.trim()
		pollStationInstance.division = divisionListbox.getSelectedItem()?.getValue()

		pollStationInstance.validate()

		if(pollStationInstance.hasErrors()){
			errorMessages.append{
				for(error in pollStationInstance.errors.allErrors){
					log.error error
					label(value: messageSource.getMessage(error,null),class:'errors')
				}
			}
		}else{
			pollStationInstance.save(flush:true)
			Messagebox.show("Poll Station Saved!", "Poll Station Message", 
				Messagebox.OK, Messagebox.INFORMATION)
			hidePollStationForm()
			showPollStationsList()
		}

		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}

	 def showPollStationsList(){
	 	pollStationsListRows.getChildren().clear()
		if(!addPollStationButton.isVisible()){
			addPollStationButton.setVisible(true)
		}

		pollStationsListRows.append{
			for(_pollStation in PollStation.list([sort:'pollNumber'])){
				def pollStationInstance = _pollStation
				row{
					label(value: _pollStation.pollNumber)
					label(value: _pollStation.division.name)
					button(label: 'Edit', onClick: {
						showPollStationForm(pollStationInstance)
						
					})
				}
			}
		}
	 }


	 def showPollStationForm(PollStation pollStationInstance){
	 	pollStationIdLabel.setValue("")
		errorMessages.getChildren().clear()
		addPollStationButton.setVisible(false)
		pollStationFormPanel.setVisible(true)
		pollNumberTextbox.setConstraint('no empty')

		if(pollStationInstance){
			pollStationFormPanel.setTitle(EDIT_TITLE)
			pollNumberTextbox.setValue("${pollStationInstance.pollNumber}")
			pollStationIdLabel.setValue("${pollStationInstance.id}")
		}else{
			pollStationInstance = new PollStation()
			pollStationFormPanel.setTitle(NEW_TITLE)
		}

		ComposerHelper.initializeListbox(divisionListbox,pollStationInstance,'division')

	 }

	
	def hidePollStationForm(){
		errorMessages.getChildren().clear()
		pollStationFormPanel.setTitle("")
		pollNumberTextbox.setConstraint("")
		pollNumberTextbox.setValue("")
		divisionListbox.setSelectedIndex(-1)
		addPollStationButton.setVisible(true)
		pollStationFormPanel.setVisible(false)
	}


}
