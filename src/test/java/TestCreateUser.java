import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestCreateUser {
    // Переменные класса
    private String token;
    // Прописываем Before с главной ссылкой
    @Before
    public void setUp(){
        baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    public void testRegisterUserSuccessfully() {

        // Данные пользователя
        User user = new User("exampler.praktikum@gmail.com", "praktikum", "praktikum");

        // Отправляем запрос на регистрацию
        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");

        // Проверяем статус-код и тело ответа
        response.then()
                .statusCode(200) // Регистрация должна вернуть 200
                .body("success", equalTo(true));

        // Сохраняем токен для удаления пользователя
        token = response.jsonPath().getString("accessToken");
    }
    @Test
    public void testRegisterUserRepeat(){
        // Данные пользователя
        User user = new User("examplerrr.praktikum@gmail.com", "praktikum", "praktikum");
        // Отправляем запрос на регистрацию
        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
                // Проверяем статус-код
                response.then().statusCode(200);

        //
        Response response1 = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
        // Проверяем статус-код
        response1.then().statusCode(403);
        // Сохраняем токен для удаления пользователя
        token = response.jsonPath().getString("accessToken");
    }


    @After
    public void tearDown() {
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
