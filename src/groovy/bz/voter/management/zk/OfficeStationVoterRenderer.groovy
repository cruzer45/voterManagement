package bz.voter.management.zk

import org.zkoss.zul.Label
import org.zkoss.zul.Row
import org.zkoss.zul.RowRenderer
import org.zkoss.zul.Window
import org.zkoss.zul.Button
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Checkbox
import org.zkoss.zul.Textbox
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.Executions

import org.zkoss.zkgrails.*

import bz.voter.management.*

public class OfficeStationVoterRenderer implements RowRenderer{
    
    def centerPanel
    def grid
    def mainDiv

    public void render(Row row, java.lang.Object data){
        def _voterElection = data
        
        VoterElection voterElection = _voterElection.voterElection
        Election _election = Election.get(voterElection.electionId)

        grid = row.getParent()
        mainDiv = grid.getParent().getParent().getParent().getParent()
        centerPanel = mainDiv.getParent()

        def saveButtonLabel = _voterElection.pickupTime ? 'Edit' : 'Save'
        def votedLabel = _voterElection.voted ? 'Yes' : 'No'

        Button saveButton = new Button(saveButtonLabel)
        saveButton.setDisabled(_election.complete)
        saveButton.addEventListener("onClick", new EventListener(){
            public void onEvent(Event evt) throws Exception{
			   def HOUR = /10|11|12|[0-9]/
				def MINUTE = /[0-5][0-9]/
				def TIME = /($HOUR):($MINUTE)/
				def valid = (_voterElection.pickupTime =~ TIME).matches()
				if(valid){
                    VoterElection.withTransaction{status->
                       voterElection.pickupTime = _voterElection.pickupTime
					   voterElection.save(flush:true)
                    }
					evt.getTarget().setLabel("Edit")
					Messagebox.show("Saved Pickup Time Successfully!", "Pickup Time Message",
						Messagebox.OK, Messagebox.INFORMATION)
				}else{
					Messagebox.show("Wrong Time Format", "Time Format Message", Messagebox.OK,
						Messagebox.ERROR)
				}
            }
        })
        
        Button detailsButton = new  Button("Details")
        detailsButton.addEventListener("onClick", new EventListener(){
            public void onEvent(Event event) throws Exception{
				final Window win = (Window) Executions.createComponents("/bz/voter/management/voterGeneralInformation.zul", 
					null, [voterElection: _voterElection.voterElection])
				win.doModal()
				win.setPosition("top,center")
            }
        })

        Button manageButton = new Button("Manage")
        manageButton.addEventListener("onClick", new EventListener(){
            public void onEvent(Event event) throws Exception{
                centerPanel.getChildren().clear()
                Executions.createComponents("/bz/voter/management/display/panel/voterMainPanel.zul",
                    centerPanel, [voter: _voterElection.voter])
            }
        })

        Textbox pickupTimeTextbox = new Textbox()
        pickupTimeTextbox.value = _voterElection.pickupTime ?: '' 
        pickupTimeTextbox.setWidth("90%")
        pickupTimeTextbox.addEventListener("onChange", new EventListener(){
            public void onEvent(Event evt) throws Exception{
                _voterElection.pickupTime = evt.getTarget().getValue()
            }
        })

        Label registrationNumberLabel = new Label("${_voterElection.registrationNumber}")
        registrationNumberLabel.setSclass("rowsCell")
        Label lastNameLabel = new Label("${_voterElection.lastName}")
        lastNameLabel.setSclass("rowsCell")
        Label firstNameLabel = new Label("${_voterElection.firstName}")
        firstNameLabel.setSclass("rowsCell")
        Label houseNumberLabel = new Label("${_voterElection.houseNumber}")
        houseNumberLabel.setSclass("rowsCell")
        Label streetLabel = new Label("${_voterElection.street}")
        streetLabel.setSclass("rowsCell")
        Label municipalityLabel = new Label("${_voterElection.municipality}")
        municipalityLabel.setSclass("rowsCell")
        Label pollNumberLabel = new Label("${_voterElection.pollNumber}")
        pollNumberLabel.setSclass("rowsCell")
        Label affiliationLabel = new Label("${_voterElection.affiliation}")
        affiliationLabel.setSclass("rowsCell")
        Label pledgeLabel = new Label("${_voterElection.pledge}")
        pledgeLabel.setSclass("rowsCell")
        Label voteLabel = new Label(votedLabel)
        voteLabel.setSclass("rowsCell")

        row.getChildren().add(registrationNumberLabel)
        row.getChildren().add(lastNameLabel)
        row.getChildren().add(firstNameLabel)
        row.getChildren().add(houseNumberLabel)
        row.getChildren().add(streetLabel)
        row.getChildren().add(municipalityLabel)
        row.getChildren().add(pollNumberLabel)
        row.getChildren().add(affiliationLabel)
        row.getChildren().add(pledgeLabel)
        row.getChildren().add(voteLabel)
        row.getChildren().add(pickupTimeTextbox)
        row.getChildren().add(saveButton)
        row.getChildren().add(detailsButton)
        row.getChildren().add(manageButton)

    }

    public void render(Row row, java.lang.Object data, int pageSize){
            render(row, data)
    }

}

