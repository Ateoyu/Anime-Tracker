//package pjatk.edu.pl.backend.integrationTest;
//
//import io.restassured.RestAssured;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import pjatk.edu.pl.data.model.Media;
//
//import static io.restassured.RestAssured.*;
//import static org.junit.jupiter.api.Assertions.*;
//
////@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class MediaControllerTest {
//
////    @LocalServerPort
////    private int port;
//
//    @BeforeEach
//    void setUp() {
////        RestAssured.port = port;
//        RestAssured.baseURI = "http://localhost:8081/getMedia/";
//    }
//
//    @Test
//    public void getMediaByIdTest() {
//        Media media = get("id/108092")
//                .then()
//                .statusCode(200)
//                .extract()
//                .as(Media.class);
//
//        assertEquals("Asteroid in Love", media.getTitle().getEnglish());
//    }
//
//    @Test
//    public void getMediaByIdNotFoundTest() {
//        get("id/-1")
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void getMediaByDateRangeTest() {
//        get("date/20200101/20200201")
//                .then()
//                .statusCode(200);
//    }
//
//
//
//}
