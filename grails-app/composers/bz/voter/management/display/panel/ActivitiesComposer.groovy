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

class ActivitiesComposer extends GrailsComposer {


    def voter
    def voterFacade

    def addActivityBtn
    def activitiesRows

    EventQueue queue

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            
            voter = Executions.getCurrent().getArg().voter
            voterFacade.voter = voter

            showActivities()

            queue = EventQueues.lookup("voterActivity", EventQueues.DESKTOP,true)
            queue.subscribe(new EventListener(){
                public void onEvent(Event evt){
                    showActivities()
                }
                
            })

        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


    def onClick_addActivityBtn(){
        Executions.createComponents("/bz/voter/management/display/panel/activitiesForm.zul",
            null, [voter: voter]).doModal()
    }


    def showActivities(){
        activitiesRows.getChildren().clear()

        for(_activity in voterFacade.getActivities()){
            def activityId = _activity.activityId
            def activityDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK).format(_activity.date)
            activitiesRows.append{
                row{
                    label(value: "${_activity.activityType}")
                    label(value: "${activityDate}")
                    label(value: "${_activity.notes}")
                    button(label: "Details", onClick:{evt->
                        Executions.createComponents("/bz/voter/management/display/panel/activitiesForm.zul",
                        null, [voter: voter, activityId: activityId]).doModal()
                    })
                    button(label: "Remove", onClick:{evt->
                        Executions.createComponents("/bz/voter/management/display/panel/removeActivity.zul",
                           null, [voter: voter, activityId: activityId] ).doModal()
                    })
                }
            } //End of activitiesRows.append
        }
    }
}
