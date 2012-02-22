package bz.voter.management.zk

import org.zkoss.zul.Label
import org.zkoss.zul.Row
import org.zkoss.zul.RowRenderer
import org.zkoss.zul.Window
import org.zkoss.zul.Button
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Checkbox
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.Executions

import org.zkoss.zkgrails.*

import bz.voter.management.VoterElection

public class PollStationVoterRenderer implements RowRenderer{

    public void render(Row row, java.lang.Object data){
        def _voterElection =  data
		def voted = _voterElection.voted ? true : false
		def backgroundColor = voted ? "red" : "white"
        Checkbox votedCheckbox = new Checkbox()
        votedCheckbox.checked = _voterElection.voted ?: false

        Button saveButton = new Button('Save')
        saveButton.addEventListener("onClick", new EventListener(){
            public void onEvent(Event evt) throws Exception{
                def voterElection = _voterElection.voterElection
                voterElection.voted = _voterElection.voted
			    voterElection.save(flush:true)
			    Messagebox.show("Saved Successfuly!", "Voter", 
			        Messagebox.OK, Messagebox.INFORMATION)
			    if(voterElection.voted){
			        evt.getTarget().getParent().setStyle("background-color:red")
			    }else{
			        evt.getTarget().getParent().setStyle("background-color: white")
		        }
            }
        })

        Button detailsButton = new Button('Details')
        detailsButton.addEventListener("onClick", new EventListener(){
            public void onEvent(Event event) throws Exception{
				final Window win = (Window) Executions.createComponents("/bz/voter/management/voterGeneralInformation.zul", 
					null, [voterElection: _voterElection.voterElection])
				win.doModal()
				win.setPosition("top,center")
            }
        })

        votedCheckbox.addEventListener("onCheck", new EventListener(){
            public void onEvent(Event event) throws Exception{
			    if(_voterElection.voted){
		            _voterElection.voterElection.voted = false
                    _voterElection.voted = false
			        _voterElection.voterElection.voteTime = null
			    }else{
			        _voterElection.voterElection.voted = true
                    _voterElection.voted = true
		   	        _voterElection.voterElection.voteTime = new Date()
			    }
            }
        })

        row.setStyle("background-color: ${backgroundColor}")
        row.getChildren().add(new Label("${_voterElection.registrationNumber}"))
        row.getChildren().add(new Label("${_voterElection.registrationDate.format('dd-MMM-yyyy')}"))
        row.getChildren().add(new Label("${_voterElection.lastName}"))
        row.getChildren().add(new Label("${_voterElection.firstName}"))
        row.getChildren().add(new Label("${_voterElection.houseNumber}"))
        row.getChildren().add(new Label("${_voterElection.street}"))
        row.getChildren().add(new Label("${_voterElection.municipality}"))
        row.getChildren().add(new Label("${_voterElection.birthDate.format('dd-MMM-yyyy')}"))
        row.getChildren().add(new Label("${_voterElection.pollNumber}"))
        row.getChildren().add(votedCheckbox)
        row.getChildren().add(saveButton)
        row.getChildren().add(detailsButton)


    }

    public void render(Row row, java.lang.Object data, int pageSize){
        render(row, data)
    }


}
