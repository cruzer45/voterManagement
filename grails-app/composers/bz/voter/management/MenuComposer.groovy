package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.*

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import bz.voter.management.zk.ComposerHelper


class MenuComposer extends GrailsComposer {

	def electionButton
	def userButton
	def voterButton
	def signOutButton
	def systemButton
    def passwordButton
    def zonesButton

	def center
	
    def afterCompose = { window ->
        // initialize components here
    }

	 def onClick_electionButton(){
	 	center.getChildren().clear()
		Executions.createComponents("/bz/voter/management/election.zul", center, null)
	 	
	 }

	 def onClick_voterButton(){
	 	center.getChildren().clear()
		Executions.createComponents("voter.zul",center,null)
		
	 }

	
	def onClick_userButton(){
		center.getChildren().clear()
		Executions.createComponents("user.zul",center,null)
	}


	 def onClick_signOutButton(){
	 	execution.sendRedirect('/logout')
	 }

	 def onClick_systemButton(){
	 	if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN')){
	 		center.getChildren().clear()
			Executions.createComponents("uploadVotersFile.zul",center,null)
		}else{
			ComposerHelper.permissionDeniedBox()
		}
	 }


     def onClick_passwordButton(){
        center.getChildren().clear()
        Executions.createComponents("password.zul", center,null)
     }

     def onClick_zonesButton(){
     	center.getChildren().clear()
     	Executions.createComponents("/bz/voter/management/zone/listPanel.zul", 
     		center, null)
     }
}
