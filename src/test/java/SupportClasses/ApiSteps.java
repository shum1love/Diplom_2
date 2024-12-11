package SupportClasses;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class ApiSteps {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Step("Регистрация нового пользователя")
    public Response registerUser(User user) {
        return given()
                .log().all()
                .baseUri(BASE_URL)
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
                .baseUri(BASE_URL)
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
                .baseUri(BASE_URL)
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
    public void deleteUser(String token) {
        Response response = given()
                .log().all()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user");

        response.then().log().all();
        response.then().statusCode(202); // Код статуса на удаление
    }
}
