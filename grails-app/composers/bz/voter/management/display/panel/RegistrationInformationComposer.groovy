package bz.voter.management.display.panel

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Messagebox
import org.zkoss.zk.ui.Executions

import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class RegistrationInformationComposer extends GrailsComposer {

    def voterFacade
    def utilsFacade

    def registrationInformationPanel
    def registrationNumberTextbox
    def registrationDatebox
    def pollingStationListbox

    def saveRegsitrationInformationButton

    def voter

    def information // The map that holds the registration number.

    def afterCompose = { window ->
        
        voter = Executions.getCurrent().getArg().voter
        voterFacade.voter = voter

        information = voterFacade.getRegistrationInformation()

        for(pollStation in utilsFacade.listPollingStations()){
            pollingStationListbox.append{
                def selected = information.pollStation.equalsTo(pollStation) 
                listitem(value: pollStation, selected:selected){
                    listcell(label: pollStation.name)
                    listcell(label: pollStation.id)
                }
            }
        }
    }


    def onClick_saveRegistrationInformationButton(){
	    if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            def params = [
                registrationNumber: registrationNumberTextbox.getValue()?.trim(),
                registrationDate: registrationDatebox.getValue(),
                pollStation: pollingStationListbox.getSelectedItem()?.getValue()
            ]

            voter = voterFacade.saveRegistrationInformation(params)
            
            if(voter.hasErrors()){
                Messagebox.show(voter.retrieveErrors(), 'Error Saving Registration Information!',
                    Messagebox.OK, Messagebox.ERROR)
            }else{
                Messagebox.show('Registration Information Saved', 'Registration Information',
                    Messagebox.OK, Messagebox.INFORMATION)
            }
        }else{
            ComposerHelper.permissionDeniedBox()
        }
        
    }
}
