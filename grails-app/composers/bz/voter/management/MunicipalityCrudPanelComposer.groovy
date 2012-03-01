package bz.voter.management

import org.zkoss.zk.grails.composer.*

import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*

import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class MunicipalityCrudPanelComposer extends GrailsComposer {

	def addMunicipalityButton
	def municipalitySaveButton
	def municipalityCancelButton

	def nameTextbox
	def districtListbox
	def municipalityIdLabel

	def municipalitiesListRows

	def municipalityFormPanel

	def errorMessages
	def messageSource

	def municipalityFacade

	private static EDIT_TITLE = "Edit Municipality"
	private static NEW_TITLE = "New Municipality"


	def springSecurityService

    def afterCompose = { window ->
	 	if(springSecurityService.isLoggedIn()){
	 		showMunicipalitiesList()
		}else{
			execution.sendRedirect('/login')
		}
    }


	def onClick_addMunicipalityButton(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
			showMunicipalityForm(null)
		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}


	def onClick_municipalityCancelButton(){
		hideMunicipalityForm()
	}


	def onClick_municipalitySaveButton(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
		
			def params = [
				id: municipalityIdLabel.getValue(),
				name: nameTextbox.getValue()?.trim()?.capitalize(),
				district: districtListbox.getSelectedItem()?.getValue()
			]

			def municipalityInstance = municipalityFacade.save(params)

			if(municipalityInstance.hasErrors()){
				errorMessages.append{
					for(error in municipalityInstance.errors.allErrors){
						log.error error
						label(value:messageSource.getMessage(error, null), class:'errors')
					}
				}
			}else{				
				Messagebox.show("Municipality Saved!", 'Municipality Message!', 
					Messagebox.OK, Messagebox.INFORMATION)
				hideMunicipalityForm()
				showMunicipalitiesList()

			}
		}else{
			ComposerHelper.permissionDeniedBox()
		}

	}


	 def showMunicipalitiesList(){
	 	municipalitiesListRows.getChildren().clear()
		if(!addMunicipalityButton.isVisible()){
			addMunicipalityButton.setVisible(true)
		}

		municipalitiesListRows.append{
			for(_municipality in Municipality.list([sort:'name'])){
				def municipalityInstance = _municipality
				row{
					label(value: _municipality.name)
					label(value: _municipality.district.name)
					button(label: 'Edit', onClick: {
						showMunicipalityForm(municipalityInstance)
					})
				}
			}
		} //End of municpalityListRows.append

	 }

	
	def showMunicipalityForm(Municipality municipalityInstance){
		municipalityIdLabel.setValue('')
		errorMessages.getChildren().clear()
		addMunicipalityButton.setVisible(false)
		municipalityFormPanel.setVisible(true)
		nameTextbox.setConstraint('no empty')

		if(municipalityInstance){
			municipalityFormPanel.setTitle(EDIT_TITLE)
			nameTextbox.setValue("${municipalityInstance.name}")
		}else{
			municipalityInstance = new Municipality()
			municipalityFormPanel.setTitle(NEW_TITLE)
		}

		ComposerHelper.initializeListbox(districtListbox, municipalityInstance, 'district')
		
	}


	def hideMunicipalityForm(){
		errorMessages.getChildren().clear()
		municipalityFormPanel.setTitle("")
		nameTextbox.setConstraint('')
		nameTextbox.setValue('')
		districtListbox.setSelectedIndex(-1)
		addMunicipalityButton.setVisible(true)
		municipalityFormPanel.setVisible(false)
	}
	
}
