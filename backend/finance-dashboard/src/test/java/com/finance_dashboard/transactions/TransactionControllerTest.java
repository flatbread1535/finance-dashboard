package com.finance_dashboard.transactions;

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

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void shouldReturnTransactionWhenDataIsSavedTest() throws Exception {
        mockMvc.perform(get("/transactions/14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(14L))
                .andExpect(jsonPath("$.account.accountId").value(5L))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.category").value("ELECTRONICS"));
    }

    @Test
    void shouldNotReturnTransactionWithUnknownIdTest() throws Exception {
        mockMvc.perform(get("/transactions/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewTransactionTest() throws Exception {
        TransactionRequest request = new TransactionRequest(
                5L,
                new BigDecimal("123.45"),
                "USD",
                "COMPLETED",
                "ELECTRONICS");

        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");
        assertThat(location).isNotNull();

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.accountId").value(5L))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.category").value("ELECTRONICS"));
    }

    @Test
    void shouldUpdateExistingTransactionTest() throws Exception {
        TransactionRequest updateRequest = new TransactionRequest(
                5L,
                new BigDecimal("525.68"),
                "USD",
                "PENDING",
                "RENT");

        mockMvc.perform(put("/transactions/14")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/transactions/14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.category").value("RENT"));
    }

    @Test
    void shouldDeleteAnExistingTransactionTest() throws Exception {
        mockMvc.perform(delete("/transactions/14"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteTransactionThatDoesNotExistTest() throws Exception {
        mockMvc.perform(delete("/transactions/20"))
                .andExpect(status().isNotFound());
    }
}
