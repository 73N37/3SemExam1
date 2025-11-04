package dat.daos;

import dat.config.HibernateConfig;
import dat.daos.impl.SkillDAO;
import dat.dtos.SkillDTO;
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
    @Order(1)
    void testCreateSkill() {
        Skill skill = new Skill("Java", "java", SkillCategory.PROG_LANG, "Programming language");
        SkillDTO dto = new SkillDTO(skill);
        SkillDTO created = skillDAO.create(dto);

        assertNotNull(created.getId());
        assertEquals("Java", created.getName());
        assertEquals("java", created.getSlug());
        assertEquals(SkillCategory.PROG_LANG, created.getCategory());
    }

    @Test
    @Order(2)
    void testReadSkill() {
        Skill skill = new Skill("Python", "python", SkillCategory.PROG_LANG, "Scripting language");
        SkillDTO dto = new SkillDTO(skill);
        SkillDTO created = skillDAO.create(dto);

        SkillDTO found = skillDAO.read(created.getId());
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
        assertEquals("Python", found.getName());
    }

    @Test
    @Order(3)
    void testUpdateSkill() {
        Skill skill = new Skill("PostgreSQL", "postgresql", SkillCategory.DB, "Database");
        SkillDTO dto = new SkillDTO(skill);
        SkillDTO created = skillDAO.create(dto);

        SkillDTO updateDto = new SkillDTO(created.getId(), skill.getName(), "PostgreSQL Advanced"
                , "Advanced database management", skill.getCategory());
        SkillDTO updated = skillDAO.update(created.getId(), updateDto);

        assertEquals("PostgreSQL Advanced", updated.getDescription());
        assertEquals(created.getId(), updated.getId());
    }

    @Test
    @Order(4)
    void testDeleteSkill() {
        Skill skill = new Skill("MongoDB", "mongodb", SkillCategory.DB, "NoSQL database");
        SkillDTO dto = new SkillDTO(skill);
        SkillDTO created = skillDAO.create(dto);

        skillDAO.delete(created.getId());
        assertNull(skillDAO.read(created.getId()));
    }

    @Test
    @Order(5)
    void testGetAllSkills() {
        skillDAO.create(new SkillDTO(new Skill("Java", "java", SkillCategory.PROG_LANG, "Programming language")));
        skillDAO.create(new SkillDTO(new Skill("Docker", "docker", SkillCategory.DEVOPS, "Containerization")));
        skillDAO.create(new SkillDTO(new Skill("React", "react", SkillCategory.FRONTEND, "UI library")));

        List<SkillDTO> all = skillDAO.readAll();
        assertEquals(3, all.size());
    }

    @Test
    @Order(6)
    void testReadSkillsByCategory() {
        skillDAO.create(new SkillDTO(new Skill("Java", "java", SkillCategory.PROG_LANG, "Programming")));
        skillDAO.create(new SkillDTO(new Skill("Python", "python", SkillCategory.PROG_LANG, "Programming")));
        skillDAO.create(new SkillDTO(new Skill("PostgreSQL", "postgresql", SkillCategory.DB, "Database")));

        List<SkillDTO> progLangSkills = skillDAO.readByCategory(SkillCategory.PROG_LANG);
        assertEquals(2, progLangSkills.size());
        assertTrue(progLangSkills.stream().allMatch(s -> s.getCategory() == SkillCategory.PROG_LANG));
    }

    @Test
    @Order(7)
    void testReadSkillBySlug() {
        Skill skill = new Skill("Spring Boot", "spring-boot", SkillCategory.FRAMEWORK, "Java framework");
        SkillDTO created = skillDAO.create(new SkillDTO(skill));

        List<SkillDTO> found = skillDAO.readBySlug("spring-boot");
        assertNotNull(found);
        assertEquals("Spring Boot", found.get(0).getName());
        assertEquals(created.getId(), found.get(0).getId());
    }

    @Test
    @Order(8)
    void testReadNonExistentSkill() {
        SkillDTO notFound = skillDAO.read(99999L);
        assertNull(notFound);
    }
}
