package bz.voter.management.zone

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.Executions
import org.zkoss.zhtml.*
import org.zkoss.zul.*

import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen

import bz.voter.management.Zone
import bz.voter.management.Division
import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class CaptainComposer extends GrailsComposer {

   def captainWindow
   def returnButton
   def centerPanel
   def header
   def votersListRows
   Button searchVoterButton
   Button removeCaptainButton
   Toolbarbutton viewDetialsButton
   Textbox voterSearchTextbox
   def currentCaptainDiv

   def firstNameLabel, lastNameLabel, registrationNumberLabel
   def ageLabel, birthDateLabel

   def utilsFacade
   def voterFacade
   def zoneFacade

   Zone zone
   Division division
   def captain

    def afterCompose = { window ->
      if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN,ROLE_OFFICE_STATION")){
         centerPanel = captainWindow.getParent()
         def zoneId = Executions.getCurrent().getArg().id
         zone = Zone.get(zoneId.toLong())
         header.value = "Captain for ${zone.name}"
         division = utilsFacade.getSystemDivision()
         captain = zoneFacade.getCaptain(zoneId)

         if(!captain.id){
           currentCaptainDiv.setVisible(false) 
         }

      }else{
         ComposerHelper.permissionDeniedBox()
      }
    }

    def onClick_returnButton(){
      centerPanel.getChildren().clear()
      Executions.createComponents("/bz/voter/management/zone/listPanel.zul",
         centerPanel, null)
    }

    def onClick_searchVoterButton(){
      if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN,ROLE_OFFICE_STATION")){
         String searchText = voterSearchTextbox.getValue()

         if(searchText.isAllWhitespace() || searchText.trim().size() < 5){
            Messagebox.show("Kindly provide a name with at least 5 characters",
               "Search Warning", Messagebox.OK, Messagebox.EXCLAMATION)
         }else{
            def voters = voterFacade.search(searchText,division)
            showSearchResults(voters)
         }
      }else{
         ComposerHelper.permissionDeniedBox()
      }
    }


    def onClick_removeCaptainButton(){
      if(SpringSecurityUtils.ifAnyGranted("ROLE_ADMIN,ROLE_OFFICE_STATION")){
         zoneFacade.removeCaptain(zone.id)
         currentCaptainDiv.setVisible(false)
      }else{
         ComposerHelper.permissionDeniedBox()
      }
    }

    def showSearchResults(List voters){
      votersListRows.getChildren().clear()
      Toolbarbutton checkButton = new Toolbarbutton('', '/images/checkbox_checked.png')
      checkButton.setTooltip('Add as captain')
      votersListRows.append{
         for(_voter in voters){
            def id = _voter.id
            row{
               label(value:_voter.registrationNumber, sclass:"rowsCell")
               label(value:_voter.lastName)
               label(value:_voter.firstName)
               label(value:_voter.age)
               label(value:_voter.birthDate)
               label(value:_voter.houseNumber)
               label(value:_voter.street)
               label(value:_voter.municipality)
               toolbarbutton(image: '/images/checkbox_checked_small.png', tooltiptext:'Add as captain', onClick:{
                  def captain = zoneFacade.addCaptain(zone.id, id.toLong())
                  registrationNumberLabel.value = captain.registrationNumber
                  firstNameLabel.value = captain.firstName
                  lastNameLabel.value = captain.lastName
                  ageLabel.value = captain.age
                  birthDateLabel.value = captain.birthDate
                  currentCaptainDiv.setVisible(true) 
               })
            }
         }
      }
    }

    def onClick_viewDetialsButton(){
      centerPanel.getChildren().clear()
      Executions.createComponents("/bz/voter/management/zone/captainDetails.zul",
        centerPanel, [captainId: captain.id, zoneId: zone.id])
    }

}
