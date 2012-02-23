package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*

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

			def userInstance
			def userRole = SecRole.findByAuthority('ROLE_USER')
			def adminRole = SecRole.findByAuthority('ROLE_ADMIN')
			def pollStationRole = SecRole.findByAuthority('ROLE_POLL_STATION')
			def officeStationRole = SecRole.findByAuthority('ROLE_OFFICE_STATION')
            def printVotersRole = SecRole.findByAuthority('ROLE_PRINT_VOTERS')
            def manageVotersRole = SecRole.findByAuthority('ROLE_MANAGE_VOTERS')

			userInstance = (userIdLabel.getValue()) ? (SecUser.get(userIdLabel.getValue())) : (new SecUser())

			
			userInstance.username = usernameTextbox.getValue()?.trim()
			userInstance.password = passwordTextbox.getValue()?.trim()
			userInstance.enabled = enabledCheckbox.isChecked()
			userInstance.accountExpired = false
			userInstance.accountLocked = false
			userInstance.passwordExpired = false
			
			userInstance.validate()
			
			if(userInstance.hasErrors()){
				errorMessages.append{
					for(error in userInstance.errors.allErrors){
						log.error error
						label(value: messageSource.getMessage(error,null),class:'errors')
					}
				}
			}else{
				userInstance.save(flush:true)
				if(adminRoleCheckbox.isChecked()){
					SecUserSecRole.create(userInstance,adminRole,true)
				}else{
					if(SecUserSecRole.get(userInstance.id,adminRole.id)){
						SecUserSecRole.remove(userInstance,adminRole,true)
					}
				}

				if(userRoleCheckbox.isChecked()){
					SecUserSecRole.create(userInstance,userRole,true)
				}else{
					if(SecUserSecRole.get(userInstance.id,userRole.id)){
						SecUserSecRole.remove(userInstance,userRole,true)
					}
				}

				if(pollStationRoleCheckbox.isChecked()){
					SecUserSecRole.create(userInstance,pollStationRole,true)
				}else{
					if(SecUserSecRole.get(userInstance.id,pollStationRole.id)){
						SecUserSecRole.remove(userInstance,pollStationRole,true)
					}
				}

				if(officeStationRoleCheckbox.isChecked()){
					SecUserSecRole.create(userInstance,officeStationRole,true)
				}else{
					if(SecUserSecRole.get(userInstance.id,officeStationRole.id)){
						SecUserSecRole.remove(userInstance,officeStationRole,true)
					}
				}

                if(printVotersRoleCheckbox.isChecked()){
                    if(!SecUserSecRole.get(userInstance.id, printVotersRole.id)){
                        SecUserSecRole.create(userInstance, printVotersRole,true)
                    }
                }else{
                    if(SecUserSecRole.get(userInstance.id, printVotersRole.id)){
                        SecUserSecRole.remove(userInstance,printVotersRole,true)
                    }
                }

                if(manageVotersRoleCheckbox.isChecked()){
                    if(!SecUserSecRole.get(userInstance.id, manageVotersRole.id)){
                        SecUserSecRole.create(userInstance, manageVotersRole,true)
                    }
                }else{
                    if(SecUserSecRole.get(userInstance.id, manageVotersRole.id)){
                        SecUserSecRole.remove(userInstance, manageVotersRole,true)
                    }
                }            	

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
		                    SecUserSecRole.findAllBySecUser(_user).collect { it.secRole.authority } ) 
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
