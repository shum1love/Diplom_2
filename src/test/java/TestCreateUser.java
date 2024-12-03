import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestCreateUser {
    // Переменные класса
    private String token;

    // Прописываем Before с главной ссылкой
    @Before
    public void setUp() {
        baseURI = "https://stellarburgers.nomoreparties.site";
    }

    // First Test
    @Test
    public void testRegisterUserSuccessfully() {

        // Данные пользователя
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");

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

    // Second Test
    @Test
    public void testRegisterUserRepeat() {
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

        // Отправляем ручку второй раз с теми же тестовыми данными
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

    //Third Test
    @Test
    public void testRegisterUserWithoutParameter() {
        Collection<Object[]> testData = UserData.getTestData();

        for (Object[] data : testData) {
            String email = (String) data[0];
            String password = (String) data[1];
            String name = (String) data[2];

            User user = new User(email, password, name);

            Response response = given()
                    .header("Content-Type", "application/json")
                    .body(user)
                    //.log().all() // Строка для снятия логов теста и просмотра как прошёл каждый тестовый набор
                    .when()
                    .post("/api/auth/register");
            response.then()
                    .statusCode(403);
        }
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
