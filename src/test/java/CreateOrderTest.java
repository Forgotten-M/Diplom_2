import io.qameta.allure.junit4.DisplayName;

import io.restassured.response.Response;
import model.Order;
import steps.OrderSteps;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import model.User;
import steps.UserSteps;

import utils.Generator;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest extends BaseTest {

    private User user;
    private Order order;
    private String accessToken;
    private int ingredientSublistSize;
    private List<String> ingredients;
    private List<String> allIngredients;

    @Before
    public void setUp() {

        allIngredients = OrderSteps.getAllIngredients();
        user = Generator.generateUser();
        Response response = UserSteps.createUser(user);
        accessToken = response.then().extract().path("accessToken").toString();
    }

    @Test
    @DisplayName("Создание заказа с ингридиентами авторизованным пользователем")
    public void createCorrectOrderWithIngredientsAuthorizedUserGetSuccess() {
        ingredientSublistSize = Generator.generateSizeForIngredientSublist(allIngredients.size());
        ingredients = allIngredients.subList(0, ingredientSublistSize);
        order = new Order(ingredients);
        OrderSteps.createOrder(accessToken, order)
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("name", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингридиентов авторизованным пользователем")
    public void createOrderWithoutIngredientsAuthorizedUserGetError() {
        order = new Order(ingredients);
        OrderSteps.createOrder(accessToken, order)
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов авторизованным пользователем")
    public void createOrderInvalidIngredientAuthorizedUserGetSuccess() {
        ingredients = new ArrayList<>();
        ingredients.add(RandomStringUtils.randomAlphabetic(24));
        order = new Order(ingredients);
        OrderSteps.createOrder(accessToken, order)
                .then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа с ингридиентами неавторизованным пользователем")
    public void createOrderWithIngredientsUnauthorizedUserGetError() {
        ingredientSublistSize = Generator.generateSizeForIngredientSublist(allIngredients.size());
        ingredients = allIngredients.subList(0, ingredientSublistSize);
        order = new Order(ingredients);
        OrderSteps.createOrderWithoutAuth(order)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @After
    public void tearDown() {
        UserSteps.deleteUser(accessToken);
    }
}