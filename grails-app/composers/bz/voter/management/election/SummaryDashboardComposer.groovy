package bz.voter.management.election

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
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
import bz.voter.management.utils.TwentyFourHourEnum

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
    def affiliationVotesGrid
    def affiliationVotesColumns
    def affiliationVotesRows

    def election
    def division
    def affiliations
    def pledges

    def voterElectionService
    def voterService

    def disabledHours = [ONE,TWO,THREE,FOUR,FIVE,TWENTY,TWENTY_ONE,TWENTY_TWO,TWENTY_THREE,TWENTY_FOUR]

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
            initPledgeVotesGrid()
            initAffiliationVotesGrid()

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


    def initPledgeVotesGrid(){
        pledgeVotesColumns.getChildren().clear()
        pledgeVotesRows.getChildren().clear()
        pledgeVotesGrid.setVisible(true)

        def pledgeVotes = voterElectionService.countTotalHourlyVotesByPledge(election,division)

        pledgeVotesColumns.append{
            column(align: "center"){
                label(value: "Hour", class: "gridHeaders")
            }
            for(pledge in pledges){
                column(align:"center"){
                    label(value: "${pledge.name}", class:"gridHeaders")
                }
            }
        } //End of pledgeVotesColumns.append


        def pledgeRows = []

        TwentyFourHourEnum.values().each{hourEnum->
            
            if(!disabledHours.contains(hourEnum)){
            
                def rowRecord = [hour: hourEnum.value(), Yes:0, No:0, Undecided:0]
                def hourMark = hourEnum.value().split('-')[0]
                def pledgeRecord = pledgeVotes.findAll{v->
                    v.vote_hour == hourMark.trim().toLong()
                }
                for(record in pledgeRecord){
                    switch(record.pledge){
                        case 'Yes':
                            rowRecord.Yes = record.votes_count
                            break
                        case 'No':
                            rowRecord.No = record.votes_count
                            break

                        case 'Undecided':
                            rowRecord.Undecided = record.votes_count
                            break
                    }
                }

                pledgeRows.push(rowRecord)
            }
        }

        for(voteRecord in pledgeRows){
            pledgeVotesRows.append{
                row(align:"center"){
                    label(value: "${voteRecord.hour}", class:"voteCountLabels")
                    label(value: "${voteRecord.No}", class:"voteCountLabels")
                    label(value: "${voteRecord.Undecided}", class:"voteCountLabels")
                    label(value: "${voteRecord.Yes}", class:"voteCountLabels")
                }
            }
        }

    }


    def initAffiliationVotesGrid(){
        affiliationVotesColumns.getChildren().clear()

        def affiliationVotes = voterElectionService.countTotalHourlyVotesByAffiliation(election,division)

        affiliationVotesColumns.append{
            column{
                label(value: "Hour", class:"gridHeaders")
            }
            for(affiliation in affiliations){
                column{
                    label(value: "${affiliation}", class:"gridHeaders")
                }
            }
        }//End of affiliationVotesColumns.append

        def affiliationRows = []
        TwentyFourHourEnum.values().each{hourEnum->

            if(!disabledHours.contains(hourEnum)){
                def rowRecord = [hour: hourEnum.value(), PUP:0, UDP:0, UNKNOWN:0]
                def hourMark = hourEnum.value().split('-')[0]
                def affiliationRecord = affiliationVotes.findAll{v->
                    v.vote_hour == hourMark.trim().toLong()
                }
                for(record in affiliationRecord){
                    switch(record.affiliation){

                        case 'PUP':
                            rowRecord.PUP = record.votes_count
                            break

                        case 'UDP':
                            rowRecord.UDP = record.votes_count
                            break

                        case 'UNKNOWN':
                            rowRecord.UNKNOWN = record.votes_count
                            break
                    }
                }

                affiliationRows.push(rowRecord)
            }

        }

        for(voteRecord in affiliationRows){
            affiliationVotesRows.append{
                row{
                    label(value: "${voteRecord.hour}", class:"voteCountLabels")
                    label(value: "${voteRecord.PUP}", class:"voteCountLabels")
                    label(value: "${voteRecord.UDP}", class:"voteCountLabels")
                    label(value: "${voteRecord.UNKNOWN}", class:"voteCountLabels")
                }
            }
        }
    }

}

