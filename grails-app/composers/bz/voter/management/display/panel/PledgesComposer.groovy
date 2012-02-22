package bz.voter.management.display.panel

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventQueue
import org.zkoss.zk.ui.event.EventQueues
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.Executions

import java.text.DateFormat
import java.util.Locale

import bz.voter.management.Voter
import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class PledgesComposer extends GrailsComposer {

    def voter
    def voterFacade
    def utilsFacade

    def addPledgeBtn
    def pledgesRows

    EventQueue queue

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){

            voter = Executions.getCurrent().getArg().voter
            voterFacade.voter = voter

            showPledges()

            queue = EventQueues.lookup("voterPledges", EventQueues.DESKTOP,true)
            queue.subscribe(new EventListener(){
                public void onEvent(Event evt){
                    showPledges()
                }
                
            })

        }else{
            ComposerHelper.permissionDeniedBox()
        }

    }



    def onClick_addPledgeBtn(){
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_MANAGE_VOTERS')){
            Executions.createComponents("/bz/voter/management/display/panel/pledgeForm.zul",
                null, [voter: voter]).doModal()
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


    def showPledges(){
        pledgesRows.getChildren().clear()

        for(_pledge in voterFacade.getPledges()){
            def pledge = _pledge.pledge
            def election = _pledge.election
            def electionDate = election.electionDate ? DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK).format(election.electionDate) : ''
            pledgesRows.append{
                row{
                    label(value: "${election.year}")
                    label(value: "${election.electionType}")
                    label(value: "${electionDate}")
                    label(value: "${pledge.name}")
                    button(label: "Edit", onClick:{
                        Executions.createComponents("/bz/voter/management/display/panel/pledgeForm.zul",
                            null, [voter: voter, election: election]).doModal()
                    })
                }
            }// end of pledgesRows.append
        }
    }
}
