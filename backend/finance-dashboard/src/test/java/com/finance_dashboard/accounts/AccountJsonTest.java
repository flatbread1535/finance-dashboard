package com.finance_dashboard.accounts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class AccountJsonTest {

    @Autowired
    // Provides assertj methods to parse Java objects to JSON
    JacksonTester<Account> json;

    @Autowired
    JacksonTester<Account[]> jsonList;

    private Account[] accounts;

    @BeforeEach
    void setUp() {
        accounts = new Account[] {
                new Account(5L, "adam", "ajlarson0731@gmail.com", "abc123", "937-479-0303"),
                new Account(6L, "chris", "clarson1@woh.rr.com", "def456", "937-416-5220"),
                new Account(7L, "amber", "anlarson0702@gmail.com", "ghi789", "937-417-7870")
        };
    }

    @Test
    void accountSerializationTest() throws IOException {
        Account account = new Account(5L, "adam", "ajlarson0731@gmail.com", "abc123", "937-479-0303");

        assertThat(json.write(account)).hasJsonPathNumberValue("@.accountId");
        assertThat(json.write(account)).extractingJsonPathNumberValue("@.accountId").isEqualTo(5);

        assertThat(json.write(account)).hasJsonPathStringValue("@.username");
        assertThat(json.write(account)).extractingJsonPathStringValue("@.username").isEqualTo("adam");

        assertThat(json.write(account)).hasJsonPathStringValue("@.email");
        assertThat(json.write(account)).extractingJsonPathStringValue("@.email").isEqualTo("ajlarson0731@gmail.com");

        assertThat(json.write(account)).hasJsonPathStringValue("@.hashPassword");
        assertThat(json.write(account)).extractingJsonPathStringValue("@.hashPassword").isEqualTo("abc123");

        assertThat(json.write(account)).hasJsonPathStringValue("@.phoneNumber");
        assertThat(json.write(account)).extractingJsonPathStringValue("@.phoneNumber").isEqualTo("937-479-0303");
    }

    @Test
    void accountListSerializationTest() throws IOException {
        assertThat(jsonList.write(accounts)).isStrictlyEqualToJson("list.json");

    }

    @Test
    void accountDeserializationTest() throws IOException {
        String expected = """
                {
                    "accountId": 5,
                    "username": "adam",
                    "email": "ajlarson0731@gmail.com",
                    "hashPassword": "abc123",
                    "phoneNumber": "937-479-0303"
                }
                """;

        Account account = json.parseObject(expected);
        assertThat(account.getAccountId()).isEqualTo(5);
        assertThat(account.getUsername()).isEqualTo("adam");
        assertThat(account.getEmail()).isEqualTo("ajlarson0731@gmail.com");
        assertThat(account.getHashPassword()).isEqualTo("abc123");
        assertThat(account.getPhoneNumber()).isEqualTo("937-479-0303");
    }

    @Test
    void accountListDeserializationTest() throws IOException {
        String expected = """
                [
                  {
                    "accountId": 5,
                    "username": "adam",
                    "email": "ajlarson0731@gmail.com",
                    "hashPassword": "abc123",
                    "phoneNumber": "937-479-0303"
                  },
                  {
                    "accountId": 6,
                    "username": "chris",
                    "email": "clarson1@woh.rr.com",
                    "hashPassword": "def456",
                    "phoneNumber": "937-416-5220"
                  },
                  {
                    "accountId": 7,
                    "username": "amber",
                    "email": "anlarson0702@gmail.com",
                    "hashPassword": "ghi789",
                    "phoneNumber": "937-417-7870"
                  }
                ]
                """;
        Account[] parsedJson = jsonList.parseObject(expected);

        assertThat(parsedJson).hasSize(3);

        assertThat(parsedJson[0].getAccountId()).isEqualTo(5L);
        assertThat(parsedJson[0].getUsername()).isEqualTo("adam");

        assertThat(parsedJson[1].getEmail()).isEqualTo("clarson1@woh.rr.com");
        assertThat(parsedJson[1].getHashPassword()).isEqualTo("def456");

        assertThat(parsedJson[2].getAccountId()).isEqualTo(7L);
        assertThat(parsedJson[2].getPhoneNumber()).isEqualTo("937-417-7870");
    }
}
