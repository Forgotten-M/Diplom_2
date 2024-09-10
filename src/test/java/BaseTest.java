import io.restassured.RestAssured;
import org.junit.Before;
import static utils.Url.BASE_URL;


public class BaseTest {
    @Before
    public void setupRestAssured(){
        RestAssured.baseURI = BASE_URL;
    }
}


