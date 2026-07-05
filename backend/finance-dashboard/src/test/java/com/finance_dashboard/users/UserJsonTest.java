package com.finance_dashboard.users;

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

    @Test
    void UserSerializationTest() throws IOException {
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
    void UserDeserializationTest() throws IOException {
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
}
