package bz.voter.management.display.panel

import org.zkoss.zk.grails.composer.*

import org.zkoss.zk.ui.select.annotation.Wire
import org.zkoss.zk.ui.select.annotation.Listen
import org.zkoss.zk.ui.Executions
import org.zkoss.zul.Messagebox
import org.zkoss.zkplus.spring.SpringUtil

import bz.voter.management.zk.ComposerHelper
import static bz.voter.management.utils.AddressEnum.*
//import bz.voter.management.spring.SpringUtil
import bz.voter.management.*

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class AddressPanelComposer extends GrailsComposer {

    def voterFacade
    def utilsFacade
    def voter
    def addressService = SpringUtil.getBean('addressService')

    def registrationAddressHouseNumberTextbox
    def registrationAddressStreetTextbox
    def registrationAddressDistrictListbox
    def registrationAddressMunicipalityListbox
    def registrationAddressPhoneNumber1Textbox
    def registrationAddressPhoneNumber2Textbox
    def registrationAddressPhoneNumber3Textbox

    def workAddressHouseNumberTextbox
    def workAddressStreetTextbox
    def workAddressDistrictListbox
    def workAddressMunicipalityListbox
    def workAddressPhoneNumber1Textbox
    def workAddressPhoneNumber2Textbox
    def workAddressPhoneNumber3Textbox

    def alternateAddressHouseNumberTextbox
    def alternateAddressStreetTextbox
    def alternateAddressDistrictListbox
    def alternateAddressMunicipalityListbox
    def alternateAddressPhoneNumber1Textbox
    def alternateAddressPhoneNumber2Textbox
    def alternateAddressPhoneNumber3Textbox

    def saveRegistrationAddressButton
    def saveWorkAddressButton
    def saveAlternateAddressButton

    def registrationAddress
    def workAddress
    def alternateAddress

    def addressDisplayPanel

    def afterCompose = { window ->
        voter = Executions.getCurrent().getArg().voter

        registrationAddress = voterFacade.getAddress(REGISTRATION)
        workAddress = voterFacade.getAddress(WORK)
        alternateAddress = voterFacade.getAddress(ALTERNATE)

        for(district in utilsFacade.listDistricts()){
            registrationAddressDistrictListbox.append{
                def selected = district.equalsTo(registrationAddress.district) ?: false
                listitem(value: district, selected: selected){
                    listcell(label: district.name )
                    listcell(label: district.id )
                }
            }

            workAddressDistrictListbox.append{
                def selected = district.equalsTo(workAddress?.district) ?: false
                listitem(value: district, selected: selected){
                    listcell(label: district.name)
                    listcell(label: district.id)
                }
            }

            alternateAddressDistrictListbox.append{
                def selected = district.equalsTo(alternateAddress?.district) ?: false
                listitem(value: district, selected: selected){
                    listcell(label: district.name)
                    listcell(label: district.id)
                }
            }
        }

        setupMunicipalityListbox(registrationAddressMunicipalityListbox,
            registrationAddress.district, registrationAddress.municipality)

        setupMunicipalityListbox(workAddressMunicipalityListbox,
            workAddress.district, workAddress.municipality)


        setupMunicipalityListbox(alternateAddressMunicipalityListbox,
            alternateAddress.district, alternateAddress.municipality)

    }


    def onSelect_registrationAddressDistrictListbox(){
        setupMunicipalityListbox(registrationAddressMunicipalityListbox,
            registrationAddressDistrictListbox.getSelectedItem().getValue(), registrationAddress.municipality)
    }


    def onSelect_workAddressDistrictListbox(){
        setupMunicipalityListbox(workAddressMunicipalityListbox,
            workAddressDistrictListbox.getSelectedItem().getValue(), workAddress.municipality)
    }


    def onSelect_alternateAddressDistrictListbox(){
        setupMunicipalityListbox(alternateAddressMunicipalityListbox,
            alternateAddressDistrictListbox.getSelectedItem().getValue(), alternateAddress.municipality)
    }

    def onClick_saveRegistrationAddressButton(){
	    if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            def params = [
                id:                 registrationAddress?.id,
                houseNumber:        registrationAddressHouseNumberTextbox.getValue()?.trim(),
                street:             registrationAddressStreetTextbox.getValue()?.trim(),
                //addressType:        REGISTRATION,
                addressType:        AddressType.findByName("Registration"),
                address:            registrationAddress,
                person:              voter.person,
                municipality:       registrationAddressMunicipalityListbox.getSelectedItem()?.getValue(),
                phoneNumber1:       registrationAddressPhoneNumber1Textbox.getValue()?.trim(),
                phoneNumber2:       registrationAddressPhoneNumber2Textbox.getValue()?.trim(),
                phoneNumber3:       registrationAddressPhoneNumber3Textbox.getValue()?.trim()
            ]
            
            println "params: ${params}"

            println "registrationAddress Municipality Selected: ${registrationAddressMunicipalityListbox.getSelectedItem()?.getValue()}"

            def addressInstance = voterFacade.saveAddress(params)

            if(addressInstance.hasErrors()){
                Messagebox.show(addressInstance.retrieveErrors(), 'Error Saving Address', 
                    Messagebox.OK, Messagebox.ERROR)
            }else{
                Messagebox.show("Address Saved!", "Save Message", Messagebox.OK,
                    Messagebox.INFORMATION)
            }
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    
    }


    def onClick_saveWorkAddressButton(){
	    if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            def params =[
                id:             workAddress?.id,
                address:        workAddress,
                person:         voter.person,
                addressType:    AddressType.findByName('Work'),
                houseNumber:    workAddressHouseNumberTextbox.getValue()?.trim(),
                street:         workAddressStreetTextbox.getValue()?.trim(),
                municipality:   workAddressMunicipalityListbox.getSelectedItem()?.getValue(),
                phoneNumber1:   workAddressPhoneNumber1Textbox.getValue()?.trim(),
                phoneNumber2:   workAddressPhoneNumber2Textbox.getValue()?.trim(),
                phoneNumber3:   workAddressPhoneNumber3Textbox.getValue()?.trim()
            ]

            def addressInstance = voterFacade.saveAddress(params)

            if(addressInstance.hasErrors()){
                Messagebox.show(addressInstance.retrieveErrors(), 'Error Saving Address', 
                    Messagebox.OK, Messagebox.ERROR)
            }else{
                Messagebox.show("Address Saved!", "Save Message", Messagebox.OK,
                    Messagebox.INFORMATION)
            }
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


    def onClick_saveAlternateAddressButton(){
	    if(SpringSecurityUtils.ifAnyGranted('ROLE_ADMIN,ROLE_MANAGE_VOTERS')){
            def params =[
                id:             alternateAddress?.id,
                address:        alternateAddress,
                addressType:    AddressType.findByName('Alternate'),
                person:         voter.person,
                houseNumber:    alternateAddressHouseNumberTextbox.getValue()?.trim(),
                street:         alternateAddressStreetTextbox.getValue()?.trim(),
                municipality:   alternateAddressMunicipalityListbox.getSelectedItem()?.getValue(),
                phoneNumber1:   alternateAddressPhoneNumber1Textbox.getValue()?.trim(),
                phoneNumber2:   alternateAddressPhoneNumber2Textbox.getValue()?.trim(),
                phoneNumber3:   alternateAddressPhoneNumber3Textbox.getValue()?.trim()
            ]

            def addressInstance = voterFacade.saveAddress(params)

            if(addressInstance.hasErrors()){
                Messagebox.show(addressInstance.retrieveErrors(), 'Error Saving Address', 
                    Messagebox.OK, Messagebox.ERROR)
            }else{
                Messagebox.show("Address Saved!", "Save Message", Messagebox.OK,
                    Messagebox.INFORMATION)
            }
        }else{
            ComposerHelper.permissionDeniedBox()
        }
    }


    def setupMunicipalityListbox(listbox,district,addressMunicipality){
        listbox.getChildren().clear()


        if(!district?.equals(addressMunicipality?.district)){
            listbox.append{
                listitem(selected: true){
                    listcell(label: "---")
                    listcell(label: null)
                }
            }
        }
        
        for(municipality in utilsFacade.listMunicipalitiesByDistrict(district)){
            listbox.append{
                def selected = addressMunicipality?.equalsTo(municipality) //?: false
                listitem(value: municipality, selected: selected){
                    listcell(label: municipality.name)
                    listcell(label: municipality.id)
                }
            }
        }
    }

}
