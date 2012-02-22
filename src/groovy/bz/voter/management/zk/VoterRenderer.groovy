package bz.voter.management.zk

import org.zkoss.zul.Label
import org.zkoss.zul.Row
import org.zkoss.zul.RowRenderer
import org.zkoss.zul.Window
import org.zkoss.zul.Button
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

		Button manageButton = new Button("Manage")


        manageButton.addEventListener("onClick", new EventListener(){
            public void onEvent(Event event) throws Exception{
                centerPanel.getChildren().clear()
                Executions.createComponents("/bz/voter/management/display/panel/voterMainPanel.zul",
                    centerPanel, [voter: voter])
            }
        })

		row.getChildren().add(new Label("${_voter.registrationDate.format('dd-MMM-yyyy')}"))
		row.getChildren().add(new Label("${_voter.registrationNumber}"))
		row.getChildren().add(new Label("${_voter.lastName}"))
		row.getChildren().add(new Label("${_voter.firstName}"))
		row.getChildren().add(new Label("${_voter.age}"))
		row.getChildren().add(new Label("${_voter.birthDate.format('dd-MMM-yyyy')}"))
		row.getChildren().add(new Label("${_voter.houseNumber}"))
		row.getChildren().add(new Label("${_voter.street}"))
		row.getChildren().add(new Label("${_voter.municipality}"))
		row.getChildren().add(new Label("${_voter.pollNumber}"))
		row.getChildren().add(manageButton)
	}


	public void render(Row row, java.lang.Object data, int pageSize){
    	render(row, data)
    }


}
