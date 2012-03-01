package bz.voter.management

import org.zkoss.zul.Messagebox
import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class PasswordComposer extends GrailsComposer {

	def springSecurityService

    def user

    def currentPasswordTextbox
    def newPasswordTextbox
    def verifyPasswordTextbox

    def saveBtn

    def securityFacade
    def sessionFactory

    def afterCompose = { window ->
	 	if(!springSecurityService.isLoggedIn()){
			execution.sendRedirect('/login')
		}
    }

    
    def onClick_saveBtn(){
        def currentPassword = currentPasswordTextbox.getValue()
        def newPassword = newPasswordTextbox.getValue()
        def verifyPassword = verifyPasswordTextbox.getValue()

        def errors = securityFacade.validatePasswords(springSecurityService.getCurrentUser().id, currentPassword, newPassword, verifyPassword)

        if(errors.isAllWhitespace()){
            def params = [
                id: springSecurityService.getCurrentUser().id,
                password: verifyPassword
            ]

            securityFacade.changePassword(params)
            Messagebox.show("Password changed!", "Password", Messagebox.OK,
                Messagebox.INFORMATION)
        }else{
            Messagebox.show(errors, "Error changing password!", Messagebox.OK,
                Messagebox.ERROR)
        }

    }


    def validatePasswords(){
        user = SecUser.get(springSecurityService.getCurrentUser().id) //SecUser.get(SpringSecurityUtils.getSubject().getPrinicpal())
        def errorStr = ""
        if(!springSecurityService.encodePassword(currentPasswordTextbox.getValue()).equals(user.password)){
            errorStr += "Current password is incorrect!"
        }

        if(!newPasswordTextbox.getValue().equals(verifyPasswordTextbox.getValue())){
            errorStr += "The new passwords do not match!" 
        }

        return errorStr
    }


}
