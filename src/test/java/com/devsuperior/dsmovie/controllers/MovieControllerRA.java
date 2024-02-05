package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


public class MovieControllerRA {

    private Long movieExistingId, nonMovieExistingId;
    private Map<String, Object> insertMovieInstance;
    private String clientToken, adminToken, invalidToken;
    private String clientUsername, clientPassword, adminUserName, adminPassword;

    @BeforeEach
    public void setUp() throws JSONException {
        baseURI = "http://localhost:8080";

        movieExistingId = 1L;
        nonMovieExistingId = 100L;

        clientUsername = "lucia@gmail.com";
        clientPassword = "123456";
        adminUserName = "maria@gmail.com";
        adminPassword = "123456";

        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
        adminToken = TokenUtil.obtainAccessToken(adminUserName, adminPassword);
        invalidToken = adminToken + "JavaoDaMassa*-*";


        insertMovieInstance = new HashMap<>();
        insertMovieInstance.put("title", "Test Movie");
        insertMovieInstance.put("score", 0.0F);
        insertMovieInstance.put("count", 0);
        insertMovieInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");

    }

    @Test
    public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {

        given()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("content.id[0]", is(1))
                .body("content.title[0]", equalTo("The Witcher"))
                .body("content.score[0]", is(4.5F))
                .body("content.count[0]", is(2))
                .body("content.image[0]", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
    }

    @Test
    public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {

        given()
                .get("/movies?title=titanic")
                .then()
                .statusCode(200)
                .body("content.id[0]", is(7))
                .body("content.title[0]", equalTo("Titanic"))
                .body("content.score[0]", is(0.0F))
                .body("content.count[0]", is(0))
                .body("content.image[0]", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/yDI6D5ZQh67YU4r2ms8qcSbAviZ.jpg"));

    }

    @Test
    public void findByIdShouldReturnMovieWhenIdExists() {

        given()
                .get("/movies/{id}", movieExistingId)
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("title", equalTo("The Witcher"))
                .body("score", is(4.5F))
                .body("count", is(2))
                .body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));


    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {

        given()
                .get("/movies/{id}", nonMovieExistingId)
                .then()
                .statusCode(404);
    }

    @Test
    public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {

        insertMovieInstance.put("title", "     ");
        JSONObject newMovie = new JSONObject(insertMovieInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newMovie)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(422)
                .body("errors.message[0]", equalTo("Campo requerido"));

    }

    @Test
    public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {

        JSONObject newMovie = new JSONObject(insertMovieInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newMovie)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(403);


    }

    @Test
    public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {

        JSONObject newMovie = new JSONObject(insertMovieInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
                .body(newMovie)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(401);

    }
}
