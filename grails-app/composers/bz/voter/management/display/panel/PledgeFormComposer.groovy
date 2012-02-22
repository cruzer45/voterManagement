package bz.voter.management.display.panel

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Messagebox
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventQueue
import org.zkoss.zk.ui.event.EventQueues
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.Executions

import java.text.DateFormat
import java.util.Locale

import bz.voter.management.Voter
import bz.voter.management.Election
import bz.voter.management.Pledge
import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class PledgeFormComposer extends GrailsComposer {

    def voter
    def election
    def voterFacade
    def utilsFacade

    def pledgeFormWindow
    def savePledgeBtn

    def electionListbox
    def pledgeListbox

    EventQueue queue

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_MANAGE_VOTERS')){

            voter = Executions.getCurrent().getArg().voter
            voterFacade.voter = voter

            election = Executions.getCurrent().getArg().election


            pledgeFormWindow.title = election ? 'Edit Pledge' : 'Add Pledge'

            for(_election in utilsFacade.listElections()){
                _election = Election.get(_election.id)
                electionListbox.append{
                    def selected = _election.equals(election)
                    listitem(value: _election, selected:selected){
                        listcell(label: "${_election}")
                        listcell(label: _election.id)
                    }
                }
            }


            for(_pledge in utilsFacade.listPledges()){
                Pledge pledge = Pledge.get(_pledge.id)
                pledgeListbox.append{
                    def selected = election ? pledge.equals(voterFacade.getPledge(election)) : false
                    listitem(value: pledge, selected: selected){
                        listcell(label: pledge.name)
                        listcell(label: pledge.id)
                    }
                }
            }

        queue = EventQueues.lookup('voterPledges', EventQueues.DESKTOP,true)

        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }



    def onClick_savePledgeBtn(){
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_MANAGE_VOTERS')){
            
            def electionInstance = electionListbox.getSelectedItem()?.getValue()
            def pledge = pledgeListbox.getSelectedItem()?.getValue()

            def voterElection = voterFacade.savePledge(electionInstance,pledge)

            if(voterElection.hasErrors()){
                Messagebox.show(voterElection.retrieveErrors(), "Pledge Message", Messagebox.OK,
                    Messagebox.ERROR)
            }else{
                queue.publish(new Event("onVoterPledge",null, null))
                Messagebox.show("Pledge Saved!", "Pledge Message", Messagebox.OK,
                    Messagebox.INFORMATION)
                pledgeFormWindow.detach()
            }
            
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }

}
