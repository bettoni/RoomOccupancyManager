package com.rom.app.service;

import com.rom.app.model.OptimizedRoomOccupancy;
import com.rom.app.model.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OccupancyOptimizationServiceShould {

    private OccupancyOptimizationService service;

    @BeforeEach
    public void setUp() {
        service = new OccupancyOptimizationService();
    }

    @Test
    public void
    not_book_economy_room_to_premium_customer() {
        List<Integer> customersBudget = Arrays.asList(100, 200, 300, 400, 500);
        int freePremiumRooms = 0;
        int freeEconomyRoom = 3;

        List<OptimizedRoomOccupancy> optimizedOccupancy = service.optimize(customersBudget, freeEconomyRoom, freePremiumRooms);
        OptimizedRoomOccupancy economyRoomResult = getRoomResult(optimizedOccupancy, RoomType.ECONOMY);
        OptimizedRoomOccupancy premiumRoomResult = getRoomResult(optimizedOccupancy, RoomType.PREMIUM);

        assertEquals(0, economyRoomResult.getOccupancy());
        assertEquals(0, economyRoomResult.getResult());
        assertEquals(0, premiumRoomResult.getOccupancy());
        assertEquals(0, premiumRoomResult.getResult());
    }

    @Test
    public void
    occupy_all_rooms_with_highest_paying_customers() {
        List<Integer> customersBudget = Arrays.asList(200, 100, 300, 20, 10, 30);
        int freePremiumRooms = 2;
        int freeEconomyRoom = 2;

        List<OptimizedRoomOccupancy> optimizedOccupancy = service.optimize(customersBudget, freeEconomyRoom, freePremiumRooms);
        OptimizedRoomOccupancy economyRoomResult = getRoomResult(optimizedOccupancy, RoomType.ECONOMY);
        OptimizedRoomOccupancy premiumRoomResult = getRoomResult(optimizedOccupancy, RoomType.PREMIUM);

        assertEquals(2, economyRoomResult.getOccupancy());
        assertEquals(50, economyRoomResult.getResult());
        assertEquals(2, premiumRoomResult.getOccupancy());
        assertEquals(500, premiumRoomResult.getResult());
    }

    @Test
    public void
    allow_room_upgrade_to_economy_customers() {
        List<Integer> customersBudget = Arrays.asList(200, 20, 10, 30);
        int freePremiumRooms = 2;
        int freeEconomyRoom = 2;

        List<OptimizedRoomOccupancy> optimizedOccupancy = service.optimize(customersBudget, freeEconomyRoom, freePremiumRooms);
        OptimizedRoomOccupancy economyRoomResult = getRoomResult(optimizedOccupancy, RoomType.ECONOMY);
        OptimizedRoomOccupancy premiumRoomResult = getRoomResult(optimizedOccupancy, RoomType.PREMIUM);


        assertEquals(2, economyRoomResult.getOccupancy());
        assertEquals(30, economyRoomResult.getResult());
        assertEquals(2, premiumRoomResult.getOccupancy());
        assertEquals(230, premiumRoomResult.getResult());
    }

    @Test
    public void
    not_allow_room_upgrade_to_economy_customers_when_there_is_an_available_economy_room() {
        List<Integer> customersBudget = Arrays.asList(20, 10, 30);
        int freePremiumRooms = 2;
        int freeEconomyRoom = 3;

        List<OptimizedRoomOccupancy> optimizedOccupancy = service.optimize(customersBudget, freeEconomyRoom, freePremiumRooms);
        OptimizedRoomOccupancy economyRoomResult = getRoomResult(optimizedOccupancy, RoomType.ECONOMY);
        OptimizedRoomOccupancy premiumRoomResult = getRoomResult(optimizedOccupancy, RoomType.PREMIUM);


        assertEquals(3, economyRoomResult.getOccupancy());
        assertEquals(60, economyRoomResult.getResult());
        assertEquals(0, premiumRoomResult.getOccupancy());
        assertEquals(0, premiumRoomResult.getResult());
    }

    private OptimizedRoomOccupancy getRoomResult(List<OptimizedRoomOccupancy> optimizedOccupancy, RoomType economy) {
        return optimizedOccupancy.stream()
                .filter(optimizedRoomOccupancy -> economy.equals(optimizedRoomOccupancy.getType()))
                .findFirst().get();
    }

}
