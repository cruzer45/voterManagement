package bz.voter.management

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.*
import org.zkoss.zk.ui.event.ForwardEvent
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventQueue
import org.zkoss.zk.ui.event.EventQueues
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zul.Messagebox
import org.zkoss.zul.Grid
import org.zkoss.zul.Paging
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Window
import org.zkoss.zul.Paging
import org.zkoss.zul.RowRenderer
import org.zkoss.zul.Label
import org.zkoss.zul.event.PagingEvent

import org.zkoss.zklargelivelist.model.DivisionVotersPagingListModel

import bz.voter.management.zk.ComposerHelper
import bz.voter.management.zk.VoterRenderer
import bz.voter.management.utils.FilterType
import bz.voter.management.utils.VoterListTypeEnum
import bz.voter.management.spring.SpringUtil

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class VoterComposer extends GrailsComposer {

	def addVoterButton
	def showAllVotersBtn
	def searchVoterButton
	def printButton
    def excelExportButton
    def filterVotersBtn

	def voterSearchTextbox

	def center
	
	def votersListRows

	def division

	Grid votersGrid
	Paging voterPaging

   def votersDiv

	DivisionVotersPagingListModel voterModel = null

	def voterService = SpringUtil.getBean('voterService')

   def voterListFacade = SpringUtil.getBean('voterListFacade')
  

    //Identifies the type of voter list we are displaying.
    VoterListTypeEnum voterListType 
    //When filtering by affiliation, we keep track of what affiliation
    //the user is searching for. This helps when the print button is pressed
    //we can always refer to the affiliation filtered so that we print only
    //voters with that affiliation.
    Affiliation _affiliation
    PollStation _pollStation
    FilterType _filterType
    def _filterValue

   private Division division

	private final int _pageSize = 10
	private int _startPageNumber = 0
	private int _totalSize = 0
	private boolean _needsTotalSizeUpdate = true

    private EventQueue queue

    def afterCompose = { window ->
	 	if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_MANAGE_VOTERS')){
            division = voterListFacade.getSystemDivision()
			votersGrid.setRowRenderer(new VoterRenderer())
            //The initial listType:
            voterListType = VoterListTypeEnum.ALL
            showVoters()
            queue = EventQueues.lookup('filterVoters', EventQueues.DESKTOP,true)
            queue.subscribe(new EventListener(){
                public void onEvent(Event evt){
                    def data = evt.getData()
                    _filterType = data.filterType
                    _filterValue =  data.filterValue
                    filter(data.filterType,data.filterValue)
                }
                
            })
		}else{
			execution.sendRedirect('/login')
		}
    }


    def filter(FilterType filterType, Object value){
        switch(filterType){
            case filterType.AFFILIATION:
                _affiliation = (Affiliation)value
                _startPageNumber = 0
                //refreshModel(filterType, _affiliation,_startPageNumber)
                refreshModel(filterType, value,_startPageNumber)
                voterListType = VoterListTypeEnum.AFFILIATION
                voterSearchTextbox.setValue("")
                break

            case filterType.POLL_STATION:
                _startPageNumber = 0
                _pollStation = (PollStation)value
                refreshModel(filterType, value,_startPageNumber)
                voterListType = VoterListTypeEnum.POLLSTATION
                voterSearchTextbox.setValue("")
                break
        }
    }


	 def onClick_searchVoterButton(){
	 	if(division){
            voterListType = VoterListTypeEnum.NAME
	 		def searchText = voterSearchTextbox.getValue()?.trim()
			refreshModel(searchText,0)
		}else{
			Messagebox.show("You must select a division!",
				"Message", Messagebox.OK, Messagebox.EXCLAMATION)
		}

	 }


    def onClick_showAllVotersBtn(){
	 	if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_MANAGE_VOTERS')){
            voterSearchTextbox.setValue("")
            voterListType = VoterListTypeEnum.ALL
            showVoters()
        }else{
			ComposerHelper.permissionDeniedBox()
        }
    }


    def onClick_filterVotersBtn(){
	 	if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_MANAGE_VOTERS')){
            Executions.createComponents("/bz/voter/management/filter/voter.zul",
                votersDiv.getParent().getParent().getParent().getFirstChild().getFirstChild()
                ,[division: division]).doModal()
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


	 def showVoters(){
        def divisionInstance = division
		if(divisionInstance){
		    def divisionVoters = voterService.countByDivision(divisionInstance)
			voterSearchTextbox.setValue("")
			if(divisionVoters>0){
                _startPageNumber = 0
				refreshModel(_startPageNumber)
			}else{
				votersListRows.getChildren().clear()
				Messagebox.show("No voters exist in ${divisionInstance.name}!",
					"Division Message", Messagebox.OK,
					Messagebox.INFORMATION)
			}
		}else{
			Messagebox.show("Error: division does not exist!", "Error",
				Messagebox.OK, Messagebox.ERROR)
		}
	 }


	 def onClick_addVoterButton(){
		final Window win = (Window)Executions.createComponents("voterNewForm.zul",votersDiv ,null) //.doModal()
		win.doModal()
	 }


	 def onClick_printButton(){
	 	if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_PRINT_VOTERS')){
	 	    if(division){
                switch(voterListType){
                    case voterListType.ALL:
	 		            Executions.sendRedirect("/person/export?division=${division.id}&listType=${voterListType}&format=pdf")
                        break
                    case voterListType.NAME:
	 		            Executions.sendRedirect("/person/export?division=${division.id}&listType=${voterListType}&searchString=${voterSearchTextbox.getValue()?.trim()}&format=pdf")
                        break

                    case voterListType.AFFILIATION:
	 		            Executions.sendRedirect("/person/export?division=${division.id}&listType=${voterListType}&affiliation=${_affiliation.id}&format=pdf")
                        break

                    case voterListType.POLLSTATION:
	 		            Executions.sendRedirect("/person/export?division=${division.id}&listType=${voterListType}&pollStation=${_pollStation.id}&format=pdf")
                        break
                        
                }
		    }else{
			    Messagebox.show("You must select a division!", "Message", Messagebox.OK,
				    Messagebox.EXCLAMATION)
		    }
        }else{
            ComposerHelper.permissionDeniedBox()
        }
	 }



    def onClick_excelExportButton(){
	 	if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN, ROLE_PRINT_VOTERS')){
	 	    if(division){
                switch(voterListType){
                    case voterListType.ALL:
	 		            Executions.sendRedirect("/person/export?division=${division.id}&listType=${voterListType}&format=excel")
                        break
                    case voterListType.NAME:
	 		            Executions.sendRedirect("/person/export?division=${division.id}&listType=${voterListType}&searchString=${voterSearchTextbox.getValue()?.trim()}&format=excel")
                        break

                    case voterListType.AFFILIATION:
	 		            Executions.sendRedirect("/person/export?division=${division.id}&listType=${voterListType}&affiliation=${_affiliation.id}&format=excel")
                        break

                    case voterListType.POLLSTATION:
	 		            Executions.sendRedirect("/person/export?division=${division.id}&listType=${voterListType}&pollStation=${_pollStation.id}&format=excel")
                        break
                        
                }
		    }else{
			    Messagebox.show("You must select a division!", "Message", Messagebox.OK,
				    Messagebox.EXCLAMATION)
		    }
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


	public void onPaging_voterPaging(ForwardEvent event){
		final PagingEvent pagingEvent = (PagingEvent) event.getOrigin()
        //if(_startPageNumber != 0){
		    _startPageNumber = pagingEvent.getActivePage()
        //}

        
        switch(voterListType){
            case voterListType.ALL:
			    refreshModel(_startPageNumber)
                break

            case voterListType.NAME:
			    refreshModel(voterSearchTextbox.getValue().trim(),_startPageNumber)
                break

            case voterListType.AFFILIATION:

            case voterListType.POLLSTATION:
                refreshModel(_filterType, _filterValue, _startPageNumber)
                break
                
        }
	}


    private void refreshModel(int activePage){
  		voterPaging.setPageSize(_pageSize)
		voterModel = new DivisionVotersPagingListModel(division,activePage, _pageSize)
		voterPaging.setTotalSize(voterModel.getTotalSize())

		if(_needsTotalSizeUpdate || activePage == 0){
			_totalSize = voterModel.getTotalSize()
			_needsTotalSizeUpdate = false
			voterPaging.setDetailed(true)
		}

		if(_totalSize < _pageSize){
			voterPaging.setDetailed(false)
		}


		votersGrid.setModel(voterModel)
    }


    private void refreshModel(String search, int activePage){
  		voterPaging.setPageSize(_pageSize)
		voterModel = new DivisionVotersPagingListModel(search,division,activePage, _pageSize)
		voterPaging.setTotalSize(voterModel.getTotalSize())

		if(_needsTotalSizeUpdate || activePage == 0){
			_totalSize = voterModel.getTotalSize()
			voterPaging.setDetailed(true)
			_needsTotalSizeUpdate = false
		}

		if(_totalSize < _pageSize){
			voterPaging.setDetailed(false)
		}

		votersGrid.setModel(voterModel)
  }


  private void refreshModel(FilterType filterType, Object filterValue, int activePage){
    voterPaging.setPageSize(_pageSize)
    voterModel = new DivisionVotersPagingListModel(filterType,(Object)filterValue, division,activePage, _pageSize)
    voterPaging.setTotalSize(voterModel.getTotalSize())

    if(_needsTotalSizeUpdate || activePage == 0){
        _totalSize = voterModel.getTotalSize()
        voterPaging.setDetailed(true)
        _needsTotalSizeUpdate = false
    }

    if(_totalSize < _pageSize){
        voterPaging.setDetailed(false)
    }

    votersGrid.setModel(voterModel)
  }

}


