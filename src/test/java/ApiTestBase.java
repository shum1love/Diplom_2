import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;

public class ApiTestBase {
    protected RequestSpecification requestSpecification;

    @Before
    public void setUp() {
        // Установка базового URL для API
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/"; // замените на ваш URL
        requestSpecification = RestAssured.given()
                .header("Content-Type", "application/json");
    }
}