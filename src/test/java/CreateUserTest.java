import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.User;
import steps.UserSteps;

import static utils.Url.*;

import utils.Generator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest {

    User user;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = Generator.generateUser();
    }

    @Test
    @DisplayName("Регистрация пользователя успешная")
    public void createNewUserGetSuccess() {
        Response response = UserSteps.createUser(user);
        accessToken = response.then().extract().path("accessToken").toString();
        response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Регистрация неуникального пользователя")
    public void createTwoSimilarUsersGetError() {
        Response response = UserSteps.createUser(user);
        accessToken = response.then().extract().path("accessToken").toString();
        UserSteps.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Регистрация пользователя без почты")
    public void createUserWithoutEmailGetError() {
        String password = RandomStringUtils.randomAlphabetic(8);
        String name = RandomStringUtils.randomAlphabetic(8);
        user = new User(password, name);
        UserSteps.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация пользователя без пароля")
    public void createUserWithoutPasswordGetError() {
        String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
        String name = RandomStringUtils.randomAlphabetic(8);
        user = new User(email, name);
        UserSteps.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Регистрация пользователя без имени")
    public void createUserWithoutNameGetError() {
        String email = RandomStringUtils.randomAlphabetic(8) + "@gmail.com";
        String password = RandomStringUtils.randomAlphabetic(8);
        user = new User(email, password);
        UserSteps.createUser(user)
                .then().assertThat().statusCode(HttpStatus.SC_FORBIDDEN)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        UserSteps.deleteUser(accessToken);
    }
}