package com.rom.app.controller;

import com.rom.app.model.OptimizedRoomOccupancy;
import com.rom.app.model.RoomType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OccupancyOptimizationResponse {
    RoomType type;
    int occupancy;
    int result;

    public static OccupancyOptimizationResponse fromOptimizedRoomOccupancy(OptimizedRoomOccupancy optimizedRoomOccupancy) {
        return OccupancyOptimizationResponse.builder()
                .type(optimizedRoomOccupancy.getType())
                .occupancy(optimizedRoomOccupancy.getOccupancy())
                .result(optimizedRoomOccupancy.getResult())
                .build();
    }
}
