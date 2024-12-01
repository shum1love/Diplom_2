import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;

public class ApiTestBase {
    protected RequestSpecification requestSpecification;

    @Before
    public void setUp() {
        // Установка базового URL для API
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        requestSpecification = RestAssured.given()
                .header("Content-Type", "application/json");
    }
}