package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils


class MainComposer extends GrailsComposer {

	def springSecurityService

    def headerLabel

    def grailsApplication

    def afterCompose = { window ->

        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_MANAGE_VOTERS, ROLE_OFFICE_STATION, ROLE_POLL_STATION')){
	 	/*if(!springSecurityService?.isLoggedIn()){ */
            headerLabel.setValue("Voter Management System ${grailsApplication.metadata['app.version']}")
			
		}else{
            
            execution.sendRedirect('/login')
        }
    }
}
