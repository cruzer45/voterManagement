package bz.voter.management

import grails.test.*

class ZoneTests extends GroovyTestCase {

	def voterService
	def zone

    protected void setUp() {
        super.setUp()
        zone = new Zone(name: 'zoNe')
        zone.beforeValidate()
        zone.save()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void test_list_voters() {
    	Voter.list().each{
    		it.zone = zone
    		it.save()
    	}
    	assertEquals 'Zone', zone.name

    	assertEquals 13, zone.voters().size()

    }
}
