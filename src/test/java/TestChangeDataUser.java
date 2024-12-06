import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class TestChangeDataUser {
    private String token;

    @Before
    public void setUp() {
        baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Step("Регистрация нового пользователя")
    public Response registerUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/login");
    }

    @Step("Изменение данных пользователя")
    public Response updateUser(String token, UserEmailName updateData) {
        return given()
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body(updateData)
                .when()
                .patch("/api/auth/user");
    }

    @Step("Получение всех заказов")
    public OrderPojo getAllOrders() {
        return given()
                .header("Content-Type", "application/json")
                .get("/api/orders/all")
                .body()
                .as(OrderPojo.class);
    }

    @Step("Удаление пользователя")
    public void deleteUser(String token) {
        given()
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202);
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void testChangeDataUser() {
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User userForLogin = new User("examplr.praktikum@gmail.com", "praktikum");
        UserEmailName updateData = new UserEmailName("boris.borisovich@exaple.com", "Boris");

        Response registerResponse = registerUser(user);
        registerResponse.then().statusCode(200);
        token = registerResponse.jsonPath().getString("accessToken");

        Response loginResponse = loginUser(userForLogin);
        loginResponse.then().statusCode(200);

        Response updateResponse = updateUser(token, updateData);
        updateResponse.then().statusCode(200);

        OrderPojo orders = getAllOrders();
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void testChangeDataWithoutLogin() {
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        UserEmailName updateData = new UserEmailName("boris.borisovich@exaple.com", "Boris");

        Response registerResponse = registerUser(user);
        registerResponse.then().statusCode(200);
        token = registerResponse.jsonPath().getString("accessToken");

        Response updateResponse = updateUser(null, updateData);
        updateResponse.then().statusCode(401);
    }

    @After
    public void tearDown() {
        if (token != null) {
            deleteUser(token);
        }
    }
}
