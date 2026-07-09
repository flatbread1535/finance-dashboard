package com.finance_dashboard.budgets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import com.finance_dashboard.accounts.Account;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BudgetJsonTest {

    @Autowired
    JacksonTester<Budget> json;

    @Autowired
    JacksonTester<Budget[]> jsonList;

    private Budget[] budgets;

    Account adam = new Account(
            5L,
            "adam",
            "ajlarson0731@gmail.com",
            "abc123",
            "937-479-0303");

    Account chris = new Account(
            6L,
            "chris",
            "clarson1@woh.rr.com",
            "def456",
            "937-416-5220");

    Account amber = new Account(
            7L,
            "amber",
            "anlarson0702@gmail.com",
            "ghi789",
            "937-417-7870");

    @BeforeEach
    void setUp() {
        budgets = new Budget[] {
                new Budget(
                        21L,
                        adam,
                        "Monthly Electronics",
                        "ELECTRONICS",
                        new BigDecimal("1000.00"),
                        new BigDecimal("250.00"),
                        LocalDateTime.of(2026, 7, 8, 12, 0),
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 31),
                        true,
                        new BigDecimal("800.00")
                ),

                new Budget(
                        22L,
                        chris,
                        "Monthly Rent",
                        "RENT",
                        new BigDecimal("1500.00"),
                        new BigDecimal("850.00"),
                        LocalDateTime.of(2026, 7, 8, 12, 30),
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 31),
                        true,
                        new BigDecimal("1200.00")
                ),

                new Budget(
                        23L,
                        amber,
                        "Food Budget",
                        "FOOD",
                        new BigDecimal("500.00"),
                        new BigDecimal("130.75"),
                        LocalDateTime.of(2026, 7, 8, 13, 0),
                        LocalDate.of(2026, 7, 1),
                        LocalDate.of(2026, 7, 31),
                        false,
                        new BigDecimal("0.00")
                )
        };
    }

    @Test
    void budgetSerializationTest() throws IOException {
        Budget budget = budgets[0];

        assertThat(json.write(budget)).hasJsonPathNumberValue("@.budgetId");
        assertThat(json.write(budget))
                .extractingJsonPathNumberValue("@.budgetId")
                .isEqualTo(21);

        assertThat(json.write(budget)).hasJsonPathNumberValue("@.account.accountId");
        assertThat(json.write(budget))
                .extractingJsonPathNumberValue("@.account.accountId")
                .isEqualTo(5);

        assertThat(json.write(budget)).hasJsonPathStringValue("@.name");
        assertThat(json.write(budget))
                .extractingJsonPathStringValue("@.name")
                .isEqualTo("Monthly Electronics");

        assertThat(json.write(budget)).hasJsonPathStringValue("@.category");
        assertThat(json.write(budget))
                .extractingJsonPathStringValue("@.category")
                .isEqualTo("ELECTRONICS");
    }

    @Test
    void budgetListSerializationTest() throws IOException {
        assertThat(jsonList.write(budgets))
                .isStrictlyEqualToJson("list.json");
    }

    @Test
    void budgetDeserializationTest() throws IOException {
        String expected = """
                {
                  "budgetId": 21,
                  "account": {
                    "accountId": 5,
                    "username": "adam",
                    "email": "ajlarson0731@gmail.com",
                    "hashPassword": "abc123",
                    "phoneNumber": "937-479-0303"
                  },
                  "name": "Monthly Electronics",
                  "category": "ELECTRONICS",
                  "targetAmount": 1000.00,
                  "currentSpending": 250.00,
                  "timeCreated": "2026-07-08T12:00:00",
                  "startDate": "2026-07-01",
                  "endDate": "2026-07-31",
                  "isThresholdAlert": true,
                  "thresholdAlertValue": 800.00
                }
                """;

        Budget budget = json.parseObject(expected);

        assertThat(budget.getBudgetId()).isEqualTo(21L);
        assertThat(budget.getAccount().getAccountId()).isEqualTo(5L);
        assertThat(budget.getAccount().getUsername()).isEqualTo("adam");

        assertThat(budget.getName()).isEqualTo("Monthly Electronics");
        assertThat(budget.getCategory()).isEqualTo("ELECTRONICS");

        assertThat(budget.getTargetAmount())
                .isEqualByComparingTo("1000.00");

        assertThat(budget.getCurrentSpending())
                .isEqualByComparingTo("250.00");

        assertThat(budget.getIsThresholdAlert()).isTrue();

        assertThat(budget.getThresholdAlertValue())
                .isEqualByComparingTo("800.00");
    }

    @Test
    void budgetListDeserializationTest() throws IOException {
        String expected = """
                [
                  {
                    "budgetId": 21,
                    "account": {
                      "accountId": 5,
                      "username": "adam",
                      "email": "ajlarson0731@gmail.com",
                      "hashPassword": "abc123",
                      "phoneNumber": "937-479-0303"
                    },
                    "name": "Monthly Electronics",
                    "category": "ELECTRONICS",
                    "targetAmount": 1000.00,
                    "currentSpending": 250.00,
                    "timeCreated": "2026-07-08T12:00:00",
                    "startDate": "2026-07-01",
                    "endDate": "2026-07-31",
                    "isThresholdAlert": true,
                    "thresholdAlertValue": 800.00
                  },
                  {
                    "budgetId": 22,
                    "account": {
                      "accountId": 6,
                      "username": "chris",
                      "email": "clarson1@woh.rr.com",
                      "hashPassword": "def456",
                      "phoneNumber": "937-416-5220"
                    },
                    "name": "Monthly Rent",
                    "category": "RENT",
                    "targetAmount": 1500.00,
                    "currentSpending": 850.00,
                    "timeCreated": "2026-07-08T12:30:00",
                    "startDate": "2026-07-01",
                    "endDate": "2026-07-31",
                    "isThresholdAlert": true,
                    "thresholdAlertValue": 1200.00
                  },
                  {
                    "budgetId": 23,
                    "account": {
                      "accountId": 7,
                      "username": "amber",
                      "email": "anlarson0702@gmail.com",
                      "hashPassword": "ghi789",
                      "phoneNumber": "937-417-7870"
                    },
                    "name": "Food Budget",
                    "category": "FOOD",
                    "targetAmount": 500.00,
                    "currentSpending": 130.75,
                    "timeCreated": "2026-07-08T13:00:00",
                    "startDate": "2026-07-01",
                    "endDate": "2026-07-31",
                    "isThresholdAlert": false,
                    "thresholdAlertValue": 0.00
                  }
                ]
                """;

        Budget[] parsedJson = jsonList.parseObject(expected);

        assertThat(parsedJson).hasSize(3);

        assertThat(parsedJson[0].getBudgetId()).isEqualTo(21L);
        assertThat(parsedJson[0].getAccount().getUsername()).isEqualTo("adam");

        assertThat(parsedJson[1].getBudgetId()).isEqualTo(22L);
        assertThat(parsedJson[1].getCategory()).isEqualTo("RENT");

        assertThat(parsedJson[2].getBudgetId()).isEqualTo(23L);
        assertThat(parsedJson[2].getCategory()).isEqualTo("FOOD");

        assertThat(parsedJson[2].getCurrentSpending())
                .isEqualByComparingTo("130.75");
    }
}
