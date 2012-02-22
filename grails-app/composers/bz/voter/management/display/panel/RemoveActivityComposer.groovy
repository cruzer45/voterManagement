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

class RemoveActivityComposer extends GrailsComposer {

    def removeActivityWindow

    def cancelButton
    def deleteActivityButton
    
    def voter
    def voterFacade
    def activityId

    EventQueue queue

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            voter = Executions.getCurrent().getArg().voter
            activityId = Executions.getCurrent().getArg().activityId

            queue = EventQueues.lookup('voterActivity', EventQueues.DESKTOP,true)

        }else{
            ComposerHelper.permissionDeniedBox()
        }

    }


    def onClick_cancelButton(){
        removeActivityWindow.detach()
    }


    def onClick_deleteActivityButton(){
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            voterFacade.deleteActivity(activityId)
            queue.publish(new Event("onVoterActivity", null, null))
            removeActivityWindow.detach()
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }
}
