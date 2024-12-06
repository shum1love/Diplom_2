import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class TestGetOrderUser {
    private String token;
    // Прописываем Before с главной ссылкой
    @Before
    public void setUp() {
        baseURI = "https://stellarburgers.nomoreparties.site";
    }
    // First Test
    @Test
    public void testGetOrderUserWithLogin() {

        // Данные пользователя
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User user1 = new User("examplr.praktikum@gmail.com", "praktikum");

        // Отправляем запрос на регистрацию
        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
        // Проверяем статус-код
        response.then()
                .statusCode(200); // Регистрация должна вернуть 200

        // Сохраняем токен для удаления пользователя
        token = response.jsonPath().getString("accessToken");

        // Проверка ручки авторизации
        Response response1 = given()
                .header("Content-Type", "application/json")
                .body(user1)
                .when()
                .post("/api/auth/login");
        // Проверяем статус-код
        response1.then()
                .statusCode(200);

        // Проверка ручки получения закзов конкретного пользователя
        Response response3 = given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .when()
                .get("/api/orders");
        // Проверяем статус-код
        response3.then()
                .statusCode(200);
    }
    @Test
    public void testGetOrderUserWithoutLogin() {

        // Данные пользователя
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User user1 = new User("examplr.praktikum@gmail.com", "praktikum");

        // Отправляем запрос на регистрацию
        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
        // Проверяем статус-код
        response.then()
                .statusCode(200); // Регистрация должна вернуть 200

        // Сохраняем токен для удаления пользователя
        token = response.jsonPath().getString("accessToken");

        // Проверка ручки авторизации
        Response response1 = given()
                .header("Content-Type", "application/json")
                .body(user1)
                .when()
                .post("/api/auth/login");
        // Проверяем статус-код
        response1.then()
                .statusCode(200);

        // Проверка ручки получения закзов конкретного пользователя
        Response response3 = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/orders");
        // Проверяем статус-код
        response3.then()
                .statusCode(401);
    }
    @After
    public void tearDown(){
        if (token != null) {
            // Удаляем пользователя после теста
            given()
                    .header("Authorization", token)
                    .when()
                    .delete("/api/auth/user")
                    .then()
                    .statusCode(202);
        }
    }
}
