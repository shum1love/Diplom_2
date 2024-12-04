import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
public class TestLoginUser {
    private String token;
    // Прописываем Before с главной ссылкой
    @Before
    public void setUp() {
        baseURI = "https://stellarburgers.nomoreparties.site";
    }
    // First Test
    @Test
    public void testLoginExistingUser() {

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
    }
    // Second test
    @Test
    public void testLoginRandomUser(){
    //
    String randomEmail = GenerateRandomString.generateRandomEmail();
    String randomPassword = GenerateRandomString.generateRandomPassword();
    User user1 = new User(randomEmail, randomPassword);
    Response response1 = given()
            .header("Content-Type", "application/json")
            .body(user1)
            .when()
            .post("/api/auth/login");
    // Проверяем статус-код
        response1.then()
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
