package co.uk.jdreamer;

import co.uk.jdreamer.model.persistence.User;
import co.uk.jdreamer.model.requests.CreateUserRequest;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    int port;

    @Test
    public void testCreateUser() throws URISyntaxException {

        final String loginUrl = "http://localhost:" + port + "/api/user/create";
        URI uri = new URI(loginUrl);

        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("jdreamer");
        userRequest.setPassword("password");
        userRequest.setConfirmPassword("password");

        HttpEntity<CreateUserRequest> entity = new HttpEntity(userRequest);

        ResponseEntity<User> response = this.testRestTemplate.postForEntity(uri, entity, User.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testLoginUser() throws URISyntaxException {

        final String loginUrl = "http://localhost:" + port + "/login";
        URI uri = new URI(loginUrl);

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", "jdreamer");
        loginMap.put("password", "password");

        HttpEntity<Map> entity = new HttpEntity(loginMap);

        ResponseEntity<Void> response = this.testRestTemplate.postForEntity(uri, entity, Void.class);

        assertNotNull(response.getHeaders().get("Authorization"));
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    }


}
