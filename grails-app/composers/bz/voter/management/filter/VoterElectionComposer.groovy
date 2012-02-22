package bz.voter.management.filter

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.*
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventQueue
import org.zkoss.zk.ui.event.EventQueues
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.Executions

import org.zkoss.zklargelivelist.model.DivisionVotersPagingListModel

import bz.voter.management.zk.ComposerHelper
import bz.voter.management.zk.VoterRenderer

import bz.voter.management.utils.FilterType
import bz.voter.management.utils.PickupTimeEnum
import bz.voter.management.Affiliation
import bz.voter.management.Pledge

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class VoterElectionComposer extends GrailsComposer {

    def voterFilterWindow
    
    def filterTypeListbox
    def filterValueListbox
    def votedCheckbox
    def votedCheckboxRow
    def filterBtn
    def cancelFilterBtn


    private EventQueue queue

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_OFFICE_STATION')){
            for(filterType in FilterType.values()){
                if((filterType != FilterType.AFFILIATION) && (filterType != FilterType.POLL_STATION) ) {
                    filterTypeListbox.append{
                        listitem(value: filterType){
                            listcell(label: filterType.name)
                        }
                    }
                }
            }
            queue = EventQueues.lookup('filterElectionVoters', EventQueues.DESKTOP,true)
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


    def onSelect_filterTypeListbox(){
        switch(filterTypeListbox.getSelectedItem().getValue()){
            case FilterType.PLEDGE:
                filterValueListbox.getChildren().clear()
                for(pledge in Pledge.list([sort: 'name'])){
                    filterValueListbox.append{
                        listitem(value: pledge,selected: false){
                            listcell(label: "${pledge.name}")
                            listcell(label: pledge.id)
                        }
                    }
                }
                break

            case FilterType.AFFILIATION:
                filterValueListbox.getChildren().clear()
                for(affiliation in Affiliation.list([sort: 'name'])){
                    filterValueListbox.append{
                        listitem(value: affiliation, selected: false){
                            listcell(label: "${affiliation}")
                            listcell(label: affiliation.id)
                        }
                    }
                }
                break
            
            case FilterType.PICKUP_TIME:
                filterValueListbox.getChildren().clear()
                for(pickupTime in PickupTimeEnum.values()){
                    filterValueListbox.append{
                        listitem(value: pickupTime, selected: false){
                            listcell(label: "${pickupTime.value()}")
                            listcell(label: "${pickupTime}")
                        }
                    }
                }
                break

        }
    }


    def onSelect_filterValueListbox(){
        votedCheckboxRow.visible = true
        filterBtn.disabled = false
        voterFilterWindow.setHeight("30%")
    }


    def onClick_filterBtn(){
        def data = [
            filterType: filterTypeListbox.getSelectedItem()?.getValue(),
            filterValue: filterValueListbox.getSelectedItem()?.getValue(),
            voted:  votedCheckbox.isChecked()
        ]
        queue.publish(new Event('onFilterElectionVoters',null,data))
        voterFilterWindow.detach()
    }


    def onClick_cancelFilterBtn(){
        voterFilterWindow.detach()
    }

}
