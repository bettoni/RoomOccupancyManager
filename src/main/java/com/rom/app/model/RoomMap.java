package com.rom.app.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RoomMap {
    List<Integer> customers;
    int availableEconomyRoom;
    int availablePremiumRooms;
}
