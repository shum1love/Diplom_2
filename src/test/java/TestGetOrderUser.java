import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class TestGetOrderUser {
    private String token;

    @Before
    public void setUp() {
        baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Получение данных заказа конкретного пользователя с авторизацией")
    public void testGetOrderUserWithLogin() {
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User user1 = new User("examplr.praktikum@gmail.com", "praktikum");

        registerUser(user);
        loginUser(user1);
        getUserOrdersWithToken();
    }

    @Test
    @DisplayName("Получение данных заказа конкретного пользователя без авторизации")
    public void testGetOrderUserWithoutLogin() {
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");

        registerUser(user);
        getUserOrdersWithoutToken();
    }

    @After
    public void tearDown() {
        if (token != null) {
            deleteUser();
        }
    }

    @Step("Регистрируем пользователя: {user}")
    private void registerUser(User user) {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");

        response.then().statusCode(200);
        token = response.jsonPath().getString("accessToken");
    }

    @Step("Авторизуем пользователя: {user}")
    private void loginUser(User user) {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/login");

        response.then().statusCode(200);
    }

    @Step("Получаем заказы пользователя с токеном")
    private void getUserOrdersWithToken() {
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .when()
                .get("/api/orders");

        response.then().statusCode(200);
    }

    @Step("Получаем заказы пользователя без токена")
    private void getUserOrdersWithoutToken() {
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/orders");

        response.then().statusCode(401);
    }

    @Step("Удаляем пользователя")
    private void deleteUser() {
        given()
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202);
    }
}
