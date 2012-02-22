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

import bz.voter.management.VoterElection

public class OfficeStationVoterRenderer implements RowRenderer{
    
    def centerPanel
    def grid
    def mainDiv

    public void render(Row row, java.lang.Object data){
        def _voterElection = data
        def voterElection = _voterElection.voterElection

        grid = row.getParent()
        mainDiv = grid.getParent().getParent().getParent().getParent()
        centerPanel = mainDiv.getParent()

        def saveButtonLabel = _voterElection.pickupTime ? 'Edit' : 'Save'
        def votedLabel = _voterElection.voted ? 'Yes' : 'No'

        Button saveButton = new Button(saveButtonLabel)
        saveButton.addEventListener("onClick", new EventListener(){
            public void onEvent(Event evt) throws Exception{
			    def HOUR = /10|11|12|[0-9]/
				def MINUTE = /[0-5][0-9]/
				def TIME = /($HOUR):($MINUTE)/
				def valid = (_voterElection.pickupTime =~ TIME).matches()
				if(valid){
                    voterElection.pickupTime = _voterElection.pickupTime
					voterElection.save(flush:true)
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
        pickupTimeTextbox.setWidth("60%")
        pickupTimeTextbox.addEventListener("onChange", new EventListener(){
            public void onEvent(Event evt) throws Exception{
                _voterElection.pickupTime = evt.getTarget().getValue()
            }
        })

        row.getChildren().add(new Label("${_voterElection.registrationNumber}"))
        row.getChildren().add(new Label("${_voterElection.lastName}"))
        row.getChildren().add(new Label("${_voterElection.firstName}"))
        row.getChildren().add(new Label("${_voterElection.houseNumber}"))
        row.getChildren().add(new Label("${_voterElection.street}"))
        row.getChildren().add(new Label("${_voterElection.municipality}"))
        row.getChildren().add(new Label("${_voterElection.pollNumber}"))
        row.getChildren().add(new Label("${_voterElection.affiliation}"))
        row.getChildren().add(new Label("${_voterElection.pledge}"))
        row.getChildren().add(new Label(votedLabel))
        row.getChildren().add(pickupTimeTextbox)
        row.getChildren().add(saveButton)
        row.getChildren().add(detailsButton)
        row.getChildren().add(manageButton)

    }

    public void render(Row row, java.lang.Object data, int pageSize){
            render(row, data)
    }

}

