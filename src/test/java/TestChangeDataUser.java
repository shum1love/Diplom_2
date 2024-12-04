import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
public class TestChangeDataUser {
    private String token;
    // Прописываем Before с главной ссылкой
    @Before
    public void setUp() {
        baseURI = "https://stellarburgers.nomoreparties.site";
    }
    // First Test
    @Test
    public void testChangeDataUser() {

        // Данные пользователя
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User user1 = new User("examplr.praktikum@gmail.com", "praktikum");
        // Change Data
        UserEmailName updateData = new UserEmailName();
        updateData.setEmail("boris.borisovich@exaple.com");
        updateData.setName("Boris");

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
        // Проверка ручки Изменения данных
        Response response2 = given()
                .header("Authorization", token)
                .body(updateData)
                .when()
                .patch("/api/auth/user");
        // Проверяем статус-код
        response2.then()
                .statusCode(200);
    }
    // Second Test
    @Test
    public void testChangeDataWithoutLogin() {
        // Данные пользователя
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        // Change Data
        UserEmailName updateData = new UserEmailName();
        updateData.setEmail("boris.borisovich@exaple.com");
        updateData.setName("Boris");
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
        // Проверка ручки Изменения данных
        Response response2 = given()
                .header("Content-Type", "application/json")
                .body(updateData)
                .when()
                .patch("/api/auth/user");
        // Проверяем статус-код
        response2.then()
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
