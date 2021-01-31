package com.rom.app.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rom.app.model.RoomMap;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OccupancyOptimizationRequest {

    List<@NotNull Integer> customers;
    @NotNull
    Integer freePremiumRooms;
    @NotNull
    Integer freeEconomyRooms;

    public RoomMap toRoomMap() {
        return RoomMap.builder()
                .customers(this.customers)
                .availablePremiumRooms(this.freePremiumRooms)
                .availableEconomyRoom(this.freeEconomyRooms)
                .build();
    }
}
