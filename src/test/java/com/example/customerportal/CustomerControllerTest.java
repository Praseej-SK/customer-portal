package com.example.customerportal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHomePage() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("Customer Portal is running"));
    }

    @Test
    void testHealthEndpoint() throws Exception {

        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .json("{\"status\":\"healthy\"}"));
    }

    @Test
    void testCustomerRegistration() throws Exception {

        String customerJson = """
                {
                    "name": "Rahul",
                    "email": "rahul@example.com"
                }
                """;

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rahul"))
                .andExpect(jsonPath("$.email")
                        .value("rahul@example.com"));
    }

    @Test
    void testCustomerNotFound() throws Exception {

        mockMvc.perform(get("/customers/999"))
                .andExpect(status().isNotFound());
    }
}