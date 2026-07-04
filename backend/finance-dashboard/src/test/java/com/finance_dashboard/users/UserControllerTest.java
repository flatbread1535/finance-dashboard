package com.finance_dashboard.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAUserWhenDataIsSaved() throws Exception {
        mockMvc.perform(get("/users/5"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(5))
        .andExpect(jsonPath("$.username").value("adam"))
        .andExpect(jsonPath("$.email").value("ajlarson0731@gmail.com"))
        .andExpect(jsonPath("$.hashPassword").value("abc123"))
        .andExpect(jsonPath("$.phoneNumber").value("937-479-0303"))
        .andExpect(jsonPath("$.userId").isNotEmpty());
    }

    @Test
    void shouldNotReturnAUserWithAnUnknownId() throws Exception {
        mockMvc.perform(get("/users/1000"))
        .andExpect(status().isNotFound());
    }
}
