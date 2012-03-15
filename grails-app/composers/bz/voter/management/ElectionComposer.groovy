package bz.voter.management

import org.zkoss.zul.*
import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen

import bz.voter.management.spring.SpringUtil


class ElectionComposer extends GrailsComposer {
	
	def addElectionButton
	def cancelElectionButton
	def saveElectionButton
	def electionPanel

	def electionFormPanel

	def electionIdLabel

	def yearTextbox

	def electionTypeListbox

	def electionsListRows


	def errorMessages
	def messageSource

	def election

	def springSecurityService = SpringUtil.getBean('springSecurityService')

    def afterCompose = { window ->

	 	if(!springSecurityService.isLoggedIn()){
	 		execution.sendRedirect('/login')
		}else{
			println "\nYou are now logged in....\n"
		}
    }


}
