package com.finance_dashboard.users;

import org.junit.jupiter.api.Test;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.jayway.jsonpath.DocumentContext;

import static org.assertj.core.api.Assertions.assertThat;

// Starts SpringBoot application and makes it available for the test to perform requests to it
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired 
    // Injects a test helper to allow HTTP requests to the locally running application
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAUserWhenDataIsSaved() {
        // Makes an HTTP GET request to application endpoint /users/5
        ResponseEntity<String> response = restTemplate.getForEntity("/users/5", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
