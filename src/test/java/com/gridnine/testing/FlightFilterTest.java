package com.gridnine.testing;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

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
        assertFalse(filter.test(pastFlight), "Past flight should be filtered");
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
        Flight acceptableFlight = createFlight(
                threeDaysFromNow, threeDaysFromNow.plusHours(1),
                threeDaysFromNow.plusHours(2), threeDaysFromNow.plusHours(3)
        );

        Flight excessiveFlight = createFlight(
                threeDaysFromNow, threeDaysFromNow.plusHours(1),
                threeDaysFromNow.plusHours(4), threeDaysFromNow.plusHours(5)
        );

        ExcessiveGroundTimeFilter filter = new ExcessiveGroundTimeFilter();

        assertTrue(filter.test(acceptableFlight), "Flight with acceptable ground time should pass");
        assertFalse(filter.test(excessiveFlight), "Flight with excessive ground time should be filtered");
    }

    @Test
    void testFlightFilterUtility() {
        List<Flight> flights = List.of(
                createFlight(now.minusDays(1), now),
                createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(1)),
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(1))
        );

        List<Flight> filtered1 = FlightFiltering.filterFlights(flights, new DepartureBeforeNowFilter(now));
        assertEquals(2, filtered1.size(), "Should filter out past flight");

        List<Flight> filtered2 = FlightFiltering.filterFlights(flights, new ArrivalBeforeDepartureFilter());
        assertEquals(2, filtered2.size(), "Should filter out flight with arrival before departure");
    }

    @Test
    void testEmptyFlightList() {
        List<Flight> emptyList = List.of();
        DepartureBeforeNowFilter filter = new DepartureBeforeNowFilter(now);

        List<Flight> result = FlightFiltering.filterFlights(emptyList, filter);
        assertTrue(result.isEmpty(), "Empty list should return empty list");
    }

    @Test
    void testFlightWithZeroSegments() {
        Flight emptyFlight = new Flight(List.of());

        DepartureBeforeNowFilter filter1 = new DepartureBeforeNowFilter(now);
        ArrivalBeforeDepartureFilter filter2 = new ArrivalBeforeDepartureFilter();
        ExcessiveGroundTimeFilter filter3 = new ExcessiveGroundTimeFilter();

        assertTrue(filter1.test(emptyFlight), "Empty flight should pass filter");
        assertTrue(filter2.test(emptyFlight), "Empty flight should pass filter");
        assertTrue(filter3.test(emptyFlight), "Empty flight should pass filter");
    }

    @Test
    void testFlightWithMultipleGroundTimeIntervals() {
        LocalDateTime now = LocalDateTime.now();
        // 1h + 1h ground time = 2h total (should pass)
        Flight acceptableFlight = createFlight(
                now, now.plusHours(1),
                now.plusHours(2), now.plusHours(3),
                now.plusHours(4), now.plusHours(5)
        );

        // 1h + 2h ground time = 3h total (should fail)
        Flight excessiveFlight = createFlight(
                now, now.plusHours(1),
                now.plusHours(2), now.plusHours(3),
                now.plusHours(5), now.plusHours(6)
        );

        ExcessiveGroundTimeFilter filter = new ExcessiveGroundTimeFilter();
        assertTrue(filter.test(acceptableFlight));
        assertFalse(filter.test(excessiveFlight));
    }

}
