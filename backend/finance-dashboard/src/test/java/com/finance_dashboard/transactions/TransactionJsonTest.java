package com.finance_dashboard.transactions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import com.finance_dashboard.accounts.Account;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class TransactionJsonTest {

    @Autowired
    JacksonTester<Transaction> json;

    @Autowired
    JacksonTester<Transaction[]> jsonList;

    private Transaction[] transactions;

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
        transactions = new Transaction[] {
                new Transaction(
                        14L,
                        LocalDateTime.of(2026, 7, 7, 20, 15),
                        adam,
                        new BigDecimal("123.45"),
                        "USD",
                        "COMPLETED",
                        "ELECTRONICS"),

                new Transaction(
                        15L,
                        LocalDateTime.of(2026, 7, 7, 20, 30),
                        chris,
                        new BigDecimal("850.50"),
                        "USD",
                        "PENDING",
                        "RENT"),

                new Transaction(
                        16L,
                        LocalDateTime.of(2026, 7, 7, 21, 00),
                        amber,
                        new BigDecimal("130.75"),
                        "USD",
                        "COMPLETED",
                        "FOOD")
        };
    }

    @Test
    void transactionSerializationTest() throws IOException {
        Transaction transaction = transactions[0];

        assertThat(json.write(transaction)).hasJsonPathNumberValue("@.transactionId");
        assertThat(json.write(transaction)).extractingJsonPathNumberValue("@.transactionId")
                .isEqualTo(14);

        assertThat(json.write(transaction)).hasJsonPathNumberValue("@.account.accountId");
        assertThat(json.write(transaction)).extractingJsonPathNumberValue("@.account.accountId")
                .isEqualTo(5);

        assertThat(json.write(transaction)).hasJsonPathStringValue("@.currency");
        assertThat(json.write(transaction)).extractingJsonPathStringValue("@.currency")
                .isEqualTo("USD");

        assertThat(json.write(transaction)).hasJsonPathStringValue("@.status");
        assertThat(json.write(transaction)).extractingJsonPathStringValue("@.status")
                .isEqualTo("COMPLETED");

        assertThat(json.write(transaction)).hasJsonPathStringValue("@.category");
        assertThat(json.write(transaction)).extractingJsonPathStringValue("@.category")
                .isEqualTo("ELECTRONICS");
    }

    @Test
    void transactionListSerializationTest() throws IOException {
        assertThat(jsonList.write(transactions)).isStrictlyEqualToJson("list.json");

    }

    @Test
    void transactionDeserializationTest() throws IOException {
        String expected = """
                {
                    "transactionId": 14,
                    "timeCreated": "2026-07-07T20:15:00",
                    "account": {
                        "accountId": 5,
                        "username": "adam",
                        "email": "ajlarson0731@gmail.com",
                        "hashPassword": "abc123",
                        "phoneNumber": "937-479-0303"
                },
                    "amount": 123.45,
                    "currency": "USD",
                    "status": "COMPLETED",
                    "category": "ELECTRONICS"
                }
                """;

        Transaction transaction = json.parseObject(expected);
        assertThat(transaction.getTransactionId()).isEqualTo(14L);
        assertThat(transaction.getAccount().getAccountId()).isEqualTo(5L);
        assertThat(transaction.getAccount().getUsername()).isEqualTo("adam");
        assertThat(transaction.getAccount().getEmail()).isEqualTo("ajlarson0731@gmail.com");
        assertThat(transaction.getAmount()).isEqualByComparingTo("123.45");
        assertThat(transaction.getCurrency()).isEqualTo("USD");
        assertThat(transaction.getStatus()).isEqualTo("COMPLETED");
        assertThat(transaction.getCategory()).isEqualTo("ELECTRONICS");
    }

    @Test
    void transactionListDeserializationTest() throws IOException {
        String expected = """
                [
                  {
                    "transactionId": 14,
                    "timeCreated": "2026-07-07T20:15:00",
                    "account": {
                      "accountId": 5,
                      "username": "adam",
                      "email": "ajlarson0731@gmail.com",
                      "hashPassword": "abc123",
                      "phoneNumber": "937-479-0303"
                    },
                    "amount": 123.45,
                    "currency": "USD",
                    "status": "COMPLETED",
                    "category": "ELECTRONICS"
                  },
                  {
                    "transactionId": 15,
                    "timeCreated": "2026-07-07T20:30:00",
                    "account": {
                      "accountId": 6,
                      "username": "chris",
                      "email": "clarson1@woh.rr.com",
                      "hashPassword": "def456",
                      "phoneNumber": "937-416-5220"
                    },
                    "amount": 850.50,
                    "currency": "USD",
                    "status": "PENDING",
                    "category": "RENT"
                  },
                  {
                    "transactionId": 16,
                    "timeCreated": "2026-07-07T21:00:00",
                    "account": {
                      "accountId": 7,
                      "username": "amber",
                      "email": "anlarson0702@gmail.com",
                      "hashPassword": "ghi789",
                      "phoneNumber": "937-417-7870"
                    },
                    "amount": 130.75,
                    "currency": "USD",
                    "status": "COMPLETED",
                    "category": "FOOD"
                  }
                ]
                """;

        Transaction[] parsedJson = jsonList.parseObject(expected);

        assertThat(parsedJson).hasSize(3);

        assertThat(parsedJson[0].getTransactionId()).isEqualTo(14L);
        assertThat(parsedJson[0].getAccount().getUsername()).isEqualTo("adam");

        assertThat(parsedJson[1].getTransactionId()).isEqualTo(15L);
        assertThat(parsedJson[1].getStatus()).isEqualTo("PENDING");

        assertThat(parsedJson[2].getTransactionId()).isEqualTo(16L);
        assertThat(parsedJson[2].getCategory()).isEqualTo("FOOD");
        assertThat(parsedJson[2].getAmount()).isEqualByComparingTo("130.75");
    }
}
