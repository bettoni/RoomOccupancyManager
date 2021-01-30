package com.rom.app.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class OccupancyOptimizationService {
    public Map<String, Integer[]> optimize(List<Integer> customersBudget, int freeEconomyRoom, int freePremiumRooms) {
        Map<Boolean, List<Integer>> clusteredCustomers = customersBudget.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.partitioningBy(o -> o >= 100));

        List<Integer> premiumCustomers = clusteredCustomers.get(true).stream()
                .limit(freePremiumRooms).collect(toList());

        List<Integer> economyCustomers = clusteredCustomers.get(false);


        if (freePremiumRooms > premiumCustomers.size() && freeEconomyRoom < economyCustomers.size()) {
            int availablePremiumRoomsForUpdate = economyCustomers.size() - freeEconomyRoom;
            List<Integer> upgradedCustomers = economyCustomers.stream().limit(availablePremiumRoomsForUpdate).collect(toList());

            premiumCustomers.addAll(upgradedCustomers);
            economyCustomers.removeAll(upgradedCustomers);
        }
        economyCustomers = economyCustomers.stream().limit(freeEconomyRoom).collect(toList());

        Map<String, Integer[]> result = new HashMap<>();
        result.put("economy", new Integer[]{economyCustomers.size(),
                economyCustomers.stream().mapToInt(Integer::intValue).sum()});
        result.put("premium", new Integer[]{premiumCustomers.size(),
                premiumCustomers.stream().mapToInt(Integer::intValue).sum()});

        return result;
    }
}