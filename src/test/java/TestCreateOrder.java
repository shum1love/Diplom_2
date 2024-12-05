// Создание пользователя
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestCreateOrder {
    private String token;

    // Прописываем Before с главной ссылкой
    @Before
    public void setUp() {
        baseURI = "https://stellarburgers.nomoreparties.site";
    }

    // First Test С авторизацией
    @Test
    public void testCreateOrderWithLogin() {

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
        // Получение ингредиентов
        Response ingredientsResponse = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/ingredients");
        ingredientsResponse.then().statusCode(200).body("success", equalTo(true));

        List<String> ingredients = ingredientsResponse.jsonPath().getList("data._id");

        //Create Order
        Response response2 = given()
                .header("Content-Type", "application/json")
                .body("{\"ingredients\": [\"" + ingredients.get(0) + "\"]}")
                .when()
                .post("/api/orders");
        // Проверяем статус-код
        response2.then()
                .statusCode(200);
    }
    // Second Test Без авторизацией с ингредиентами
    @Test
    public void testCreateOrderWithoutLogin() {
        // Получение ингредиентов
        Response ingredientsResponse = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/ingredients");
        ingredientsResponse.then()
                .statusCode(400)
                .body("success", equalTo(false));

        List<String> ingredients = ingredientsResponse.jsonPath().getList("data._id");

        //Create Order
        Response response2 = given()
                .header("Content-Type", "application/json")
                .body("{\"ingredients\": [\"" + ingredients.get(0) + "\"]}")
                .when()
                .post("/api/orders");
        // Проверяем статус-код
        response2.then()
                .statusCode(200);
    }

    // Third Test С авторизацией с ингредиентами
    @Test
    public void testCreateOrderWithIngredients() {

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
        // Получение ингредиентов
        Response ingredientsResponse = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/ingredients");
        ingredientsResponse.then().statusCode(200).body("success", equalTo(true));

        List<String> ingredients = ingredientsResponse.jsonPath().getList("data._id");

        //Create Order
        Response response2 = given()
                .header("Content-Type", "application/json")
                .body("{\"ingredients\": [\"" + ingredients.get(2) + "\"]}")
                .when()
                .post("/api/orders");
        // Проверяем статус-код
        response2.then()
                .statusCode(200);
    }
    // Fourth Test С авторизацией без ингредиентов
    @Test
    public void testCreateOrderWithotIngredients() {

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
        //Create Order
        Response response2 = given()
                .header("Content-Type", "application/json")
                .when()
                .post("/api/orders");
        // Проверяем статус-код
        response2.then()
                .statusCode(400)
                .body("success", equalTo(false));
    }
    // Fifth Test С авторизацией c неверным хешем
    @Test
    public void testCreateOrderWithWrongHash() {

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

        //Create Order
        Response response2 = given()
                .header("Content-Type", "application/json")
                .body("{\"ingredients\": [\"123456789example12345678\"]}")
                .when()
                .post("/api/orders");
        // Проверяем статус-код
        response2.then()
                .statusCode(500);
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
