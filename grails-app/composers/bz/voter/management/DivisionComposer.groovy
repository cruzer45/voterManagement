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
	def divisionFacade

	ListModel districtModel

	private static NEW_TITLE = "NEW DIVISION"
	private static EDIT_TITLE = "Edit Division"

    def afterCompose = { window ->
	 	if(springSecurityService.isLoggedIn()){
	 		districtModel = new ListModelList(District.list())
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

			def params = [
				id: divisionIdLabel.getValue(),
				name: divisionNameTextbox.getValue()?.trim()?.capitalize(),
				district: districtModel.getSelection().toArray()[0]
			]

			Division divisionInstance = divisionFacade.save(params)

			if(divisionInstance.hasErrors()){
				errorMessages.append{
					for(error in divisionInstance.errors.allErrors){
						log.error error
						label(value: messageSource.getMessage(error,null),class:'errors')
					}
				}
			}else{				
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
		districtListbox.setModel(districtModel)	

		if(divisionInstance){
			divisionInstance = Division.get(divisionInstance.id)			
			divisionFormPanel.setTitle(EDIT_TITLE)
			divisionNameTextbox.setValue("${divisionInstance.name}")
			divisionIdLabel.setValue("${divisionInstance.id}")
			def selection = districtModel.addToSelection(District.get(divisionInstance.districtId))			
			def selectedItem = districtModel.getSelection()
			
			def index = 0
			districtListbox.getListModel().each{
				println it
				
				for(_district in selectedItem){
					if(_district.equalsTo(it)){
						districtListbox.setSelectedIndex(index)
					}
				}				
				index += 1
			}
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
				def divisionInstance = Division.get(_division.id)
				def district = District.get(divisionInstance.districtId)
				row{
					label(value: _division.name)
					label(value: "${district}")
					button(label: 'Edit', onClick: {
						showDivisionForm(divisionInstance)
					})
				}
			}
		}
	 }

}
