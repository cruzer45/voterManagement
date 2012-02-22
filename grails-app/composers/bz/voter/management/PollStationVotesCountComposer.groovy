package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.*

class PollStationVotesCountComposer extends GrailsComposer {

	def election
	
	def votesCountRows

	def countGrids

	def divisionListBox
	def votesBtn

    def afterCompose = { window ->
		  def electionId = Executions.getCurrent().getArg().id
		  election = Election.get(electionId)
		  
		  divisionListBox.append{
		  	for(item in Division.list()){
				listitem(value: item, selected: false){
					listcell(label: item.name)
					listcell(label: item.id)
				}
			}
		  }
    }

	 def onClick_votesBtn(){
	 	countGrids.getChildren().clear()
	 	def division = divisionListBox.getSelectedItem()?.getValue()
		def pollStations = PollStation.findAllByDivision(division)
		def pollVotes = []
		if(division){
			def votesCount = VoterElection.countVotesByPollStationAndAffiliation(election,division)
			for(poll in pollStations){
				votesCount.each{cnt->
					def tmp = []
					if(poll.pollNumber == cnt[1]){
						tmp = [cnt[0],cnt[2]]	
						pollVotes.add(tmp)
					}
				}//End of votesCounte.each

				if(pollVotes.size()>0){
					gridSetUp(pollVotes,poll.pollNumber)
				}
				pollVotes.clear()

			} // End of For

		}//End of IF
	 }

	 private gridSetUp(votesSummary,pollNumber){
	 		def pollStationTotalVotes = 0
			def allVoters = Voter.totalVotersByPollStation(PollStation.findByPollNumber(pollNumber))
	 		countGrids.append{
				grid(width: "90%", style:"padding: 10px") {
					auxhead{
						auxheader(label: "Poll Station: ${pollNumber}")
					}
					columns{
						column(label: 'Affiliation', width: "15%" )
						column(label: '# of Votes' )
						column(label: 'Total in Poll Area')
						column(label: '%')
					}
					rows{
	 				for(voteDetails in votesSummary){
						row{
							label(value: "${voteDetails[1]}")
							label(value: "${voteDetails[0]}")
						}
						pollStationTotalVotes += voteDetails[0]
					}// end of for 
					def percentOfVoters = (pollStationTotalVotes/allVoters) * 100
					row(style: "background-color: khaki"){
						label(value: "Total")
						label(value: "${pollStationTotalVotes}", style: "font-weight: bolder")
						label(value: "${allVoters}")
						label(value: "${percentOfVoters} %")
					}
				}//End of rows 
			}//End of grid
		}// End of countGrids.append
	 }

}
