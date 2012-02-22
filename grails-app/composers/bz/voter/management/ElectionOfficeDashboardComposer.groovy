package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.*

class ElectionOfficeDashboardComposer extends GrailsComposer {
	
	def votesCountBox
	def election

    def afterCompose = { window ->
	 	votesCountBox.getChildren().clear()
		election = Election.get(Executions.getCurrent().getArg().electionId)
	 	Executions.createComponents("pollStationVotesCount.zul", votesCountBox, 
		[id: election.id])
    }
}
