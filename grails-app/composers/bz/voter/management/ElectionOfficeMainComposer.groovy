package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*
import org.zkoss.zk.ui.*

import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class ElectionOfficeMainComposer extends GrailsComposer {

	def electionOfficeCenter
	def navigationBox

    def afterCompose = { window ->
	 	if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_OFFICE_STATION')){
			electionOfficeCenter.getChildren().clear()
			navigationBox.getChildren().clear()
			def electionId = Executions.getCurrent().getArg().id
			electionOfficeCenter.getChildren().clear()
			Executions.createComponents("electionOfficeVoters.zul",
				electionOfficeCenter,[id: electionId])
			Executions.createComponents("electionOfficeNavigation.zul",
				navigationBox,[id: electionId])
		}else{
			ComposerHelper.permissionDeniedBox()
		}
    }
}
