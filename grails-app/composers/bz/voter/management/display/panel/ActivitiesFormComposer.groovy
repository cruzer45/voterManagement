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
import bz.voter.management.ActivityType
import bz.voter.management.Activity
import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class ActivitiesFormComposer extends GrailsComposer {


    def voter
    def activity

    def activityFormWindow

    def createActivityBtn
    def saveActivityBtn
    def activityListbox
    def activityDatebox
    def notesTextbox
    def activityTypeRow
    def activityListRow
    def activityTypeTextbox

    def voterFacade
    def activityFacade

    private EventQueue queue

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_MANAGE_VOTERS')){
            voter = Executions.getCurrent().getArg().voter
            voterFacade.voter = voter
            def activityId = Executions.getCurrent().getArg().activityId
            //activity = activityId ? activityFacade.getActivity(activityId) : null
            activity = activityId ? Activity.get(activityId) : null

            
            activityFormWindow.title = activity ? 'Edit Activity' : 'Create New Activity'

            for(_activityType in activityFacade.getActivityTypes()){
                ActivityType activityType = ActivityType.get(_activityType.id)
                activityListbox.append{
                    def selected = activityType.equals(ActivityType.get(activity?.activityTypeId))  
                    listitem(value: activityType, selected: selected){
                        listcell(label: activityType.name)
                        listcell(label: activityType.id)
                    }
                }
            }
            
        queue = EventQueues.lookup('voterActivity', EventQueues.DESKTOP,true)

        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


    def onClick_createActivityBtn(){
        activityListRow.setVisible(false)
        activityTypeRow.setVisible(true)
        activityTypeTextbox.setConstraint('no empty')
    }


    def onClick_saveActivityBtn(){
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_MANAGE_VOTERS')){
            
            def activityType = activityTypeTextbox.getValue() ? activityFacade.saveActivity(activityTypeTextbox.getValue()) : activityListbox.getSelectedItem()?.getValue()
            
            def params = [
                voterId:    voter.id,
                activityId: activity?.id,
                activityType:  activityType,
                notes:      notesTextbox.getValue()?.trim(),
                activityDate:   activityDatebox.getValue()
            ]

            def activityInstance = voterFacade.saveActivity(params)

            if(activityInstance.hasErrors()){
                Messagebox.show(activityInstance.retrieveErrors(),'Activity Message', Messagebox.OK, Messagebox.ERROR)
            }else{
                queue.publish(new Event("onVoterActivity", null ,null))
                Messagebox.show("Activity Saved", "Activity Message", Messagebox.OK, Messagebox.INFORMATION)
                activityFormWindow.detach()
            }
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


}
