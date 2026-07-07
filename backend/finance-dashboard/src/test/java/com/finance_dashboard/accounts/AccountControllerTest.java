package com.finance_dashboard.accounts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
        AccountRegistrationRequest request = new AccountRegistrationRequest();
        request.setUsername("JohnDoe");
        request.setEmail("johndoe@gmail.com");
        request.setPassword("password123");
        request.setPhoneNumber("123-456-7890");

        MvcResult result = mockMvc.perform(post("/accounts")
                // Tells system the format of data being transmitted is JSON
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();
        
        String location = result.getResponse().getHeader("Location");
        assertThat(location).isNotNull();

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("JohnDoe"))
                .andExpect(jsonPath("$.email").value("johndoe@gmail.com"))
                .andExpect(jsonPath("$.hashPassword").value("password123"))
                .andExpect(jsonPath("$.phoneNumber").value("123-456-7890"));
    }

    @Test
    void shouldUpdateExistingAccountTest() throws Exception {
        AccountRegistrationRequest updateRequest = new AccountRegistrationRequest();
        updateRequest.setUsername("adam_updated");
        updateRequest.setEmail("newemail@gmail.com");
        updateRequest.setPassword("new_password");
        updateRequest.setPhoneNumber("123-456-7890");

        mockMvc.perform(put("/accounts/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(updateRequest)))
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
