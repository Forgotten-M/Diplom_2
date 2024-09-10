import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import steps.OrderSteps;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.User;
import steps.UserSteps;

import utils.Generator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class GetUserOrdersTest extends BaseTest {

    User user;
    String accessToken;

    @Before
    public void setUp() {
        user = Generator.generateUser();
    }

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    public void getOrdersAuthorizedUserGetSuccess() {
        Response response = UserSteps.createUser(user);
        accessToken = response.then().extract().path("accessToken").toString();
        OrderSteps.getOrdersAuthorizedUser(accessToken)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void getOrdersUnauthorizedUserGetError() {
        Response response = UserSteps.createUser(user);
        accessToken = response.then().extract().path("accessToken").toString();
        OrderSteps.getOrdersUnauthorizedUser()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        UserSteps.deleteUser(accessToken);
    }

}