package bz.voter.management

import org.zkoss.zkgrails.*
import org.zkoss.zk.ui.*

class VoterElectionComposer extends GrailsComposer {

	def springSecurityService

	def votersListRows

    def afterCompose = { window ->
	 	if(springSecurityService.isLoggedIn()){
			def election = Election.get(Executions.getCurrent().getArg().id)
			showVoters(election)
		}else{
			execution.sendRedirect('/login')
		}
    }


	 def showVoters(Election election){
	 	votersListRows.getChildren().clear()
		for(_voterElection in VoterElection.findAllByElection(election)){
			def voterElectionInstance = _voterElection
			def voted = _voterElection.voted ? true : false
			votersListRows.append{
				row{
					label(value: _voterElection.voter.person.firstName)
					label(value: _voterElection.voter.person.lastName)
					label(value: _voterElection.voter.person.age)
					label(value: _voterElection.voter.registrationNumber)
					label(value: _voterElection.voter.person.sex)
					label(value: _voterElection.voter.person.homePhone)
					label(value: _voterElection.voter.person.cellPhone)
					label(value: _voterElection.voter.pledge)
					label(value: _voterElection.voter.affiliation)
					checkbox(checked: voted, onCheck: {event->
						if(voterElectionInstance.voted){
							voterElectionInstance.voted = false
						}else{
							voterElectionInstance.voted = true
						}
							voterElectionInstance.save(flush:true)
					})
					label(value: _voterElection.pickupTime)
					button(label: 'Activities', onClick:{
					})
					button(label: 'Manage', onClick:{
					})
				}
			}
		}
		
	 }
}
