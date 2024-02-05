package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ScoreControllerRA {

    private Long movieExistingId, nonMovieExistingId;
    private Map<String, Object> saveInstance;
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

        saveInstance = new HashMap<>();
        saveInstance.put("movieId", 1);
        saveInstance.put("score", 4);

    }

    @Test
    public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {

        saveInstance.put("movieId", nonMovieExistingId);

        JSONObject newSave = new JSONObject(saveInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newSave)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(404);


    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {

        saveInstance.put("movieId", null);

        JSONObject newSave = new JSONObject(saveInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newSave)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(422)
                .body("errors.message[0]", equalTo("Campo requerido"));


    }

    @Test
    public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {

        saveInstance.put("score", -4);

        JSONObject newSave = new JSONObject(saveInstance);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newSave)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(422)
                .body("errors.message[0]", equalTo("Valor mínimo 0"));



    }
}
