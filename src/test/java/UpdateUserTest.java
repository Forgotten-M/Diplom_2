import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.User;
import model.UserEdit;
import steps.UserSteps;

import static utils.Url.*;

import utils.Generator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UpdateUserTest {

    User user;
    UserEdit userEditedData;
    String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = Generator.generateUser();
        userEditedData = Generator.generateUserEditedData();
    }

    @Test
    @DisplayName("Изменение почты для авторизованного пользователя")
    public void editEmailAuthorizedUserGetSuccess() {
        Response responseCreating = UserSteps.createUser(user);
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        UserSteps.editAuthorizedUser(accessToken, userEditedData)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Изменение почты для неавторизованного пользователя")
    public void editEmailUnauthorizedUserGetError() {
        Response responseCreating = UserSteps.createUser(user);
        accessToken = responseCreating.then().extract().path("accessToken").toString();
        UserSteps.editUnauthorizedUser(accessToken, userEditedData)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        UserSteps.deleteUser(accessToken);
    }

}
