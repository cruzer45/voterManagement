package bz.voter.management.display.panel

import org.zkoss.zk.grails.composer.*

import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Messagebox
import org.zkoss.zk.ui.Executions

import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class ContactInformationTabComposer extends GrailsComposer {

    def voterFacade
    def voter

    def contactInformationPanel
    def homePhoneTextbox
    def cellPhoneTextbox
    def workPhoneTextbox
    def emailTextbox

    def saveContactInformationButton

    def contactInformation

    def afterCompose = { window ->
        
        voter = Executions.getCurrent().getArg().voter
        voterFacade.voter = voter

        contactInformation = voterFacade.getContactInformation()
    }


    def onClick_saveContactInformationButton(){
	    if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            def params = [
                homePhone:      homePhoneTextbox.getValue()?.trim(),
                cellPhone:      cellPhoneTextbox.getValue()?.trim(),
                workPhone:      workPhoneTextbox.getValue()?.trim(),
                emailAddress:   emailTextbox.getValue()?.trim()
            ]

            def personInstance = voterFacade.saveContactInformation(params)

            if(personInstance.hasErrors()){
                Messagebox.show(personInstance.retrieveErrors(), 'Error Saving Contact Information',
                    Messagebox.OK, Messagebox.ERROR)
            }else{
                Messagebox.show('Contact Information Saved!', 'Contact Information Message',
                    Messagebox.OK, Messagebox.INFORMATION)
            }
        }else{
            ComposerHelper.persmissionDeniedBox()
        }
    }


    /**
    Checks if the textboxes were left empty.
    @return true if left empty
    private boolean isEmptyParams(){
        
    }
    **/
}
