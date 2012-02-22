package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zul.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen

import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class DivisionComposer extends GrailsComposer {

	def addDivisionButton
	def divisionCancelButton
	def divisionSaveButton
	def districtListbox

	def divisionFormPanel

	def divisionIdLabel

	def divisionsListRows

	def divisionNameTextbox

	def messageSource
	def errorMessages

	def springSecurityService

	private static NEW_TITLE = "NEW DIVISION"
	private static EDIT_TITLE = "Edit Division"

    def afterCompose = { window ->
	 	if(springSecurityService.isLoggedIn()){
	 		showDivisionsList()
		}else{
			execution.sendRedirect('/login')
		}
    }


	def onClick_addDivisionButton(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
			showDivisionForm(null)
		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}

	def onClick_divisionCancelButton(){
		hideDivisionForm()
	}


	def onClick_divisionSaveButton(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){

		def divisionInstance
		if(divisionIdLabel.getValue()){
			divisionInstance = Division.get(divisionIdLabel.getValue())
		}else{
			divisionInstance = new Division()
		}

		divisionInstance.name = divisionNameTextbox.getValue()?.trim()?.capitalize()
		divisionInstance.district = districtListbox.getSelectedItem()?.getValue() ?: District.findByName('Unknown')
		divisionInstance.validate()

		if(divisionInstance.hasErrors()){
			errorMessages.append{
				for(error in divisionInstance.errors.allErrors){
					log.error error
					label(value: messageSource.getMessage(error,null),class:'errors')
				}
			}
		}else{
			divisionInstance.save(flush:true)
			hideDivisionForm()
			Messagebox.show("Division Saved!", "Division Message", Messagebox.OK,
				Messagebox.INFORMATION)
			showDivisionsList()
		}

		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}


	 def showDivisionForm(Division divisionInstance){
	 	divisionIdLabel.setValue("")
		errorMessages.getChildren().clear()
		addDivisionButton.setVisible(false)
		divisionFormPanel.setVisible(true)
		divisionNameTextbox.setConstraint('no empty')
		ListModel divisionModel = new ListModelList(District.list())
		districtListbox.setModel(divisionModel)

		if(divisionInstance){
			divisionInstance = Division.load(divisionInstance.id)
			divisionFormPanel.setTitle(EDIT_TITLE)
			divisionNameTextbox.setValue("${divisionInstance.name}")
			divisionIdLabel.setValue("${divisionInstance.id}")
			divisionModel.addSelection(divisionInstance.district)
		}else{
			divisionFormPanel.setTitle(NEW_TITLE)
		}

	 }


	 def hideDivisionForm(){
	 	errorMessages.getChildren().clear()
		divisionFormPanel.setTitle("")
		divisionNameTextbox.setConstraint("")
		divisionNameTextbox.setValue("")
		addDivisionButton.setVisible(true)
		divisionFormPanel.setVisible(false)
	 }


	 def showDivisionsList(){
	 	divisionsListRows.getChildren().clear()
		if(!addDivisionButton.isVisible()){
			addDivisionButton.setVisible(true)
		}

		divisionsListRows.append{
			for(_division in Division.list([sort:'name'])){
				def divisionInstance = _division
				row{
					label(value: _division.name)
					label(value: "${_division.district}")
					button(label: 'Edit', onClick: {
						showDivisionForm(divisionInstance)
					})
				}
			}
		}
	 }

}
