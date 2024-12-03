import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class TestCreateUser {
    // Переменные класса
    private String token;
    // Прописываем Before с главной ссылкой
    @Before
    public void setUp(){
        baseURI = "https://stellarburgers.nomoreparties.site";
    }
    // First Test
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
    // Second Test
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
    //Third Test
    @Parameterized.Parameters
    public static Collection<Object[]> getTestData(){
        return UserData.getTestData();
    }
    @Test
    public void testRegisterUserWithInvalidData() {
        for (Object[] testData : getTestData()) {
            String email = (String) testData[0];
            String password = (String) testData[1];
            String name = (String) testData[2];

            // Данные пользователя
            User user = new User(email, password, name);

            // Отправляем запрос на регистрацию
            Response response = given()
                    .header("Content-Type", "application/json")
                    .body(user)
                    .when()
                    .post("/api/auth/register");

            // Проверяем, что запрос завершится ошибкой
            response.then()
                    .statusCode(403) // Или другой код ошибки
                    .body("success", equalTo(false));
        }
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
