package dat.controllers.impl;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.dtos.CandidateDTO;
import dat.dtos.SkillDTO;
import dat.entities.Skill;
import dat.entities.SkillCategory;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class CandidateControllerTest {


    private static EntityManagerFactory emf;
    private static Javalin app;
    private static final String BASE_URL = "http://localhost:7070/api";

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        app = ApplicationConfig.startServer(7070);
        RestAssured.baseURI = BASE_URL;
    }

    @AfterAll
    static void tearDownAll() {
        ApplicationConfig.stopServer(app);
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Candidate").executeUpdate();
            em.createQuery("DELETE FROM Skill").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @Order(1)
    @Test
    public void testCreateCandidate()
    {
        CandidateDTO dto = new CandidateDTO(
                null,
                "John Doe",
                "+45 12345678",
                "Bachelor in CS",
                java.util.Set.of()
        );

        given()
                .contentType("application/json")
                .body(dto)
                .when()
                .post("/candidates")
                .then()
                .statusCode(201)
                .body("name", equalTo("John Doe"))
                .body("phone", equalTo("+45 12345678"))
                .body("education", equalTo("Bachelor in CS"));
    }

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