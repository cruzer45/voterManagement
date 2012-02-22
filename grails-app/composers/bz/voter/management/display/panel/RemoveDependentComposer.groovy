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

import bz.voter.management.Voter
import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class RemoveDependentComposer extends GrailsComposer {

    def removeDependentWindow

    def cancelButton
    def deleteButton

    def voter
    def personId

    def voterFacade

    EventQueue queue

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){

            voter = Executions.getCurrent().getArg().voter
            voterFacade.voter = voter
            personId = Executions.getCurrent().getArg().id

            queue = EventQueues.lookup('dependent', EventQueues.DESKTOP,true)
        }else{
            ComposerHelper.permissionDeniedBox()
        }

    }

    def onClick_cancelButton(){
        removeDependentWindow.detach()
    }


    def onClick_deleteButton(){
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            def result = voterFacade.deleteDependent(personId)
            if(result){
                queue.publish(new Event("onDependentRemoved", null, null))
            }
            removeDependentWindow.detach()
       }else{
            ComposerHelper.permissionDeniedBox()
       }
    }
}
