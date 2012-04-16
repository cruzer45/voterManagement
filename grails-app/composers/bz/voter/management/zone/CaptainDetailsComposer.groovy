package bz.voter.management.zone

import org.zkoss.zk.grails.composer.*

import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zul.*
import org.zkoss.zk.ui.Executions

import bz.voter.management.*

class CaptainDetailsComposer extends GrailsComposer {

   Label captainNameLabel
   Voter captain
   def returnButton
   Zone zone
   def centerPanel

   def captainDetailsWindow

   def captainDetails

    def afterCompose = { window ->

      def captainId =  Executions.getCurrent().getArg().captainId
      def zoneId = Executions.getCurrent().getArg().zoneId

      centerPanel = captainDetailsWindow?.getParent()

      captain = Voter.get(captainId.toLong())
      zone = Zone.get(zoneId.toLong())

      captainNameLabel.value = "${captain?.firstName} ${captain?.lastName}"

      Executions.createComponents("/bz/voter/management/display/panel/voterMainPanel.zul",
            captainDetails, [voter: captain])

    }


    def onClick_returnButton(){
      centerPanel.getChildren().clear()
      Executions.createComponents("/bz/voter/management/zone/captain.zul",centerPanel, [id: zone.id])
    }

    
}
