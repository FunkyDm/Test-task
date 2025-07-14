package com.gridnine.testing;

import java.util.List;

public class FlightFiltering {
    public static List<Flight> filterFlights(List<Flight> flights, FlightFilter filter) {
        return flights.stream()
                .filter(filter)
                .toList();
    }

    public static void printFlights(String title, List<Flight> flights) {
        System.out.println(title);
        System.out.println("----------------------");
        if (flights.isEmpty()) {
            System.out.println("Полеты не найдены");
        } else {
            flights.forEach(System.out::println);
        }
        System.out.println();
    }

}
