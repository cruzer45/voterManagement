package bz.voter.management.display.panel

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.Tab
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.ForwardEvent
import org.zkoss.zk.ui.Executions

import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class GeneralInformationTabComposer extends GrailsComposer {
    
    def generalWindowTab
    def basicInformationDiv
    def addressDiv

    def voter

    def afterCompose = { window ->
        voter = Executions.getCurrent().getArg().voter

        Executions.createComponents("/bz/voter/management/display/panel/basicInformationPanel.zul",
            basicInformationDiv, [voter: voter])

    }
}

