package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zul.*
import org.zkoss.zk.ui.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen

import bz.voter.management.zk.ComposerHelper

import bz.voter.management.spring.SpringUtil

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils


class ElectionCrudPanelComposer extends GrailsComposer {

	def addElectionButton
	def cancelElectionButton
	def saveElectionButton
	def electionCrudDiv

	def electionFormPanel

	def electionIdLabel

	def yearTextbox

	def electionDatebox

	def electionTypeListbox

	def electionsListRows

	def errorMessages
	def messageSource = SpringUtil.getBean('messageSource')

	def election

	def center

	def springSecurityService = SpringUtil.getBean('springSecurityService')
	def electionService = SpringUtil.getBean('electionService')
	def voterElectionService

	def electionFacade = SpringUtil.getBean('electionFacade')

    def afterCompose = { window ->
	 	if(springSecurityService.isLoggedIn()){
			showElectionsList()
		}else{
			execution.sendRedirect('/login')
		}
    }


	 def onClick_addElectionButton(){
	 	if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_OFFICE_STATION')){	 		
			showElectionFormGrid(null)
		}else{
			ComposerHelper.permissionDeniedBox()
		}
	 }


	 def onClick_cancelElectionButton(){
	 	addElectionButton.setVisible(true)
		hideElectionFormGrid()
	 }


	 def onClick_saveElectionButton(){
	 if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
	 	
		def electionMap = [
			id: electionIdLabel.getValue(),
			year: yearTextbox.getValue()?.trim()?.toInteger(),
			electionDate:  electionDatebox.getValue(),
			electionType: ElectionType.get(electionTypeListbox.getSelectedItem()?.getLastChild()?.getLabel()) ?: null
		]

		def electionInstance = electionService.save(electionMap)

		if(electionInstance.hasErrors()){
			errorMessages.append{
				for(error in electionInstance.errors.allErrors){
					log.error  error
					label(value: messageSource.getMessage(error,null),class:'errors')
				}
			}
		}else{			
			Messagebox.show("Election Saved!", "Election Message", Messagebox.OK, Messagebox.INFORMATION)
			hideElectionFormGrid()
			showElectionsList()
		}

		}else{
			ComposerHelper.permissionDeniedBox()
		}

	 }


	 def showElectionsList(){
		electionsListRows.getChildren().clear()
		if(!addElectionButton.isVisible()){
			addElectionButton.setVisible(true)
		}

		electionsListRows.append{
			for(_election in electionService.list()){
				def electionInstance = _election
				row{
					label(value: _election.year)
					label(value: _election.electionDate?.format('dd-MMM-yyyy'))
					label(value: _election.electionType)
					button(label: 'Edit', onClick:{
						showElectionFormGrid(electionInstance)
					})
					button(label: 'Poll Station', onClick:{
						if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN, ROLE_POLL_STATION")){
							center.getChildren().clear()
							Executions.createComponents("voterElection.zul", center, 
								[id: electionInstance.id])
						}else{
							ComposerHelper.permissionDeniedBox()
						}
					})
					button(label: 'Office Management', onClick:{
						if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN, ROLE_OFFICE_STATION")){

							center.getChildren().clear()
							Executions.createComponents("electionOfficeMain.zul",center,
								[id: electionInstance.id])
						}else{
							ComposerHelper.permissionDeniedBox()
						}

					})
				}
			}
		}
		
	 }


	def showElectionFormGrid(Election electionInstance){
		electionIdLabel.setValue("")
		errorMessages.getChildren().clear()
	 	addElectionButton.setVisible(false)
		electionFormPanel.setVisible(true)
		yearTextbox.setConstraint('no empty')


		electionTypeListbox.getChildren().clear()
		def electionTypes = ElectionType.list([sort:'name'])
		electionTypeListbox.append{
			int cnt = 0
			def selected = false
			for(electionType in electionTypes){
				listitem(value : electionType, selected:selected){
					listcell(label: electionType.name)
					listcell(label: electionType.id)
				}
				cnt++
			}
		}

		if(electionInstance){
			electionFormPanel.setTitle("Edit Election")
			electionDatebox.setValue(electionInstance?.electionDate)
			yearTextbox.setValue("${electionInstance?.year}")
			electionIdLabel.setValue("${electionInstance.id}")
			for(item in electionTypeListbox.getItems()){
				if(item?.getValue()?.id ==  electionInstance.electionType.id){
					electionTypeListbox.setSelectedItem(item)
				}
			}
		}else{
			electionFormPanel.setTitle("Add New Election")
			electionTypeListbox.setSelectedIndex(-1)
		}

	}


	 def hideElectionFormGrid(){
	 	errorMessages.getChildren().clear()
		electionFormPanel.setTitle("")
		yearTextbox.setConstraint('')
		yearTextbox.setValue("")
		electionIdLabel.setVisible(false)
		electionTypeListbox.setSelectedIndex(-1)
		electionFormPanel.setVisible(false)
		addElectionButton.setVisible(true)
	 }
}
