package com.gridnine.testing;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.gridnine.testing.FlightBuilder.createFlight;
import static org.junit.jupiter.api.Assertions.*;

public class FlightFilterTest {
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime threeDaysFromNow = now.plusDays(3);

    @Test
    void testDepartureBeforeNowFilter() {
        Flight normalFlight = createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2));
        Flight pastFlight = createFlight(now.minusDays(6), now);

        DepartureBeforeNowFilter filter = new DepartureBeforeNowFilter(now);

        assertTrue(filter.test(normalFlight), "Normal flight should pass filter");
        assertFalse(filter.test(pastFlight),"Past flight should be filtered");
    }

    @Test
    void testArrivalBeforeDepartureFilter() {
        Flight normalFlight = createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2));
        Flight invalidFlight = createFlight(threeDaysFromNow, threeDaysFromNow.minusDays(6));

        ArrivalBeforeDepartureFilter filter = new ArrivalBeforeDepartureFilter();

        assertTrue(filter.test(normalFlight), "Normal flight should pass filter");
        assertFalse(filter.test(invalidFlight), "Flight with arrival before departure should be filtered out");
    }

    @Test
    void testExcessiveGroundTimeFilter() {

    }

    @Test
    void testFlightFilterUtility() {

    }

    @Test
    void testEmptyFlightList() {

    }

    @Test
    void testSingleSegmentFlightForGroundTime() {

    }
}
