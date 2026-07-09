package com.finance_dashboard.budgets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void shouldReturnBudgetWhenDataIsSavedTest() throws Exception {
        mockMvc.perform(get("/budgets/21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.budgetId").value(21L))
                .andExpect(jsonPath("$.account.accountId").value(5L))
                .andExpect(jsonPath("$.name").value("Monthly Electronics"))
                .andExpect(jsonPath("$.category").value("ELECTRONICS"))
                .andExpect(jsonPath("$.targetAmount").value(1000.00));
    }

    @Test
    void shouldNotReturnBudgetWithUnknownIdTest() throws Exception {
        mockMvc.perform(get("/budgets/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewBudgetTest() throws Exception {

        BudgetRequest request = new BudgetRequest(
                5L,
                "Food Budget",
                "FOOD",
                new BigDecimal("300.00"),
                BigDecimal.ZERO,
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 31),
                true,
                new BigDecimal("250.00"));

        MvcResult result = mockMvc.perform(post("/budgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String location = result.getResponse().getHeader("Location");

        assertThat(location).isNotNull();

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account.accountId").value(5L))
                .andExpect(jsonPath("$.name").value("Food Budget"))
                .andExpect(jsonPath("$.category").value("FOOD"))
                .andExpect(jsonPath("$.targetAmount").value(300.00));
    }

    @Test
    void shouldUpdateExistingBudgetTest() throws Exception {

        BudgetRequest updateRequest = new BudgetRequest(
                5L,
                "Updated Budget",
                "RENT",
                new BigDecimal("1200.00"),
                new BigDecimal("400.00"),
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 31),
                true,
                new BigDecimal("1000.00"));

        mockMvc.perform(put("/budgets/21")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/budgets/21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Budget"))
                .andExpect(jsonPath("$.category").value("RENT"))
                .andExpect(jsonPath("$.targetAmount").value(1200.00));
    }

    @Test
    void shouldDeleteAnExistingBudgetTest() throws Exception {
        mockMvc.perform(delete("/budgets/21"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldNotDeleteBudgetThatDoesNotExistTest() throws Exception {
        mockMvc.perform(delete("/budgets/999"))
                .andExpect(status().isNotFound());
    }
}
