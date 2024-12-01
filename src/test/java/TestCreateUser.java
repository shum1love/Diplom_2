import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class TestCreateUser {
    private String bearerToken;
    CreateUserData createUserData = new CreateUserData("bogomol3000@yandex.ru", "1234567890", "Bogdan");
    // оздаём уникального пользователя
    @Test
    public void createUniqueUser(){
        Response response = given()
                //.header("Content-type", "application/json")
                .and()
                .body(createUserData)
                .when()
                .post("/api/auth/register")
                .then().statusCode(201)
                .extract()
                .response();
        bearerToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
    }
    @After
    public void tearDown(){
        given()
                .headers("Content-type", "application/json", "Authorization", "Bearer" + bearerToken)
                .when()
                .delete()
                .then()
                .statusCode(202);
    }
}
