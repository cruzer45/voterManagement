package bz.voter.management

import grails.test.*

class ZoneTests extends GrailsUnitTestCase {

	def zone 

    protected void setUp() {
        super.setUp()
        mockDomain(Zone, [zone])
        zone = new Zone(name: 'myZone').save()

    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSomething() {
    	assertNotNull zone.id
    }
}
