package com.bill_spliter.bill_splitter.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BillSplitterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void splitBill_postEndpointWorks() throws Exception {
        String json = """
            {
              "serviceFeePercent": 10,
              "commonItems": [
                { "name": "Big pizza", "price": 60.00 }
              ],
              "participants": [
                {
                  "name": "Alice",
                  "items": [
                    { "name": "Pasta", "price": 10.00 }
                  ]
                },
                {
                  "name": "Bob",
                  "items": []
                }
              ]
            }
            """;

        mockMvc.perform(post("/api/bill/split")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participants[0].name").value("Alice"))
                .andExpect(jsonPath("$.participants[1].name").value("Bob"));
    }

    @Test
    void demo_getEndpointWorks() throws Exception {
        mockMvc.perform(get("/api/bill/demo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participants").isArray());
    }
}
