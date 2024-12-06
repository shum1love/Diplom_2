import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;

public class TestLoginUser {
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

    @Step("Генерация случайного пользователя")
    public User generateRandomUser() {
        String randomEmail = GenerateRandomString.generateRandomEmail();
        String randomPassword = GenerateRandomString.generateRandomPassword();
        return new User(randomEmail, randomPassword);
    }

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void testLoginExistingUser() {
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User userForLogin = new User("examplr.praktikum@gmail.com", "praktikum");

        Response registerResponse = registerUser(user);
        registerResponse.then().statusCode(200);

        token = registerResponse.jsonPath().getString("accessToken");

        Response loginResponse = loginUser(userForLogin);
        loginResponse.then().statusCode(200);
    }

    @Test
    @DisplayName("Авторизация с неверным логином и паролем")
    public void testLoginRandomUser() {
        User randomUser = generateRandomUser();

        Response loginResponse = loginUser(randomUser);
        loginResponse.then().statusCode(401);
    }

    @After
    public void tearDown() {
        if (token != null) {
            deleteUser(token).then().statusCode(202);
        }
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user");
    }
}
