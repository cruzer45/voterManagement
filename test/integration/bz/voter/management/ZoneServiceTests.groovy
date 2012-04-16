package bz.voter.management

import static org.junit.Assert.*
import org.junit.*

class ZoneServiceTests {

   def zoneService

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void list_all_zones() {
      List<Zone> zones = zoneService.list()
      assertEquals 2, zones.size()

      assert zones.contains(Zone.findByName('Zone1'))
      assert zones.contains(Zone.findByName('Zone2'))

    }
}
