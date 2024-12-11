import SupportClasses.User;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import org.apache.hc.core5.http.HttpStatus;

public class TestCreateOrder {
    private String token;

    @Before
    public void setUp() {
        baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void testCreateOrderWithLogin() {
        User user = new User("bogdanexample.praktikum1@gmail.com", "Bogdan777", "Bogdan");
        User userLogin = new User("bogdanexample.praktikum1@gmail.com", "Bogdan777");

        registerUser(user).then().statusCode(HttpStatus.SC_OK);
        token = loginUser(userLogin).then().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getString("accessToken");

        List<String> ingredients = getIngredients();
        String ingredientsJson = "{\"ingredients\": [\"" + ingredients.get(0) + "\"]}";

        createOrder(token, ingredientsJson).then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCreateOrderWithoutLogin() {
        List<String> ingredients = getIngredients();
        String ingredientsJson = "{\"ingredients\": [\"" + ingredients.get(0) + "\"]}";

        createOrder("", ingredientsJson).then().statusCode(HttpStatus.SC_UNAUTHORIZED); // Нет авторизации
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с ингедиентами")
    public void testCreateOrderWithIngredients() {
        User user = new User("bogdanexample2.praktikum@gmail.com", "Bogdan777", "Bogdan");
        User userLogin = new User("bogdanexample2.praktikum@gmail.com", "Bogdan777");

        registerUser(user).then().statusCode(HttpStatus.SC_OK);
        token = loginUser(userLogin).then().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getString("accessToken");

        List<String> ingredients = getIngredients();
        String ingredientsJson = "{\"ingredients\": [\"" + ingredients.get(1) + "\"]}";

        createOrder(token, ingredientsJson).then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, но без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        User user = new User("bogdanexample2.praktikum@gmail.com", "Bogdan777", "Bogdan");
        User userLogin = new User("bogdanexample2.praktikum@gmail.com", "Bogdan777");

        registerUser(user).then().statusCode(HttpStatus.SC_OK);
        token = loginUser(userLogin).then().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getString("accessToken");

        createOrder(token, "{\"ingredients\": []}").then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, но с неправильным хешем ингедиентов")
    public void testCreateOrderWithWrongHash() {
        User user = new User("bogdanexample2.praktikum@gmail.com", "Bogdan777", "Bogdan");
        User userLogin = new User("bogdanexample2.praktikum@gmail.com", "Bogdan777");

        registerUser(user).then().statusCode(HttpStatus.SC_OK);
        token = loginUser(userLogin).then().statusCode(HttpStatus.SC_OK)
                .extract().jsonPath().getString("accessToken");

        createOrder(token, "{\"ingredients\": [\"wrong_hash\"]}").then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        if (token != null) {
            given()
                    .header("Authorization", token)
                    .when()
                    .delete("/api/auth/user")
                    .then()
                    .statusCode(HttpStatus.SC_ACCEPTED);
        }
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

    @Step("Получение списка ингредиентов")
    public List<String> getIngredients() {
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/ingredients");
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true));
        return response.jsonPath().getList("data._id");
    }

    @Step("Создание заказа")
    public Response createOrder(String token, String ingredientsJson) {
        return given()
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body(ingredientsJson)
                .when()
                .post("/api/orders");
    }
}
