package com.finance_dashboard.accounts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// Loads application context for integration testing
@SpringBootTest
// Configures and injects MockMvc instance
@AutoConfigureMockMvc
// Each test runs in own isolated transaction
@Transactional
public class AccountControllerTest {

    @Autowired
    // Simulates HTTP requests without starting a real server
    private MockMvc mockMvc;

    @Autowired
    // Able to serialize or deserialize JSON data
    private JsonMapper jsonMapper;

    @Test
    void shouldReturnAccountWhenDataIsSavedTest() throws Exception {
        mockMvc.perform(get("/accounts/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(5))
                .andExpect(jsonPath("$.username").value("adam"))
                .andExpect(jsonPath("$.email").value("ajlarson0731@gmail.com"))
                .andExpect(jsonPath("$.hashPassword").value("abc123"))
                .andExpect(jsonPath("$.phoneNumber").value("937-479-0303"));
    }

    @Test
    void shouldNotReturnAccountWithUnknownIdTest() throws Exception {
        mockMvc.perform(get("/accounts/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewAccountTest() throws Exception {
        Account newAccount = new Account(5L, "adam", "ajlarson0731@gmail.com", "abc123", "937-479-0303");

        mockMvc.perform(post("/accounts")
                // Tells system the format of data being transmitted is JSON
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/accounts/5"));

        mockMvc.perform(get("/accounts/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(5))
                .andExpect(jsonPath("$.username").value("adam"))
                .andExpect(jsonPath("$.email").value("ajlarson0731@gmail.com"))
                .andExpect(jsonPath("$.hashPassword").value("abc123"))
                .andExpect(jsonPath("$.phoneNumber").value("937-479-0303"));
    }

    @Test
    void shouleUpdateExistingAccountTest() throws Exception {
        Account accountUpdate = new Account(5L, "adam_updated", "newemail@gmail.com", "new_password", "123-456-7890");

        mockMvc.perform(put("/accounts/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(accountUpdate)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/accounts/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountId").value(5))
                .andExpect(jsonPath("$.username").value("adam_updated"))
                .andExpect(jsonPath("$.email").value("newemail@gmail.com"))
                .andExpect(jsonPath("$.hashPassword").value("new_password"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"));
    }

    @Test
    void shouldDeleteAnExistingAccountTest() throws Exception {
        mockMvc.perform(delete("/accounts/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteAccountThatDoesNotExistTest() throws Exception {
        mockMvc.perform(delete("/accounts/2"))
                .andExpect(status().isNotFound());
    }
}
