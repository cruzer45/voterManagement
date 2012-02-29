package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*


import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class PledgeCrudPanelComposer extends GrailsComposer {

	def addPledgeButton
	def pledgeSaveButton
	def pledgeCancelButton

	def nameTextbox

	def pledgeIdLabel

	def pledgesListRows

	def pledgeFormPanel

	def codeTextbox

	def errorMessages
	def messageSource

	private static EDIT_TITLE = "Edit Pledge"
	private static NEW_TITLE = "Add New Pledge"


	def springSecurityService
	def pledgeFacade

	def afterCompose ={ window->
		if(springSecurityService.isLoggedIn()){
			showPledgesList()
		}else{
			execution.sendRedirect('/login')
		}
	}


	def onClick_addPledgeButton(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
			showPledgeForm(null)
		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}


	def onClick_pledgeCancelButton(){
		hidePledgeForm()
	}


	def onClick_pledgeSaveButton(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){

		def params = [
			id: pledgeIdLabel.getValue(),
			name: nameTextbox.getValue()?.trim(),
			code: codeTextbox.getValue()?.trim()
		]

		Pledge pledgeInstance = pledgeFacade.save(params)

		if(pledgeInstance.hasErrors()){
			errorMessages.append{
				for(error in pledgeInstance.errors.allErrors){
					log.error error
					label(value: pledgeInstance.retrieveErrors(),class:'errors')
				}
			}
		}else{
			Messagebox.show('Pledge Saved!', 'Pledge Message', Messagebox.OK,
				Messagebox.INFORMATION)
			hidePledgeForm()
			showPledgesList()
		}

		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}
	

	def showPledgesList(){
		pledgesListRows.getChildren().clear()
		if(!addPledgeButton.isVisible()){
			addPledgeButton.setVisible(true)
		}

		pledgesListRows.append{
			for(_pledge in Pledge.list([sort:'name'])){
				def pledgeInstance = _pledge
				row{
					label(value: _pledge.name)
					label(value: _pledge.code)
					button(label: 'Edit', onClick:{
						showPledgeForm(pledgeInstance)
					})
				}
			}
		}
	}



	def showPledgeForm(Pledge pledgeInstance){
		pledgeIdLabel.setValue("")
		errorMessages.getChildren().clear()
		addPledgeButton.setVisible(false)
		pledgeFormPanel.setVisible(true)
		nameTextbox.setConstraint('no empty')
		codeTextbox.setConstraint('no empty')

		if(pledgeInstance){
			pledgeFormPanel.setTitle(EDIT_TITLE)
			nameTextbox.setValue("${pledgeInstance.name}")
			pledgeIdLabel.setValue("${pledgeInstance.id}")
			codeTextbox.setValue("${pledgeInstance.code}")
		}else{
			pledgeFormPanel.setTitle(NEW_TITLE)
		}
	}


	def hidePledgeForm(){
		errorMessages.getChildren().clear()
		pledgeFormPanel.setTitle("")
		nameTextbox.setConstraint('')
		nameTextbox.setValue('')
		codeTextbox.setConstraint('')
		codeTextbox.setValue('')
		addPledgeButton.setVisible(true)
		pledgeFormPanel.setVisible(false)
	}


}
