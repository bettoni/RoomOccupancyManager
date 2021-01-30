package com.rom.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OccupancyOptimizationServiceShould {

    OccupancyOptimizationService service;

    @BeforeEach
    public void setUp(){
        service = new OccupancyOptimizationService();
    }

    @Test
    public void
    not_book_economy_room_to_premium_customer() {
        List<Integer> customersBudget = Arrays.asList(100, 200, 300, 400, 500);
        int freePremiumRooms = 0;
        int freeEconomyRoom = 3;

        Map<String, Integer[]> optimizedOccupancy = service.optimize(customersBudget, freeEconomyRoom, freePremiumRooms);

        assertEquals(0, optimizedOccupancy.get("economy")[0]);
        assertEquals(0, optimizedOccupancy.get("economy")[1]);
        assertEquals(0, optimizedOccupancy.get("economy")[0]);
        assertEquals(0, optimizedOccupancy.get("economy")[1]);

    }

    @Test
    public void
    occupy_all_rooms_with_highest_paying_customers() {
        List<Integer> customersBudget = Arrays.asList(200, 100, 300, 20, 10, 30);
        int freePremiumRooms = 2;
        int freeEconomyRoom = 2;

        Map<String, Integer[]> optimizedOccupancy = service.optimize(customersBudget, freeEconomyRoom, freePremiumRooms);

        assertEquals(2, optimizedOccupancy.get("economy")[0]);
        assertEquals(50, optimizedOccupancy.get("economy")[1]);
        assertEquals(2, optimizedOccupancy.get("premium")[0]);
        assertEquals(500, optimizedOccupancy.get("premium")[1]);
    }

    @Test
    public void allow_room_upgrade_to_economy_customers() {
        List<Integer> customersBudget = Arrays.asList(200, 20, 10, 30);
        int freePremiumRooms = 2;
        int freeEconomyRoom = 2;

        Map<String, Integer[]> optimizedOccupancy = service.optimize(customersBudget, freeEconomyRoom, freePremiumRooms);

        assertEquals(2, optimizedOccupancy.get("economy")[0]);
        assertEquals(30, optimizedOccupancy.get("economy")[1]);
        assertEquals(2, optimizedOccupancy.get("premium")[0]);
        assertEquals(230, optimizedOccupancy.get("premium")[1]);
    }

    @Test
    public void not_allow_room_upgrade_to_economy_customers_when_there_is_an_available_economy_room() {
        List<Integer> customersBudget = Arrays.asList(200, 20, 10, 30);
        int freePremiumRooms = 2;
        int freeEconomyRoom = 3;

        Map<String, Integer[]> optimizedOccupancy = service.optimize(customersBudget, freeEconomyRoom, freePremiumRooms);

        assertEquals(3, optimizedOccupancy.get("economy")[0]);
        assertEquals(60, optimizedOccupancy.get("economy")[1]);
        assertEquals(1, optimizedOccupancy.get("premium")[0]);
        assertEquals(200, optimizedOccupancy.get("premium")[1]);
    }

}
