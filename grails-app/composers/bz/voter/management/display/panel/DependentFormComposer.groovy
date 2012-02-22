package bz.voter.management.display.panel

import org.zkoss.zk.grails.composer.*
import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventQueue
import org.zkoss.zk.ui.event.EventQueues
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.ForwardEvent
import org.zkoss.zk.ui.Executions
import org.zkoss.zul.Messagebox

import bz.voter.management.Voter
import bz.voter.management.Person
import bz.voter.management.zk.ComposerHelper

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class DependentFormComposer extends GrailsComposer {

    def dependentFormWindow

    def saveDependentBtn

    def firstNameTextbox
    def lastNameTextbox
    def middleNameTextbox
    def birthDatebox
    def emailAddressTextbox
    def sexListbox
    def relationListbox

    def voter
    def person
    def dependent

    def voterFacade
    def dependentFacade
    def utilsFacade

    EventQueue queue

    def afterCompose = { window ->
        voter   = Executions.getCurrent().getArg().voter
        person  = Person.load(Executions.getCurrent().getArg().id)

        if(person){
            dependent = dependentFacade.get(voter,person)
        }

        dependentFormWindow.title = dependent ? 'Edit Dependent' : 'Create New Dependent'

        for(sex in utilsFacade.listSex()){
            sexListbox.append{
                def selected = (sex.equals(dependent?.sex)) ? true : false
                listitem(value: sex, selected: selected){
                    listcell(label: sex.name)
                    listcell(label: sex.id)
                }
            }
        }

        for(relation in utilsFacade.listRelations()){
            relationListbox.append{
                def selected = (relation.equals(dependent?.relation)) 
                listitem(value: relation, selected: selected){
                    listcell(label: relation.name)
                    listcell(label: relation.id)
                }
            }// end of relationListbox.append
        }

        queue = EventQueues.lookup('dependent', EventQueues.DESKTOP,true)
        
    }


    def onClick_saveDependentBtn(){
        if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            def errors = validate()
            if(!errors.isAllWhitespace()){
                Messagebox.show(errors, 'Errors', Messagebox.OK,
                    Messagebox.ERROR)
            }else{
                def params = [
                    id:     person?.id,
                    firstName:  firstNameTextbox.getValue()?.trim(),
                    middleName: middleNameTextbox.getValue()?.trim(),
                    lastName:   lastNameTextbox.getValue()?.trim(),
                    birthDate:  birthDatebox.getValue(),
                    emailAddress:   emailAddressTextbox.getValue()?.trim(),
                    sex:        sexListbox.getSelectedItem()?.getValue(),
                    relation:   relationListbox.getSelectedItem()?.getValue()
                ]


                def dependentInstance = voterFacade.saveDependent(params)

                if(dependentInstance.hasErrors()){
                }else{
                    queue.publish(new Event("onDependentAdded", null, null))
                        
                    Messagebox.show('Dependent Saved!', 'Save Message', Messagebox.OK,
                        Messagebox.INFORMATION,
                        new EventListener(){
                            public void onEvent(Event evt){
                                if(evt.getName().equals(Messagebox.ON_OK)){
                                    dependentFormWindow.detach()
                                }
                            }
                        })
                }
            }
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


    def validate(){
        def errors = ''
        if(!sexListbox.getSelectedItem()){
            errors += "Sex: Please select one!\n"
        }

        if(!relationListbox.getSelectedItem()){
            errors += "Relation: Please select one!\n"
        }

        return errors
    }
}
