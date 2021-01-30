package com.rom.app.service;

import lombok.Value;

import java.util.List;

@Value
public class RoomMap {
    List<Integer> customers;
    int availableEconomyRoom;
    int availablePremiumRooms;
}
