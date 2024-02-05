package com.devsuperior.dsmovie.controllers;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MovieControllerRA {

    private Long movieExistingId, nonMovieExistingId;

    @BeforeEach
    void setUp() {
        baseURI = "http://localhost:8080";

        movieExistingId = 1L;
        nonMovieExistingId = 100L;

    }

    @Test
    public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {

        given()
                .get("/movies")
                .then()
                .statusCode(200)
                .body("content.id[0]", is(1))
                .body("content.title[0]", equalTo("The Witcher"))
                .body("content.score[0]", is(4.33F))
                .body("content.count[0]", is(3))
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
                .body("score", is(4.33F))
                .body("count", is(3))
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
    }

    @Test
    public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
    }

    @Test
    public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
    }
}
