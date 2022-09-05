package com.example.demo;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.entity.ResourceDto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Url reqresProperties;

    @Autowired
    private WebTestClient webClient;

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
        double invalidId = -10000.0;
        int incorrectId = 100000;

        // get existed user
        webClient.get().uri(reqresProperties.getUsersApi() + realId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(UserDto.class)
                .consumeWith(response -> {
                    userDto.setData(Objects.requireNonNull(response.getResponseBody()).getData());
                    userDto.setSupport(response.getResponseBody().getSupport());
                });
        log.info(userDto + " - user information");
        assertThat(userDto.getData()).isNotNull();
        assertThat(userDto.getSupport()).isNotNull();

        // get user by invalid id
        webClient.get().uri(reqresProperties.getUsersApi() + invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();


        // get user that not exist
        webClient.get().uri(reqresProperties.getUsersApi() + incorrectId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

    }




    @Test
    void isListOfUsersTests() {

        UsersDto usersDto = new UsersDto();
        int pageNumber = 2;
        int perPageNumber = 5;
        double invalidPage = -100000.0;
        String incorrectData = "@#$!&*)+";

        // get list of users with valid offset number
        webClient.get().uri(reqresProperties.getUsersApiWithOffset() + pageNumber)
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
        assertThat(usersDto.getData().size()).isGreaterThan(0);

        // get list of users with invalid offset number
        webClient.get().uri(reqresProperties.getUsersApiWithOffset() + invalidPage)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(UsersDto.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).getData().size()).isEqualTo(0);
                });

        // offset with negative number should return 1 page
        webClient.get().uri(reqresProperties.getUsersApiWithOffset() + invalidPage)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(UsersDto.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).getPage()).isEqualTo(1);
                });

        // request with incorrect data should return response with 400 status and
        webClient.get().uri(reqresProperties.getUsersApiWithOffset() + incorrectData)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody())).isNotEmpty();
                });

        // request with offset and limit
        webClient.get().uri(reqresProperties.getUsersApiWithOffsetAndLimit(), pageNumber,  perPageNumber )
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
        assertThat(usersDto.getPer_page()).isEqualTo(perPageNumber);
        assertThat(usersDto.getData().size()).isEqualTo(perPageNumber);


    }


    @Test
    void isResourceExist() {

        ResourceDto resourceDto = new ResourceDto();
        int realId = 2;
        double invalidId = -10000.0;
        int incorrectId = 100000;

        // get existed user
        webClient.get().uri(reqresProperties.getUsersApi() + realId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(ResourceDto.class)
                .consumeWith(response -> {
                    resourceDto.setData(Objects.requireNonNull(response.getResponseBody()).getData());
                    resourceDto.setSupport(response.getResponseBody().getSupport());
                });
        log.info(resourceDto + " - resource information");
        assertThat(resourceDto.getData()).isNotNull();
        assertThat(resourceDto.getSupport()).isNotNull();

        // get user by invalid id
        webClient.get().uri(reqresProperties.getUsersApi() + invalidId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();


        // get user that not exist
        webClient.get().uri(reqresProperties.getUsersApi() + incorrectId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

    }




    @Test
    void isListOfResourceTests() {

        ResourcesDto resourceDto = new ResourcesDto();
        int pageNumber = 2;
        int perPageNumber = 5;
        double invalidPage = -100000.0;
        String incorrectData = "@#$!&*)+";

        // get list of users with valid offset number
        webClient.get().uri(reqresProperties.getUsersApiWithOffset() + pageNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(ResourcesDto.class)
                .consumeWith(response -> {
                    resourceDto.setData(Objects.requireNonNull(response.getResponseBody()).getData());
                    resourceDto.setPage(response.getResponseBody().getPage());
                    resourceDto.setPer_page(response.getResponseBody().getPer_page());
                    resourceDto.setTotal(response.getResponseBody().getTotal());
                    resourceDto.setTotal_pages(response.getResponseBody().getTotal_pages());

                });
        log.info(resourceDto + " - resource information");
        assertThat(resourceDto.getData()).isNotNull();
        assertThat(resourceDto.getPage()).isEqualTo(pageNumber);
        assertThat(resourceDto.getPer_page()).isNotNull();
        assertThat(resourceDto.getTotal()).isNotNull();
        assertThat(resourceDto.getTotal_pages()).isNotNull();
        assertThat(resourceDto.getData().size()).isGreaterThan(0);

        // get list of users with invalid offset number
        webClient.get().uri(reqresProperties.getUsersApiWithOffset() + invalidPage)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(ResourcesDto.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).getData().size()).isEqualTo(0);
                });

        // offset with negative number should return 1 page
        webClient.get().uri(reqresProperties.getUsersApiWithOffset() + invalidPage)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(ResourcesDto.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).getPage()).isEqualTo(1);
                });

        // request with incorrect data should return response with 400 status and
        webClient.get().uri(reqresProperties.getUsersApiWithOffset() + incorrectData)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody())).isNotEmpty();
                });

        // request with offset and limit
        webClient.get().uri(reqresProperties.getUsersApiWithOffsetAndLimit(), pageNumber,  perPageNumber )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(ResourcesDto.class)
                .consumeWith(response -> {
                    resourceDto.setData(Objects.requireNonNull(response.getResponseBody()).getData());
                    resourceDto.setPage(response.getResponseBody().getPage());
                    resourceDto.setPer_page(response.getResponseBody().getPer_page());
                    resourceDto.setTotal(response.getResponseBody().getTotal());
                    resourceDto.setTotal_pages(response.getResponseBody().getTotal_pages());

                });
        log.info(resourceDto + " - resource information");
        assertThat(resourceDto.getData()).isNotNull();
        assertThat(resourceDto.getPage()).isEqualTo(pageNumber);
        assertThat(resourceDto.getPer_page()).isNotNull();
        assertThat(resourceDto.getTotal()).isNotNull();
        assertThat(resourceDto.getTotal_pages()).isNotNull();
        assertThat(resourceDto.getPer_page()).isEqualTo(perPageNumber);
        assertThat(resourceDto.getData().size()).isGreaterThan(0);

    }


    @Test
    void creationTest(){

        // create with correct data
        NewUserRequestDto newUserRequestDto = NewUserRequestDto.builder().name("Billy").job("Marketing").build();
        webClient.post().uri(reqresProperties.getUsersApi())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newUserRequestDto), NewUserRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(NewUserResponseDto.class)
                .consumeWith(result -> {
                    NewUserResponseDto newUserResponseDto = result.getResponseBody();
                    assert newUserResponseDto != null;
                    assertThat(newUserResponseDto.getName()).isEqualTo(newUserRequestDto.getName());
                    assertThat(newUserResponseDto.getId()).isInstanceOf(String.class);
                    assertThat(newUserResponseDto.getId()).isNotNull();
                    assertThat(newUserResponseDto.getJob()).isEqualTo(newUserRequestDto.getJob());
                    assertThat(newUserResponseDto.getCreatedAt()).isInstanceOf(String.class);
                    assertThat(newUserResponseDto.getCreatedAt()).isNotEmpty();
                    log.info(newUserResponseDto + " - new user creation response");
                });

        // request without any data
        LoginErrorDto loginErrorDto = new LoginErrorDto();
        webClient.post().uri(reqresProperties.getUsersApi())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(LoginErrorDto.class)
                .consumeWith(response -> {
                    loginErrorDto.setError(Objects.requireNonNull(response.getResponseBody()).getError());
                });
        log.info(loginErrorDto.getError() + " - error message");
        assertThat(loginErrorDto.getError()).isNotEmpty();
    }

    @Test
    void registrationTest(){

        // POST request with correct full data
        RegistrationRequest registrationRequestFull = RegistrationRequest.builder().email("eve.holt@reqres.in").password("12345").username("eve_ice").build();
        webClient.post().uri(reqresProperties.getRegistrationBaseUrl())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequestFull), RegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RegistrationResponse.class)
                .consumeWith(result -> {
                    RegistrationResponse registrationResponse = result.getResponseBody();
                    assert registrationResponse != null;
                    assertThat(registrationResponse.getToken()).isNotEmpty();
                    assertThat(registrationResponse.getId()).isInstanceOf(Long.class);
                    assertThat(registrationResponse.getId()).isNotNull();
                    log.info(registrationResponse + " - registration response with full data");
                });

        // POST request with correct required data
        RegistrationRequest registrationRequest = RegistrationRequest.builder().email("eve.holt@reqres.in").password("12345").build();
        webClient.post().uri(reqresProperties.getRegistrationBaseUrl())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(registrationRequest), RegistrationRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RegistrationResponse.class)
                .consumeWith(result -> {
                    RegistrationResponse registrationResponse = result.getResponseBody();
                    assert registrationResponse != null;
                    assertThat(registrationResponse.getToken()).isNotEmpty();
                    assertThat(registrationResponse.getId()).isInstanceOf(Long.class);
                    assertThat(registrationResponse.getId()).isNotNull();
                    log.info(registrationResponse + " - registration response with required data");
                });

        // POST request with user that not exist is system
        RegistrationRequest unknownUserRegistrationRequest = RegistrationRequest.builder().email("Norbert").password("12345").build();
        webClient.post().uri(reqresProperties.getRegistrationBaseUrl())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(unknownUserRegistrationRequest), RegistrationRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(RegistrationError.class)
                .consumeWith(result -> {
                    RegistrationError registrationResponse = result.getResponseBody();
                    assert registrationResponse != null;
                    assertThat(registrationResponse.getError()).isInstanceOf(String.class);
                    assertThat(registrationResponse.getError()).isEqualTo("Note: Only defined users succeed registration");
                    log.info(registrationResponse.getError() + " - registration error response");
                });

        // POST data without data at all
        RegistrationRequest emptyRegistrationRequest = new RegistrationRequest();
        webClient.post().uri(reqresProperties.getRegistrationBaseUrl())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(emptyRegistrationRequest), RegistrationRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(RegistrationError.class)
                .consumeWith(result -> {
                    RegistrationError registrationResponse = result.getResponseBody();
                    assert registrationResponse != null;
                    assertThat(registrationResponse.getError()).isNotEmpty();
                    log.info(registrationResponse.getError() + " - registration error response");
                });

    }

    @Test
    void updatingTest(){

        int userId = 3;
        int idThatNotExist = 999999;
        double idWithIncorrectId = -10000.0;

        // Updating with correct data
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder().job("Manager").name("Alex").build();
        webClient.put().uri(reqresProperties.getUsersApi() + userId)
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

        // Updating entity that not exist
        UpdateUserRequestDto updateUser = UpdateUserRequestDto.builder().job("CEO").name("Henry").build();
        webClient.put().uri(reqresProperties.getUsersApi() + idThatNotExist)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUser), UpdateUserRequestDto.class)
                .exchange()
                .expectStatus().isNotFound();

        // Updating by incorrect id
        UpdateUserRequestDto updateUserWithIncorrectId = UpdateUserRequestDto.builder().job("Head of Department").name("Sia").build();
        webClient.put().uri(reqresProperties.getUsersApi() + idWithIncorrectId)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUserWithIncorrectId), UpdateUserRequestDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deletionTest(){

        int userId = 2;
        int idThatNotExist  = 999999;
        double incorrectUserId = -10000.0;

        // delete the invalid id
        webClient.delete().uri(reqresProperties.getUsersApi() + idThatNotExist)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        // delete id that exist
        webClient.delete().uri(reqresProperties.getUsersApi() + userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        // deletion of id that already deleted
        webClient.delete().uri(reqresProperties.getUsersApi() + userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();


        // delete entity with incorrect id
        webClient.delete().uri(reqresProperties.getUsersApi() + incorrectUserId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();

    }


    @Test
    void patchingTest(){

        int userId = 3;
        int idThatNotExist = 999999;
        double idWithIncorrectId = -10000.0;

        // Updating with correct data
        UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder().job("Legal Assistant").name("Sara").build();
        webClient.patch().uri(reqresProperties.getUsersApi() + userId)
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

        // Updating entity that not exist
        UpdateUserRequestDto updateUser = UpdateUserRequestDto.builder().job("Accounting Manager").name("Debi").build();
        webClient.put().uri(reqresProperties.getUsersApi() + idThatNotExist)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUser), UpdateUserRequestDto.class)
                .exchange()
                .expectStatus().isNotFound();

        // Updating by incorrect id
        UpdateUserRequestDto updateUserWithIncorrectId = UpdateUserRequestDto.builder().job("Graphic Designer").name("Anna").build();
        webClient.put().uri(reqresProperties.getUsersApi() + idWithIncorrectId)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateUserWithIncorrectId), UpdateUserRequestDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }



}
