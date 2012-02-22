package bz.voter.management.filter

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventQueue
import org.zkoss.zk.ui.event.EventQueues
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.Executions

import org.zkoss.zklargelivelist.model.DivisionVotersPagingListModel

import bz.voter.management.zk.ComposerHelper
import bz.voter.management.zk.VoterRenderer

import bz.voter.management.utils.FilterType
import bz.voter.management.Affiliation
import bz.voter.management.Pledge
import bz.voter.management.PollStation

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class VoterComposer extends GrailsComposer {

    def voterFilterWindow
    
    def filterTypeListbox
    def filterValueListbox
    def filterBtn
    def cancelFilterBtn

    def division

    private EventQueue queue

    def afterCompose = { window ->
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_OFFICE_STATION')){
            
            division = Executions.getCurrent().getArg().division
            for(filterType in FilterType.values()){
                if(filterType == FilterType.AFFILIATION || filterType == FilterType.POLL_STATION) {
                    filterTypeListbox.append{
                        listitem(value: filterType){
                            listcell(label: filterType.name)
                        }
                    }
                }
            }

            queue = EventQueues.lookup('filterVoters', EventQueues.DESKTOP,true)
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


    def onSelect_filterTypeListbox(){
        switch(filterTypeListbox.getSelectedItem().getValue()){
            case FilterType.POLL_STATION:
                filterValueListbox.getChildren().clear()
                for(pollStation in PollStation.findAllByDivision(division, [sort: 'pollNumber'])){
                    filterValueListbox.append{
                        listitem(value: pollStation,selected: false){
                            listcell(label: "${pollStation.pollNumber}")
                            listcell(label: pollStation.id)
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
        }
    }


    def onSelect_filterValueListbox(){
        filterBtn.disabled = false
    }

    def onClick_filterBtn(){
        def data = [
            filterType: filterTypeListbox.getSelectedItem()?.getValue(),
            filterValue: filterValueListbox.getSelectedItem()?.getValue()
        ]
        queue.publish(new Event('onFilterVoter',null,data))
        voterFilterWindow.detach()
    }

    
    def onClick_cancelFilterBtn(){
        voterFilterWindow.detach()
    }
}
