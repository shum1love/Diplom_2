import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class TestCreateOrder {
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

    @Step("Получение списка ингредиентов")
    public List<String> getIngredients() {
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/api/ingredients");
        response.then()
                .statusCode(200)
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

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void testCreateOrderWithLogin() {
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User userLogin = new User("examplr.praktikum@gmail.com", "praktikum");

        registerUser(user).then().statusCode(200);
        token = loginUser(userLogin).then().statusCode(200)
                .extract().jsonPath().getString("accessToken");

        List<String> ingredients = getIngredients();
        String ingredientsJson = "{\"ingredients\": [\"" + ingredients.get(0) + "\"]}";

        createOrder(token, ingredientsJson).then().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCreateOrderWithoutLogin() {
        List<String> ingredients = getIngredients();
        String ingredientsJson = "{\"ingredients\": [\"" + ingredients.get(0) + "\"]}";

        createOrder(null, ingredientsJson).then().statusCode(401); // Нет авторизации
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с ингедиентами")
    public void testCreateOrderWithIngredients() {
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User userLogin = new User("examplr.praktikum@gmail.com", "praktikum");

        registerUser(user).then().statusCode(200);
        token = loginUser(userLogin).then().statusCode(200)
                .extract().jsonPath().getString("accessToken");

        List<String> ingredients = getIngredients();
        String ingredientsJson = "{\"ingredients\": [\"" + ingredients.get(1) + "\"]}";

        createOrder(token, ingredientsJson).then().statusCode(200);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, но без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User userLogin = new User("examplr.praktikum@gmail.com", "praktikum");

        registerUser(user).then().statusCode(200);
        token = loginUser(userLogin).then().statusCode(200)
                .extract().jsonPath().getString("accessToken");

        createOrder(token, "{\"ingredients\": []}").then()
                .statusCode(400)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, но с неправильным хешем ингедиентов")
    public void testCreateOrderWithWrongHash() {
        User user = new User("examplr.praktikum@gmail.com", "praktikum", "praktikum");
        User userLogin = new User("examplr.praktikum@gmail.com", "praktikum");

        registerUser(user).then().statusCode(200);
        token = loginUser(userLogin).then().statusCode(200)
                .extract().jsonPath().getString("accessToken");

        createOrder(token, "{\"ingredients\": [\"wrong_hash\"]}").then()
                .statusCode(500);
    }

    @After
    public void tearDown() {
        if (token != null) {
            given()
                    .header("Authorization", token)
                    .when()
                    .delete("/api/auth/user")
                    .then()
                    .statusCode(202);
        }
    }
}
