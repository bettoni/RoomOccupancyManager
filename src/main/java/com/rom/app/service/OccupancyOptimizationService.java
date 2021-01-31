package com.rom.app.service;

import com.rom.app.model.OptimizedRoomOccupancy;
import com.rom.app.model.RoomMap;
import com.rom.app.model.RoomType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rom.app.model.RoomType.ECONOMY;
import static com.rom.app.model.RoomType.PREMIUM;
import static java.util.stream.Collectors.toList;

@Service
public class OccupancyOptimizationService {

    public List<OptimizedRoomOccupancy> optimize(RoomMap roomMap) {
        Map<RoomType, List<Integer>> clusteredCustomers = clusterByBudget(roomMap.getCustomers());

        List<Integer> assignedEconomyCustomers = getCustomersFromCluster(clusteredCustomers, ECONOMY);
        List<Integer> assignedPremiumCustomers = assignAvailableRooms(
                getCustomersFromCluster(clusteredCustomers, PREMIUM),
                roomMap.getAvailablePremiumRooms()
        );

        boolean havePremiumRoomsAvailable = roomMap.getAvailablePremiumRooms() > assignedPremiumCustomers.size();
        boolean haveMoreEconomyCustomersThenRoom = roomMap.getAvailableEconomyRoom() < assignedEconomyCustomers.size();

        if (havePremiumRoomsAvailable && haveMoreEconomyCustomersThenRoom) {
            List<Integer> upgradableCustomers = getUpgradableCustomers(roomMap.getAvailableEconomyRoom(), assignedEconomyCustomers);
            assignedPremiumCustomers.addAll(upgradableCustomers);
            assignedEconomyCustomers.removeAll(upgradableCustomers);
        }

        assignedEconomyCustomers = assignAvailableRooms(assignedEconomyCustomers, roomMap.getAvailableEconomyRoom());

        return List.of(
                new OptimizedRoomOccupancy(PREMIUM, assignedPremiumCustomers),
                new OptimizedRoomOccupancy(ECONOMY, assignedEconomyCustomers)
        );
    }

    private List<Integer> assignAvailableRooms(List<Integer> customer, int availableRooms) {
        return customer.stream()
                .limit(availableRooms).collect(toList());
    }

    private List<Integer> getCustomersFromCluster(Map<RoomType, List<Integer>> clusteredCustomers, RoomType premium) {
        return clusteredCustomers.getOrDefault(premium, new ArrayList<>());
    }

    private List<Integer> getUpgradableCustomers(int freeEconomyRoom, List<Integer> economyCustomers) {
        int premiumRoomsAvailableForUpgrade = economyCustomers.size() - freeEconomyRoom;
        return assignAvailableRooms(economyCustomers, premiumRoomsAvailableForUpgrade);
    }

    private Map<RoomType, List<Integer>> clusterByBudget(List<Integer> customersBudget) {
        return customersBudget.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.groupingBy(
                        budget -> budget >= 100 ? PREMIUM : ECONOMY
                ));
    }
}