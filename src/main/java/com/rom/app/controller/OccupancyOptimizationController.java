package com.rom.app.controller;

import com.rom.app.model.OptimizedRoomOccupancy;
import com.rom.app.service.OccupancyOptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class OccupancyOptimizationController {

    private final OccupancyOptimizationService optimizationService;

    @PostMapping("/occupancy/optimize")
    public List<OccupancyOptimizationResponse> optimize(@RequestBody OccupancyOptimizationRequest request) {
        List<OptimizedRoomOccupancy> optimizedRoomOccupancies = optimizationService.optimize(request.toRoomMap());
        return optimizedRoomOccupancies.stream()
                .map(OccupancyOptimizationResponse::fromOptimizedRoomOccupancy)
                .collect(Collectors.toList());
    }

}