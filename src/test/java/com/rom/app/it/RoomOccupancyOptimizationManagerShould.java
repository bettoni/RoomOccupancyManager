package com.rom.app.it;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RoomOccupancyOptimizationManagerShould {

    @Autowired
    private MockMvc apiClient;

    @Test
    public void allocate_rooms_per_customer_budget() throws Exception {
        this.apiClient.perform(post("/occupancy/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"customers\": [23, 45, 155, 374, 22, 99, 100, 101, 115, 209]," +
                        "\"free_premium_rooms\":7," +
                        "\"free_economy_rooms\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is("PREMIUM")))
                .andExpect(jsonPath("$[0].occupancy", equalTo(7)))
                .andExpect(jsonPath("$[0].result", equalTo(1153)))
                .andExpect(jsonPath("$[1].type", is("ECONOMY")))
                .andExpect(jsonPath("$[1].occupancy", equalTo(1)))
                .andExpect(jsonPath("$[1].result", equalTo(45)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"customers\": [23, 45, 155, 374, 22, 99, 100, 101, 115, 209],\"free_premium_rooms\":7}",
            "{\"customers\": [23, null, 155],\"free_premium_rooms\":7,\"free_economy_rooms\":7}",
            "{\"customers\": [],\"free_premium_rooms\":7,\"free_economy_rooms\":7}",
            "{\"free_economy_rooms\":7,\"free_premium_rooms\":7}",
            "{\"customers\": [23, 45, 155, 374, 22, 99, 100, 101, 115, 209],\"free_economy_rooms\":7}",})
    public void return_bad_request_with_invalid_parameters(String invalid_json) throws Exception {
        this.apiClient.perform(post("/occupancy/optimize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalid_json))
                .andExpect(status().isBadRequest());
    }
}
