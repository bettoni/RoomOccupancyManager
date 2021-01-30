package com.rom.app.service;

import com.rom.app.model.OptimizedRoomOccupancy;
import com.rom.app.model.RoomType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rom.app.model.RoomType.ECONOMY;
import static com.rom.app.model.RoomType.PREMIUM;
import static java.util.stream.Collectors.toList;

public class OccupancyOptimizationService {
    public List<OptimizedRoomOccupancy> optimize(List<Integer> customersBudget, int freeEconomyRoom, int freePremiumRooms) {
        Map<RoomType, List<Integer>> clusteredCustomers = clusterByBudget(customersBudget);

        List<Integer> assignedPremiumCustomers = clusteredCustomers.getOrDefault(PREMIUM, new ArrayList<>()).stream()
                .limit(freePremiumRooms)
                .collect(toList());

        List<Integer> assignedEconomyCustomers = clusteredCustomers.getOrDefault(ECONOMY, new ArrayList<>());

        boolean havePremiumRoomsAvailable = freePremiumRooms > assignedPremiumCustomers.size();
        boolean haveMoreEconomyCustomersThenRoom = freeEconomyRoom < assignedEconomyCustomers.size();

        if (havePremiumRoomsAvailable && haveMoreEconomyCustomersThenRoom) {
            List<Integer> upgradableCustomers = getUpgradableCustomers(freeEconomyRoom, assignedEconomyCustomers);

            assignedPremiumCustomers.addAll(upgradableCustomers);
            assignedEconomyCustomers.removeAll(upgradableCustomers);
        }

        assignedEconomyCustomers = assignedEconomyCustomers.stream().limit(freeEconomyRoom).collect(toList());

        return List.of(
                new OptimizedRoomOccupancy(PREMIUM, assignedPremiumCustomers),
                new OptimizedRoomOccupancy(ECONOMY, assignedEconomyCustomers)
        );
    }

    private List<Integer> getUpgradableCustomers(int freeEconomyRoom, List<Integer> economyCustomers) {
        int premiumRoomsAvailableForUpgrade = economyCustomers.size() - freeEconomyRoom;

        return economyCustomers.stream()
                .limit(premiumRoomsAvailableForUpgrade)
                .collect(toList());
    }

    private Map<RoomType, List<Integer>> clusterByBudget(List<Integer> customersBudget) {
        return customersBudget.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.groupingBy(
                        budget -> budget >= 100 ? PREMIUM : ECONOMY
                ));
    }
}