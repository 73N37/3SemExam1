package dat.controllers.impl;

import dat.dtos.CandidateDTO;
import dat.dtos.SkillDTO;
import dat.entities.Skill;
import dat.entities.SkillCategory;
import org.junit.Test;
import org.junit.jupiter.api.Order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class CandidateControllerTest {

    @Order(2)
    @Test
    public void testGetAllCandidates()
    {
        given()
                .contentType("application/json")
                .when()
                .get("/candidates")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Order(1)
    @Test
    public void testCreateCandidate()
    {
        CandidateDTO candidate = new CandidateDTO(  null,
                                                    "Merovingian",
                                                    "0045112",
                                                    "Computer Science",
                                                    java.util.Set.of(new Skill("Merovingian",
                                                                                "Merovingian",
                                                                                SkillCategory.DEV_OPS,
                                                                                "The best programmer ever 'born'")));
        given()
                .contentType("application/json")
                .body(candidate)
                .when()
                .post("/candidates")
                .then()
                .statusCode(201)
                .body("name", equalTo("Merovingian"));
    }

    @Order(3)
    @Test
    public void testAddSkillToCandidate() {
        given()
                .contentType("application/json")
                .when()
                .put("/candidates/1/skills/1")
                .then()
                .statusCode(200);
    }
}