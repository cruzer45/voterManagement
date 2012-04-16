package bz.voter.management.facade

import bz.voter.management.*

import org.zkoss.zk.grails.*

class ZoneFacade {

    Zone selected

    def getZones() {
        def zones = []
        for(zone in Zone.list([order:'name'])){
         zones.add([id:zone.id, name:zone.name])
        }

        return zones
    }


    def getCaptain(long zoneId){
      Zone zone = Zone.get(zoneId)

      if(zone.captain){

         Voter captain = zone.captain

         [id: captain.id,
         firstName: captain.firstName,
         lastName: captain.lastName,
         registrationNumber: captain.registrationNumber,
         birthDate: captain.birthDate.format('dd-MMM-yyyy'),
         age: captain.age
         ]
      }else{
         [:]
      }

    }

    def addCaptain(long zoneId, long voterId){
      Zone zone = Zone.get(zoneId)
      zone.captain = Voter.get(voterId)
      zone.validate()
      if(zone.hasErrors()){
         for(error in zone.errors){
            log.error error
         }
      }else{
         zone.save()
      }

      def captain = [:]
      if(zone.captain){
         captain = [
            id: zone.captain.id,
            firstName: zone.captain.firstName,
            lastName: zone.captain.lastName,
            registrationNumber: zone.captain.registrationNumber,
            birthDate: zone.captain.birthDate.format('dd-MMM-yyyy'),
            age: zone.captain.age
         ]
      }
      return captain

    }

    def removeCaptain(long zoneId){
      Zone zone = Zone.get(zoneId)
      if(zone instanceof Zone){
         zone.captain = null
         zone.save()
      }
    }
}
