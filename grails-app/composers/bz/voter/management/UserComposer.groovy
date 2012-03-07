package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*

import org.apache.commons.lang.StringUtils

import bz.voter.management.zk.ComposerHelper
import bz.voter.management.spring.SpringUtil

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils


class UserComposer extends GrailsComposer {

	def addUserButton
	def userSaveButton
	def userCancelButton

	def usernameTextbox
	def passwordTextbox

	def adminRoleCheckbox
	def userRoleCheckbox
	def officeStationRoleCheckbox
	def pollStationRoleCheckbox
    def printVotersRoleCheckbox
    def manageVotersRoleCheckbox
	def enabledCheckbox

	def userIdLabel

	def usersListRows

	def userFormPanel

	def errorMessages
	def messageSource

	def securityFacade //= SpringUtil.getBean('securityFacade')

	def springSecurityService = SpringUtil.getBean('springSecurityService')

	private static NEW_TITLE = "New User"
	private static EDIT_TITLE = "Edit User"

    def afterCompose = { window ->
	 	if(springSecurityService.isLoggedIn()){
			showUsersList()
		}else{
			execution.sendRedirect('/login')
		}
    }


	def onClick_addUserButton(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
			showUserForm(null)
		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}


	def onClick_userCancelButton(){
		hideUserForm()
	}


	def onClick_userSaveButton(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){

			def params = [:]			
						
			params.username = usernameTextbox.getValue()?.trim()
			params.password = passwordTextbox.getValue()?.trim()
			params.enabled = enabledCheckbox.isChecked()
			params.accountExpired = false
			params.accountLocked = false
			params.passwordExpired = false
			params.secUserId = StringUtils.isNotBlank(userIdLabel.getValue().trim()) ? userIdLabel.getValue().trim() : null

			def roles = [
				'ROLE_ADMIN' : adminRoleCheckbox.isChecked(),
				'ROLE_USER' : userRoleCheckbox.isChecked(),
				'ROLE_POLL_STATION' : pollStationRoleCheckbox.isChecked(),
				'ROLE_OFFICE_STATION' : officeStationRoleCheckbox.isChecked() ,
				'ROLE_PRINT_VOTERS' : printVotersRoleCheckbox.isChecked() ,
				'ROLE_MANAGE_VOTERS' : manageVotersRoleCheckbox.isChecked()
			]
			
			params.roles = roles

			def userInstance = securityFacade.saveUser(params)

			if(userInstance.hasErrors()){
				Messagebox.show(userInstance.retrieveErrors(),"Error Saving User",
					Messagebox.OK, Messagebox.ERROR)
			}else{
				hideUserForm()
				Messagebox.show('User Saved','User Message', Messagebox.OK,
					Messagebox.INFORMATION)
				showUsersList()
			}			

		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}
	

	def showUsersList(){
		if(SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){

		usersListRows.getChildren().clear()
		if(!addUserButton.isVisible()){
			addUserButton.setVisible(true)
		}

		usersListRows.append{
			for(_user in SecUser.list([sort:'username'])){
				def userInstance = _user
                if(_user.username != 'admin'){
				    row{
					    label(value: _user.username)
					    label(value: _user.enabled)
					    label(value: 
		                    SecUserSecRole.findAllBySecUser(_user).collect {userRole->
		                     	def secRole = SecRole.get(userRole.secRoleId)
		                     	return secRole.authority 
		                     } ) 
					    button(label: 'Edit', onClick:{
						    showUserForm(userInstance)
					    })
				    }
                }
			}
		}// End of usersListRows.append
		}else{
			ComposerHelper.permissionDeniedBox()
		}
	}


	def showUserForm(SecUser userInstance){
	 	userIdLabel.setValue("")
		adminRoleCheckbox.setChecked(false)
		userRoleCheckbox.setChecked(false)
		enabledCheckbox.setChecked(false)
        manageVotersRoleCheckbox.setChecked(false)
		errorMessages.getChildren().clear()
		addUserButton.setVisible(false)
		userFormPanel.setVisible(true)
		usernameTextbox.setConstraint('no empty')
		passwordTextbox.setConstraint('no empty')

		if(userInstance){
			userFormPanel.setTitle(EDIT_TITLE)
			usernameTextbox.setValue("${userInstance.username}")
			userIdLabel.setValue("${userInstance.id}")
			passwordTextbox.setValue(userInstance.password)
			if(userInstance.enabled){
				enabledCheckbox.setChecked(true)
			}
			if(SecUserSecRole.get(userInstance.id, SecRole.findByAuthority('ROLE_ADMIN').id)){
				adminRoleCheckbox.setChecked(true)
			}
			if(SecUserSecRole.get(userInstance.id, SecRole.findByAuthority('ROLE_USER').id)){
				userRoleCheckbox.setChecked(true)
			}
			if(SecUserSecRole.get(userInstance.id, SecRole.findByAuthority('ROLE_POLL_STATION').id)){
				pollStationRoleCheckbox.setChecked(true)
			}
			if(SecUserSecRole.get(userInstance.id, SecRole.findByAuthority('ROLE_OFFICE_STATION').id)){
				officeStationRoleCheckbox.setChecked(true)
			}
			if(SecUserSecRole.get(userInstance.id, SecRole.findByAuthority('ROLE_MANAGE_VOTERS').id)){
				manageVotersRoleCheckbox.setChecked(true)
            }

		}else{
			userFormPanel.setTitle(NEW_TITLE)
			enabledCheckbox.setChecked(true)
		}

	 }


	def hideUserForm(){
	 	errorMessages.getChildren().clear()
		userFormPanel.setTitle('')
		usernameTextbox.setConstraint('')
		usernameTextbox.setValue('')
		passwordTextbox.setConstraint('')
		passwordTextbox.setValue('')
		addUserButton.setVisible(true)
		userFormPanel.setVisible(false)
		adminRoleCheckbox.setChecked(false)
		userRoleCheckbox.setChecked(false)
		enabledCheckbox.setChecked(false)
		manageVotersRoleCheckbox.setChecked(false)

	 }
}
