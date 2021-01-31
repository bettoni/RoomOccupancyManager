package com.rom.app.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.rom.app.model.RoomMap;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OccupancyOptimizationRequest {

    @NotEmpty
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
