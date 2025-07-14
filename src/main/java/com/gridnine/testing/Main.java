package com.gridnine.testing;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args){
       List<Flight> flights = FlightBuilder.createFlights();
        LocalDateTime now = LocalDateTime.now();

        flights.forEach(System.out::println);
        System.out.println();

        List<Flight> filtered1 = FlightFiltering.filterFlights(flights, new DepartureBeforeNowFilter(now));
        FlightFiltering.printFlights("Отфильтрованные полеты (исключаем где вылет до текущего момента времени): ", filtered1);

        List<Flight> filtered2 = FlightFiltering.filterFlights(flights,
                new ArrivalBeforeDepartureFilter());
        FlightFiltering.printFlights("Отфильтрованные полеты (исключаем где сегменты с датой прилёта раньше даты вылета):", filtered2);

        List<Flight> filtered3 = FlightFiltering.filterFlights(flights,
                new ExcessiveGroundTimeFilter());
        FlightFiltering.printFlights("Отфильтрованные полеты (исключаем где общее время, проведённое на земле, превышает два часа ):", filtered3);

    }
}
