package dat.daos;

import dat.config.HibernateConfig;
import dat.entities.Skill;
import dat.entities.SkillCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkillDAOTest {
    private static EntityManagerFactory emf;
    private static SkillDAO skillDAO;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        skillDAO = SkillDAO.getInstance(emf);
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

    @AfterAll
    static void tearDownAll() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    void testCreateSkill() {
        Skill skill = new Skill("Python", "python", SkillCategory.PROG_LANG, "Programming language");
        Skill created = skillDAO.create(skill);

        assertNotNull(created.getId());
        assertEquals("Python", created.getName());
    }

    @Test
    void testReadSkill() {
        Skill skill = new Skill("Docker", "docker", SkillCategory.DEV_OPS, "Containerization");
        Skill created = skillDAO.create(skill);

        Skill found = skillDAO.read(created.getId());
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
    }

    @Test
    void testGetAllSkills() {
        skillDAO.create(new Skill("Java", "java", SkillCategory.PROG_LANG, "Language"));
        skillDAO.create(new Skill("React", "react", SkillCategory.FRONTEND, "Framework"));

        List<Skill> all = skillDAO.getAll();
        assertEquals(2, all.size());
    }
}
