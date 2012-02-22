package bz.voter.management.display.panel

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventQueue
import org.zkoss.zk.ui.event.EventQueues
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.ForwardEvent
import org.zkoss.zk.ui.Executions

import java.text.DateFormat
import java.util.Locale

import bz.voter.management.Voter
import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class DependentsComposer extends GrailsComposer {

    def voter
    def dependentsPanel
    def addDependentBtn
    def dependentsRows

    def voterFacade
    def dependentFacade

    EventQueue queue

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            voter = Executions.getCurrent().getArg().voter
            voterFacade.voter = voter
            
            queue = EventQueues.lookup("dependent", EventQueues.DESKTOP,true)
            queue.subscribe(new EventListener(){
                public void onEvent(Event evt){
                    showDependents()
                }
                
            })
            showDependents()
        }else{
            ComposerHelper.permissionDeniedBox()
        }
        
    }


    def onClick_addDependentBtn(){
        Executions.createComponents("/bz/voter/management/display/panel/dependentForm.zul", 
            null, [voter: voter]).doModal()
    }


    def showDependents(){
        dependentsRows.getChildren().clear()

        for(dependent in voterFacade.getDependents(voter)){
            def personId = dependent.id
            dependentsRows.append{
                row{
                    label(value: "${dependent.lastName}")
                    label(value: "${dependent.firstName}")
                    label(value: "${DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.UK).format(dependent.birthDate)}")
                    label(value: "${dependent.age}")
                    label(value: "${dependent.sex}")
                    label(value: "${dependent.relation}")
                    button(label: "Details", onClick:{evt->
                        Executions.createComponents("/bz/voter/management/display/panel/dependentForm.zul", 
                            null, [voter: voter,id: personId]).doModal()
                    })
                    button(label: 'Remove', onClick:{evt->
                        Executions.createComponents("/bz/voter/management/display/panel/removeDependent.zul",
                            null, [voter: voter, id: personId]).doModal()
                    })
                }
            } //End of dependentsRows.append
        }
    }
}
