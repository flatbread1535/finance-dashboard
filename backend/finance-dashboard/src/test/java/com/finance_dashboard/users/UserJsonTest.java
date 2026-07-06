package com.finance_dashboard.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;

@JsonTest
public class UserJsonTest {

    @Autowired
    // Provides assertj methods to parse Java objects to JSON
    JacksonTester<User> json;

    @Autowired
    JacksonTester<User[]> jsonList;

    private User[] users;

    @BeforeEach
    void setUp() {
        users = new User[] {
                new User(5L, "adam", "ajlarson0731@gmail.com", "abc123", "937-479-0303"),
                new User(6L, "chris", "clarson1@woh.rr.com", "def456", "937-416-5220"),
                new User(7L, "amber", "anlarson0702@gmail.com", "ghi789", "937-417-7870")
        };
    }

    @Test
    void userSerializationTest() throws IOException {
        User user = new User(5L, "adam", "ajlarson0731@gmail.com", "abc123", "937-479-0303");

        assertThat(json.write(user)).hasJsonPathNumberValue("@.userId");
        assertThat(json.write(user)).extractingJsonPathNumberValue("@.userId").isEqualTo(5);

        assertThat(json.write(user)).hasJsonPathStringValue("@.username");
        assertThat(json.write(user)).extractingJsonPathStringValue("@.username").isEqualTo("adam");

        assertThat(json.write(user)).hasJsonPathStringValue("@.email");
        assertThat(json.write(user)).extractingJsonPathStringValue("@.email").isEqualTo("ajlarson0731@gmail.com");

        assertThat(json.write(user)).hasJsonPathStringValue("@.hashPassword");
        assertThat(json.write(user)).extractingJsonPathStringValue("@.hashPassword").isEqualTo("abc123");

        assertThat(json.write(user)).hasJsonPathStringValue("@.phoneNumber");
        assertThat(json.write(user)).extractingJsonPathStringValue("@.phoneNumber").isEqualTo("937-479-0303");
    }

    @Test
    void userListSerializationTest() throws IOException {
        assertThat(jsonList.write(users)).isStrictlyEqualToJson("list.json");

    }

    @Test
    void userDeserializationTest() throws IOException {
        String expected = """
                {
                    "userId": 5,
                    "username": "adam",
                    "email": "ajlarson0731@gmail.com",
                    "hashPassword": "abc123",
                    "phoneNumber": "937-479-0303"
                }
                """;

        User user = json.parseObject(expected);
        assertThat(user.getUserId()).isEqualTo(5);
        assertThat(user.getUsername()).isEqualTo("adam");
        assertThat(user.getEmail()).isEqualTo("ajlarson0731@gmail.com");
        assertThat(user.getHashPassword()).isEqualTo("abc123");
        assertThat(user.getPhoneNumber()).isEqualTo("937-479-0303");
    }

    @Test
    void userListDeserializationTest() throws IOException {
        String expected = """
                [
                  {
                    "userId": 5,
                    "username": "adam",
                    "email": "ajlarson0731@gmail.com",
                    "hashPassword": "abc123",
                    "phoneNumber": "937-479-0303"
                  },
                  {
                    "userId": 6,
                    "username": "chris",
                    "email": "clarson1@woh.rr.com",
                    "hashPassword": "def456",
                    "phoneNumber": "937-416-5220"
                  },
                  {
                    "userId": 7,
                    "username": "amber",
                    "email": "anlarson0702@gmail.com",
                    "hashPassword": "ghi789",
                    "phoneNumber": "937-417-7870"
                  }
                ]
                """;
        User[] parsedJson = jsonList.parseObject(expected);

        assertThat(parsedJson).hasSize(3);

        assertThat(parsedJson[0].getUserId()).isEqualTo(5L);
        assertThat(parsedJson[0].getUsername()).isEqualTo("adam");

        assertThat(parsedJson[1].getEmail()).isEqualTo("clarson1@woh.rr.com");
        assertThat(parsedJson[1].getHashPassword()).isEqualTo("def456");

        assertThat(parsedJson[2].getUserId()).isEqualTo(7L);
        assertThat(parsedJson[2].getPhoneNumber()).isEqualTo("937-417-7870");
    }
}
