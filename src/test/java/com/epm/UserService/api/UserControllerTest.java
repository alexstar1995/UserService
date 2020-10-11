package com.epm.UserService.api;

import com.epm.UserService.model.User;
import com.epm.UserService.repository.UserRepository;
import com.epm.UserService.util.AppConstants;
import com.epm.UserService.util.TestsUtil;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static com.epm.UserService.util.TestsUtil.INIT_DB;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseClient databaseClient;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "fs";
    private static final String LAST_NAME = "ls";
    private static final String GENDER = "gender";
    private static final Integer AGE = 10;

    private static final String USER1_SIGNATURE = "-USR-1";
    private static final String USER2_SIGNATURE = "-USR-2";
    private static final String USER3_SIGNATURE = "-USR-3";
    private static final String USER4_SIGNATURE = "-USR-4";

    private static final User USER_1;
    private static final User USER_2;
    private static final User USER_3;
    private static final User USER_4;

    static {

        USER_1 = TestsUtil.createUser(USERNAME + USER1_SIGNATURE, PASSWORD + USER1_SIGNATURE,
                EMAIL + USER1_SIGNATURE, FIRST_NAME + USER1_SIGNATURE,
                LAST_NAME + USER1_SIGNATURE, GENDER + USER1_SIGNATURE, AGE);

        USER_2 = TestsUtil.createUser(USERNAME + USER2_SIGNATURE, PASSWORD + USER2_SIGNATURE,
                EMAIL + USER2_SIGNATURE, FIRST_NAME + USER2_SIGNATURE,
                LAST_NAME + USER2_SIGNATURE, GENDER + USER2_SIGNATURE, AGE);

        USER_3 = TestsUtil.createUser(USERNAME + USER3_SIGNATURE, PASSWORD + USER3_SIGNATURE,
                EMAIL + USER3_SIGNATURE, FIRST_NAME + USER3_SIGNATURE,
                LAST_NAME + USER3_SIGNATURE, GENDER + USER3_SIGNATURE, AGE);

        USER_4 = TestsUtil.createUser(USERNAME + USER4_SIGNATURE, PASSWORD + USER4_SIGNATURE,
                EMAIL + USER4_SIGNATURE, FIRST_NAME + USER4_SIGNATURE,
                LAST_NAME + USER4_SIGNATURE, GENDER + USER4_SIGNATURE, AGE);
    }

    @BeforeAll
    public void allSetup() {

        databaseClient.execute(INIT_DB)
                .fetch()
                .rowsUpdated()
                .block();

        userRepository.deleteAll()
                .thenMany(Flux.fromIterable(Arrays.asList(USER_1, USER_2, USER_3)))
                .flatMap(userRepository::save)
                .blockLast();
    }

    private boolean compare(User user, User user1) {
        return user.getUsername().equals(user1.getUsername())
                && user.getPassword().equals(user1.getPassword())
                && user.getFirstName().equals(user1.getFirstName())
                && user.getLastName().equals(user1.getLastName())
                && user.getEmail().equals(user1.getEmail())
                && user.getGender().equals(user1.getGender())
                && user.getAge().equals(user1.getAge());
    }

    @Test
    public void getAllUsers () {

        webTestClient.get().uri(AppConstants.BASE_RESOURCE_URL.concat("/getAll"))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(User.class)
                .hasSize(3)
                .consumeWith(userStream -> {
                    List<User> users = userStream.getResponseBody();
                    assertNotNull(users);
                    assertTrue(compare(users.get(0), USER_1));
                    assertTrue(compare(users.get(1), USER_2));
                    assertTrue(compare(users.get(2), USER_3));
                });
    }

    @Test
    public void getUser() {

        webTestClient.get().uri(AppConstants.BASE_RESOURCE_URL.concat("/getUser/{userId}"),"2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody(User.class)
                .consumeWith(userStream -> {
                    User user = userStream.getResponseBody();
                    assertNotNull(user);
                    assertTrue(compare(user, USER_2));
                });
    }

    @Test
    public void createUser() {

        webTestClient.post().uri(AppConstants.BASE_RESOURCE_URL.concat("/createUser"))
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(USER_4), User.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody(User.class)
                .consumeWith(userStream -> {
                    User user = userStream.getResponseBody();
                    assertNotNull(user);
                    assertTrue(compare(user, USER_4));
                });
    }

    @Test
    public void updateUser() {

        webTestClient.put().uri(AppConstants.BASE_RESOURCE_URL.concat("/updateUser/{userId}"), "1")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(Mono.just(USER_4), User.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody(User.class)
                .consumeWith(userStream -> {
                    User user = userStream.getResponseBody();
                    assertNotNull(user);
                    assertTrue(compare(user, USER_4));
                });
    }

    @Test
    public void deleteUser() {

        webTestClient.delete().uri(AppConstants.BASE_RESOURCE_URL.concat("/deleteUser/{userId}"),"1")
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(streamResult -> {
                    String result = streamResult.getResponseBody();
                    assertEquals(result, "User deleted");
                });
    }
}