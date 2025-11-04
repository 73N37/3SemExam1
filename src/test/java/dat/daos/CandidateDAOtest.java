package dat.daos;

import dat.config.HibernateConfig;
import dat.daos.impl.CandidateDAO;
import dat.daos.impl.SkillDAO;
import dat.dtos.CandidateDTO;
import dat.dtos.SkillDTO;
import dat.entities.Candidate;
import dat.entities.Skill;
import dat.entities.SkillCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CandidateDAOTest {
    private static EntityManagerFactory emf;
    private static CandidateDAO candidateDAO;
    private static SkillDAO skillDAO;

    @BeforeAll
    static void setUpAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        candidateDAO = CandidateDAO.getInstance(emf);
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
    void testCreateCandidate() {
        CandidateDTO dto = new CandidateDTO(null, "John Doe", "12345678", "Computer Science", null);
        CandidateDTO created = candidateDAO.create(dto);

        assertNotNull(created.getId());
        assertEquals("John Doe", created.getName());
    }

    @Test
    @Order(2)
    void testReadCandidate() {
        CandidateDTO dto = new CandidateDTO(null, "Jane Smith", "87654321", "Engineering", null);
        CandidateDTO created = candidateDAO.create(dto);

        CandidateDTO found = candidateDAO.read(created.getId());
        assertNotNull(found);
        assertEquals(created.getId(), found.getId());
    }

    @Test
    @Order(3)
    void testUpdateCandidate()
    {
        CandidateDTO dto = new CandidateDTO(null, "Bob Jones", "11223344", "Mathematics", null);
        CandidateDTO created = candidateDAO.create(dto);

        String createdName = created.getName();
        CandidateDTO updated = candidateDAO.update(created.getId(), new CandidateDTO(null, "Robert Jones", "11223344", "Mathematics", null));

        assertEquals("Robert Jones", updated.getName());
    }

    @Test
    @Order(4)
    void testDeleteCandidate() {
        CandidateDTO dto = new CandidateDTO(null, "Alice Brown", "55667788", "Physics", null);
        CandidateDTO created = candidateDAO.create(dto);

        candidateDAO.delete(created.getId());
        assertNull(candidateDAO.read(created.getId()));
    }

    @Test
    @Order(5)
    void testGetAllCandidates() {
        candidateDAO.create(new CandidateDTO(null, "Candidate 1", "111", "CS", null));
        candidateDAO.create(new CandidateDTO(null, "Candidate 2", "222", "Math", null));

        List<CandidateDTO> all = candidateDAO.readAll();
        assertEquals(2, all.size());
    }

    @Test
    @Order(6)
    void testLinkCandidateToSkill() {
        Skill skill = new Skill("Java", "java", SkillCategory.PROG_LANG, "Programming language");
        SkillDTO skillDTO = skillDAO.create(new SkillDTO(skill));

        CandidateDTO candidate = candidateDAO.create(new CandidateDTO(null, "Developer", "99999", "IT", null));

        candidateDAO.addSkillToCandidate(candidate.getId(), skillDTO.getId());

        CandidateDTO updated = candidateDAO.read(candidate.getId());
        assertEquals(1, updated.getSkills().size());
    }
}
