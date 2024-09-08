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

public class LoginUserTest {

    User user;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = Generator.generateUser();
    }

    @Test
    @DisplayName("Успешная авторизация")
    public void loginGetSuccess() {
        Response responseCreating = UserSteps.createUser(user);
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        Response responseLogin = UserSteps.logInUser(user);
        responseLogin.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue())
                .and()
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void loginWithIncorrectPassword() {
        UserSteps.createUser(user);
        User incorrectUser = new User(user.getEmail(), RandomStringUtils.randomAlphabetic(10), user.getName());
        UserSteps.logInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверной почтой")
    public void loginWithIncorrectEmail() {
        UserSteps.createUser(user);
        User incorrectUser = new User(RandomStringUtils.randomAlphabetic(10) + "@gmail.com", user.getPassword(), user.getName());
        UserSteps.logInUser(incorrectUser)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void tearDown() {
        UserSteps.deleteUser(accessToken);
    }

}