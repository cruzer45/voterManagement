package bz.voter.management.zk

import org.zkoss.zul.Label
import org.zkoss.zul.Row
import org.zkoss.zul.RowRenderer
import org.zkoss.zul.Window
import org.zkoss.zul.Toolbarbutton
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.Executions

import org.zkoss.zkgrails.*

import bz.voter.management.Voter

public class VoterRenderer implements RowRenderer{


    def grid
    def panel
    def centerPanel

	public void render(Row row, java.lang.Object data){
		//Voter voter = (Voter) data
        def _voter = data
        Voter voter = _voter.voter

        grid = row.getParent()
        panel = grid.getParent().getParent()
        centerPanel = panel.getParent().getParent()

		Toolbarbutton manageButton = new Toolbarbutton("","/images/round_arrow_right.png")
      manageButton.setTooltiptext('Edit Voter')


      manageButton.addEventListener("onClick", new EventListener(){
          public void onEvent(Event event) throws Exception{
                centerPanel.getChildren().clear()
                Executions.createComponents("/bz/voter/management/display/panel/voterMainPanel.zul",
                    centerPanel, [voter: voter])
            }
        })

        Label registrationDateLabel = new Label("${_voter.registrationDate.format('dd-MMM-yyyy')}")
        registrationDateLabel.setSclass("rowsCell")
        Label registrationNumberLabel = new Label("${_voter.registrationNumber}")
        registrationNumberLabel.setSclass("rowsCell")
        Label lastNameLabel = new Label("${_voter.lastName}")
        lastNameLabel.setSclass('rowsCell')
        Label firstNameLabel = new Label("${_voter.firstName}")
        firstNameLabel.setSclass("rowsCell")
        Label ageLabel = new Label("${_voter.age}")
        ageLabel.setSclass("rowsCell")
        Label birthDateLabel = new Label("${_voter.birthDate.format('dd-MMM-yyyy')}")
        birthDateLabel.setSclass("rowsCell")
        Label houseNumberLabel = new Label("${_voter.houseNumber}")
        houseNumberLabel.setSclass("rowsCell")
        Label streetLabel = new Label("${_voter.street}")
        streetLabel.setSclass("rowsCell")
        Label municipalityLabel = new Label("${_voter.municipality}")
        municipalityLabel.setSclass("rowsCell")
        Label pollNumberLabel = new Label("${_voter.pollNumber}")
        pollNumberLabel.setSclass("rowsCell")

		row.getChildren().add(registrationDateLabel)
		row.getChildren().add(registrationNumberLabel)
		row.getChildren().add(lastNameLabel)
		row.getChildren().add(firstNameLabel)
		row.getChildren().add(ageLabel)
		row.getChildren().add(birthDateLabel)
		row.getChildren().add(houseNumberLabel)
		row.getChildren().add(streetLabel)
		row.getChildren().add(municipalityLabel)
		row.getChildren().add(pollNumberLabel)
		row.getChildren().add(manageButton)
	}


	public void render(Row row, java.lang.Object data, int pageSize){
    	render(row, data)
    }


}
