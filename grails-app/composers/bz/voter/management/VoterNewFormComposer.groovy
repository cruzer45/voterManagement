package bz.voter.management

import org.zkoss.zkgrails.*
import org.zkoss.zk.ui.*
import org.zkoss.zul.*

import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

import java.lang.reflect.Field

class VoterNewFormComposer extends GrailsComposer {

    def centerPanel

	def saveButton
	def voterIdLabel
	def voterNewFormPanel
    def voterFormWindow

	def sexListbox
	def pollStationListbox
	def identificationTypeListbox
	def affiliationListbox

	def firstNameTextbox
	def middleNameTextbox
	def lastNameTextbox
	def registrationNumberTextbox

	def birthDateBox
	def registrationDateBox

	def center

	def Person person
	def Voter voter

	def messageSource
	def errorMessages

	def voterService
	def springSecurityService

	def voterFacade
	
   def afterCompose = { window ->
		if(springSecurityService.isLoggedIn()){

          centerPanel = voterFormWindow.getParent().getParent()

		  if(Executions.getCurrent().getArg().id){
				voterIdLabel.setValue(Executions.getCurrent().getArg().id.toString())
				voter = Voter.get(Executions.getCurrent().getArg().id)
				person = voter.person

				firstNameTextbox.setValue(person.firstName)
				middleNameTextbox.setValue(person.middleName)
				lastNameTextbox.setValue(person.lastName)
				registrationNumberTextbox.setValue("${voter.registrationNumber}")
				birthDateBox.setValue(person.birthDate)
				registrationDateBox.setValue(voter.registrationDate)

		  }else{
		  		voter = new Voter()
		  		person = new Person()
			}
			

		  		ComposerHelper.initializeListbox(sexListbox, person, 'sex')
		  		ComposerHelper.initializeListbox(identificationTypeListbox, voter, 'identificationType')
		  		ComposerHelper.initializeListbox(pollStationListbox,voter,'pollStation')
		  		ComposerHelper.initializeListbox(affiliationListbox,voter,'affiliation')
	 	}else{
			execution.sendRedirect('/login')
		}

	 }


	 /*def onClick_cancelButton(){
	 	/*center.getChildren().clear()
		Executions.createComponents("voter.zul", center,null)
	 }*/


	 def onClick_saveButton(){
	 	if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_OFFICE_STATION')){

	 	errorMessages.getChildren().clear()
	 	def personInstance
		def voterInstance

		voterInstance = (voterIdLabel.getValue()) ?  Voter.get(voterIdLabel.getValue()) : (new Voter())
		personInstance = (voterIdLabel.getValue()) ? voterInstance.person : (new Person())


		def params = [
			firstName : firstNameTextbox.getValue()?.trim()?.capitalize(),
			middleName : middleNameTextbox.getValue()?.trim()?.capitalize(),
			lastName : lastNameTextbox.getValue()?.trim()?.capitalize(),
			registrationNumber: registrationNumberTextbox.getValue()?.trim(),
			birthDate : birthDateBox.getValue(),
			registrationDate : registrationDateBox.getValue(),
			identificationType : identificationTypeListbox.getSelectedItem()?.getValue(),
			sex : sexListbox.getSelectedItem()?.getValue(),
			pollStation : pollStationListbox.getSelectedItem()?.getValue(),
			affiliation: affiliationListbox.getSelectedItem()?.getValue()
		]

		if(voterInstance.id){
			params.voter = voterInstance
			//voterInstance = voterService.save(params)
		}

		voterInstance = voterFacade.save(params)

		if(voterInstance.retrieveErrors()){
			errorMessages.append{
				label(value: voterInstance.retrieveErrors(),class:'errors')
			}
		}else{
			Messagebox.show("Voter Saved!", "Voter Message!", Messagebox.OK,
				Messagebox.INFORMATION)
            centerPanel.getChildren().clear()
            Executions.createComponents("/bz/voter/management/display/panel/voterMainPanel.zul",
                    centerPanel, [voter: voterInstance])
            voterFormWindow.detach()
		}

		}else{
			ComposerHelper.permissionDeniedBox()
		}

	 }

}

