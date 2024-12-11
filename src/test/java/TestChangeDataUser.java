import SupportClasses.User;
import SupportClasses.UserEmailName;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import SupportClasses.ApiSteps;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import org.apache.hc.core5.http.HttpStatus;

public class TestChangeDataUser {
    private String token;
    private ApiSteps apiSteps = new ApiSteps();

    /*@Before
    public void setUp() {
        baseURI = "https://stellarburgers.nomoreparties.site";
    }*/

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void testChangeDataUser() {
        User user = new User("bogdanexample17.praktikum@gmail.com", "Bogdan777", "Bogdan");
        User userForLogin = new User("bogdanexample17.praktikum@gmail.com", "Bogdan777");
        UserEmailName updateData = new UserEmailName("boris.borabora17@exaple.com", "Boris");

        Response registerResponse = apiSteps.registerUser(user);
        registerResponse.then().statusCode(HttpStatus.SC_OK);
        token = registerResponse.jsonPath().getString("accessToken");

        Response loginResponse = apiSteps.loginUser(userForLogin);
        loginResponse.then().statusCode(HttpStatus.SC_OK);

        Response updateResponse = apiSteps.updateUser(token, updateData);
        updateResponse.then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void testChangeDataWithoutLogin() {
        User user = new User("bogdanexamplerr13.praktikum@gmail.com", "Bogdan777", "Bogdan");
        UserEmailName updateData = new UserEmailName("boriss13.borisovich@exaple.com", "Boris");

        Response registerResponse = apiSteps.registerUser(user);
        registerResponse.then().statusCode(HttpStatus.SC_OK);
        token = registerResponse.jsonPath().getString("accessToken");

        Response updateResponse = apiSteps.updateUser("", updateData);
        updateResponse.then().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    /*@Step("Регистрация нового пользователя")
    public Response registerUser(User user) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register")
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/login")
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Изменение данных пользователя")
    public Response updateUser(String token, UserEmailName updateData) {
        return given()
                .log().all()
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body(updateData)
                .when()
                .patch("/api/auth/user")
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Удаление пользователя")
    private void deleteUser(String token) {
        Response response = given()
                .log().all()
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user");

        response.then().log().all();
        response.then().statusCode(HttpStatus.SC_ACCEPTED);
    }

     */

    @After
    public void tearDown() {
        if (token != null) {
            apiSteps.deleteUser(token);
        }
    }
}
