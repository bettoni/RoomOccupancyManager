package com.rom.app.model;

import lombok.Value;

import java.util.List;

@Value
public class OptimizedRoomOccupancy {

    RoomType type;
    Integer occupancy;
    Integer result;

    public OptimizedRoomOccupancy(RoomType type, List<Integer> customers) {
        this.type = type;
        this.occupancy = customers.size();
        this.result = customers.stream().mapToInt(Integer::intValue).sum();
    }

}
