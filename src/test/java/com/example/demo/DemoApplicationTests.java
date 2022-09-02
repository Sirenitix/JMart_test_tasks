package com.example.demo;

import com.example.demo.dto.*;
import com.example.demo.entity.UsersDto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import java.io.IOException;
import java.util.Objects;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

    @Autowired
    private WebTestClient webClient;

    @Value("${getSingleUser}")
    String singleUserBaseUrl;

    @Value("${getListOfUsers}")
    String listOfUsersBaseUrl;

    @Value("${loginApi}")
    String loginApi;

    private static MockWebServer mockWebServer;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("reqres-api.base-url", () -> mockWebServer.url("/").url().toString());
    }

    @BeforeAll
    static void setupMockWebServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }


    @Test
    void isUserExist() {
        UserDto userDto = new UserDto();
        int realId = 2;
        webClient.get().uri(singleUserBaseUrl + realId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(UserDto.class)
                .consumeWith(response -> {
                    userDto.setData(Objects.requireNonNull(response.getResponseBody()).getData());
                    userDto.setSupport(response.getResponseBody().getSupport());
                });
        log.info(userDto.toString() + " - user information");
        assertThat(userDto.getData()).isNotNull();
        assertThat(userDto.getSupport()).isNotNull();
    }




    @Test
    void isListOfUsersExists() {
        UsersDto usersDto = new UsersDto();
        int pageNumber = 2;
        webClient.get().uri(listOfUsersBaseUrl + pageNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(UsersDto.class)
                .consumeWith(response -> {
                    usersDto.setData(Objects.requireNonNull(response.getResponseBody()).getData());
                    usersDto.setPage(response.getResponseBody().getPage());
                    usersDto.setPer_page(response.getResponseBody().getPer_page());
                    usersDto.setTotal(response.getResponseBody().getTotal());
                    usersDto.setTotal_pages(response.getResponseBody().getTotal_pages());

                });
        log.info(usersDto + " - user information");
        assertThat(usersDto.getData()).isNotNull();
        assertThat(usersDto.getPage()).isEqualTo(pageNumber);
        assertThat(usersDto.getPer_page()).isNotNull();
        assertThat(usersDto.getTotal()).isNotNull();
        assertThat(usersDto.getTotal_pages()).isNotNull();

    }


    @Test
    void isStatusCorrectWhenParametersAreMissing(){
        LoginErrorDto loginErrorDto = new LoginErrorDto();
        webClient.post().uri(loginApi)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(LoginErrorDto.class)
                .consumeWith(response -> {
                    loginErrorDto.setError(response.getResponseBody().getError());
                });
        log.info(loginErrorDto.getError() + " - error message");
        assertThat(loginErrorDto.getError()).isNotEmpty();
    }

    @Test
    void validData(){
        LoginErrorDto loginErrorDto = new LoginErrorDto();
        webClient.post().uri(loginApi)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(LoginErrorDto.class)
                .consumeWith(response -> {
                    loginErrorDto.setError(response.getResponseBody().getError());
                });
        log.info(loginErrorDto.getError() + " - error message");
        assertThat(loginErrorDto.getError()).isNotEmpty();

    }

    @Test
    void creationTest(){

        NewUserRequestDto newUserRequestDto = NewUserRequestDto.builder().job("Manager").name("Alex").build();
        webClient.post().uri(singleUserBaseUrl)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newUserRequestDto), NewUserRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(NewUserResponseDto.class)
                .consumeWith(result -> {
                    NewUserResponseDto newUserResponseDto = result.getResponseBody();
                    assert newUserResponseDto != null;
                    assertThat(newUserResponseDto.getJob()).isEqualTo(newUserRequestDto.getJob());
                    assertThat(newUserResponseDto.getName()).isEqualTo(newUserRequestDto.getName());
                    assertThat(newUserResponseDto.getId()).isNotEmpty();
                    assertThat(newUserResponseDto.getCreatedAt()).isNotEmpty();
                    log.info(newUserResponseDto + " - new user response");
                });
    }

    @Test
    void updatingTest(){
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder().job("Manager").name("Alex").build();
        webClient.put().uri(singleUserBaseUrl)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUserRequestDto), UpdateUserRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UpdateUserResponseDto.class)
                .consumeWith(result -> {
                    UpdateUserResponseDto updateUserResponseDto = result.getResponseBody();
                    assert updateUserResponseDto != null;
                    assertThat(updateUserResponseDto.getJob()).isEqualTo(updateUserResponseDto.getJob());
                    assertThat(updateUserResponseDto.getName()).isEqualTo(updateUserResponseDto.getName());
                    assertThat(updateUserResponseDto.getUpdatedAt()).isNotEmpty();
                    log.info(updateUserResponseDto + " - updated user response");
                });
    }

    @Test
    void deletionTest(){
        webClient.delete().uri(singleUserBaseUrl)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void patchingTest(){
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder().job("Manager").name("Alex").build();
        webClient.patch().uri(singleUserBaseUrl)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUserRequestDto), UpdateUserRequestDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UpdateUserResponseDto.class)
                .consumeWith(result -> {
                    UpdateUserResponseDto updateUserResponseDto = result.getResponseBody();
                    assert updateUserResponseDto != null;
                    assertThat(updateUserResponseDto.getJob()).isEqualTo(updateUserResponseDto.getJob());
                    assertThat(updateUserResponseDto.getName()).isEqualTo(updateUserResponseDto.getName());
                    assertThat(updateUserResponseDto.getUpdatedAt()).isNotEmpty();
                    log.info(updateUserResponseDto + " - patch user response");
                });
    }



}
