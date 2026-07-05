package com.finance_dashboard.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// Loads application context for integration testing
@SpringBootTest
// Configures and injects MockMvc instance
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    // Simulates HTTP requests without starting a real server
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void shouldReturnUserWhenDataIsSavedTest() throws Exception {
        mockMvc.perform(get("/users/5", String.class))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(5))
                .andExpect(jsonPath("$.username").value("adam"))
                .andExpect(jsonPath("$.email").value("ajlarson0731@gmail.com"))
                .andExpect(jsonPath("$.hashPassword").value("abc123"))
                .andExpect(jsonPath("$.phoneNumber").value("937-479-0303"));
    }

    @Test
    void shouldNotReturnUserWithUnknownIdTest() throws Exception {
        mockMvc.perform(get("/users/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNewUserTest() throws Exception {
        User newUser = new User(5L, "adam", "ajlarson0731@gmail.com", "abc123", "937-479-0303");

        mockMvc.perform(post("/users", newUser, Void.class)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/users/5"));

        mockMvc.perform(get("/users/5", String.class))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(5))
                .andExpect(jsonPath("$.username").value("adam"))
                .andExpect(jsonPath("$.email").value("ajlarson0731@gmail.com"))
                .andExpect(jsonPath("$.hashPassword").value("abc123"))
                .andExpect(jsonPath("$.phoneNumber").value("937-479-0303"));
    }
}
