package bz.voter.management.election

import org.zkoss.zkgrails.*
import org.zkoss.zk.ui.Executions
import org.zkoss.zul.Hlayout

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import h5chart.H5Chart
import h5chart.Pie

import bz.voter.management.VoterElection
import bz.voter.management.Election
import bz.voter.management.Division
import bz.voter.management.PollStation
import bz.voter.management.Voter
import bz.voter.management.Pledge
import bz.voter.management.Affiliation
import bz.voter.management.zk.ComposerHelper
import static bz.voter.management.utils.TwentyFourHourEnum.*

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class SummaryDashboardComposer extends GrailsComposer {

    def summaryDashboardPanel

    def pledgesSummaryBox
    def pledgesSummaryGrid
    def pledgesSummaryRows
    def pledgeVotesGrid
    def pledgeVotesColumns
    def pledgeVotesRows

    def affiliationSummaryGrid
    def affiliationColumns
    def affiliationRows

    def election
    def division
    def affiliations
    def pledges

    def voterElectionService
    def voterService

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_OFFICE_STATION')){
            def electionId = Executions.getCurrent().getArg().electionId

            election = Election.get(electionId.toLong())
            division = Division.findByName(ConfigurationHolder.config.division)
            affiliations = Affiliation.list([sort:'name'])
            pledges = Pledge.list([sort:'name'])

            initPledgesSummaryRows()
            initAffiliationSummaryColumns()
            initAffiliationSummaryRows()
            initPledgeVotesColumns()

        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


    def initPledgesSummaryRows(){
        pledgesSummaryRows.getChildren().clear()

        for(voters in voterElectionService.summaryByPledge(election,division)){
            pledgesSummaryRows.append{
                row{
                    label(value: "${voters.pledge}", class: "voteCountLabels")
                    label(value: "${voters.total_voters}", class: "voteCountLabels")
                }
            } // End of pledgesSummaryRows.append
        }

    }


    def initAffiliationSummaryColumns(){
        affiliationColumns.getChildren().clear()

        affiliationColumns.append{
            for(affiliation in affiliations){
                column{
                    label(value: "${affiliation.name}", class: "gridHeaders")
                }
            }
        }
    }


    def initAffiliationSummaryRows(){
        affiliationRows.getChildren().clear()

        def voters = voterService.summaryByAffiliation(election,division)

        affiliationRows.append{
            row{
                for(affiliation in affiliations){
                    def columnValue = voters.find{voter-> 
                        voter.party == "${affiliation.name}"
                    }

                    label(value: "${columnValue.total}", class: "voteCountLabels")
                }
            }
        }
    }


    def initPledgeVotesColumns(){
        pledgeVotesColumns.getChildren().clear()
        pledgeVotesGrid.setVisible(true)

        def pledgeVotes = voterElectionService.countTotalHourlyVotesByPledge(election,division)

        println "pledgeVotes : ${pledgeVotes}"

        pledgeVotesColumns.append{
            column{
                label(value: "Hour", class: "gridHeaders")
            }
            for(pledge in pledges){
                column{
                    label(value: "${pledge.name}", class:"gridHeaders")
                }
            }
        } //End of pledgeVotesColumns.append


        pledgeVotesRows.append{
            for(votes in pledgeVotes){
                switch(votes.vote_hour){
                    
                    case "16":
                        row{
                            label(value: "${SIXTEEN.value()}", class:"voteCountLabels")
                            for(pledge in pledges){
                                println "votes: ${votes}"
                                def _pledgeVotes = votes.find{v->
                                    println "v: ${v}"
                                    //v.pledge == "${pledge.name}"
                                }
                                //label(value: "${_pledgeVotes.votes_count}", class:"voteCountLabels")
                            }
                        }
               
                } 
            }
        }// End of pledgeVotesRows.append

    }

}
