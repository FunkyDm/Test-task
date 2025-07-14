package com.gridnine.testing;

import java.time.LocalDateTime;

/*
Фильтр, исключающий рейсы, у которых вылет раньше текущего времени
*/
public class DepartureBeforeNowFilter implements FlightFilter {
    private final LocalDateTime now;

    public DepartureBeforeNowFilter(LocalDateTime now) {
        this.now = now;
    }

    @Override
    public boolean test(Flight flight) {
        return flight.getSegments().stream()
                .noneMatch(segment -> segment.getDepartureDate()
                        .isBefore(now));
    }

}