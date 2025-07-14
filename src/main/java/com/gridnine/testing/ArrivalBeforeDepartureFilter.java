package com.gridnine.testing;

/*
Фильтр, исключающий рейсы с прибытием до вылета
*/
public class ArrivalBeforeDepartureFilter implements FlightFilter {
    @Override
    public boolean test(Flight flight) {
        return flight.getSegments().stream()
                .noneMatch(segment -> segment.getArrivalDate()
                        .isBefore(segment.getDepartureDate()));
    }

}
