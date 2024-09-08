package steps;

import model.User;
import model.UserEdit;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static utils.Url.*;
import static io.restassured.RestAssured.given;


public class UserSteps {

    @Step("Регистрация пользователя")
    public static Response createUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER_ENDPOINT);
        return response;
    }

    @Step("Авторизация пользователя")
    public static Response logInUser(User user) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_ENDPOINT);
        return response;
    }

    @Step("Удаление пользователя")
    public static void deleteUser(String accessToken) {
        if (accessToken != null)
            given()
                    .header("Authorization", accessToken)
                    .when()
                    .delete(USER_ENDPOINT);
    }

    @Step("Изменение авторизованного пользователя")
    public static Response editAuthorizedUser(String accessToken, UserEdit userEditedData) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(userEditedData)
                .when()
                .patch(USER_ENDPOINT);
        return response;
    }

    @Step("Изменение неавторизованного пользователя")
    public static Response editUnauthorizedUser(String accessToken, UserEdit userEditedData) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(userEditedData)
                .when()
                .patch(USER_ENDPOINT);
        return response;
    }
}