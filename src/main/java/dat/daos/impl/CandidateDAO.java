package dat.daos.impl;

import dat.Service.SkillStatsService;
import dat.config.HibernateConfig;
import dat.daos.IDAO;
import dat.dtos.CandidateDTO;
import dat.dtos.SkillStatsDTO;
import dat.entities.Candidate;
import dat.entities.Skill;
import dat.entities.SkillCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CandidateDAO implements IDAO<CandidateDTO, Long> {

    private static CandidateDAO instance;
    private static EntityManagerFactory emf;
    private final SkillStatsService skillStatsService = new SkillStatsService();

    public static CandidateDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CandidateDAO();
        }
        return instance;
    }

    @Override
    public CandidateDTO read(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Candidate candidate = em.find(Candidate.class, id);
            if (candidate == null) return null;

            CandidateDTO dto = new CandidateDTO(candidate);
            enrichWithStats(dto);
            return dto;
        }
    }

    private void enrichWithStats(CandidateDTO dto) {
        List<Skill> enrichedSkills = skillStatsService.enrichSkills(dto.getSkills());
        // The skills in dto are already updated by reference since enrichSkills modifies them
    }

    @Override
    public List<CandidateDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<CandidateDTO> query = em.createQuery(
                    "SELECT new dat.dtos.CandidateDTO(c) FROM Candidate c",
                    CandidateDTO.class
            );
            return query.getResultList();
        }
    }

    @Override
    public CandidateDTO create(CandidateDTO candidateDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate candidate = new Candidate(candidateDTO);
            em.persist(candidate);
            em.getTransaction().commit();
            return new CandidateDTO(candidate);
        }
    }

    @Override
    public CandidateDTO update(Long id, CandidateDTO candidateDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate candidate = em.find(Candidate.class, id);
            if (candidate != null) {
                candidate.setName(candidateDTO.getName());
                candidate.setPhone(candidateDTO.getPhone());
                candidate.setEducation(candidateDTO.getEducation());
                Candidate mergedCandidate = em.merge(candidate);
                em.getTransaction().commit();
                return new CandidateDTO(mergedCandidate);
            }
            em.getTransaction().rollback();
            return null;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate candidate = em.find(Candidate.class, id);
            if (candidate != null) {
                em.remove(candidate);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Candidate candidate = em.find(Candidate.class, id);
            return candidate != null;
        }
    }

    public CandidateDTO addSkillToCandidate(Long candidateId, Long skillId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate candidate = em.find(Candidate.class, candidateId);
            Skill skill = em.find(Skill.class, skillId);
            if (candidate != null && skill != null) {
                candidate.addSkill(skill);
                Candidate mergedCandidate = em.merge(candidate);
                em.getTransaction().commit();
                return new CandidateDTO(mergedCandidate);
            }
            em.getTransaction().rollback();
            return null;
        }
    }

    public List<CandidateDTO> filterByCategory(SkillCategory category) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Candidate> query = em.createQuery(
                    "SELECT DISTINCT c FROM Candidate c " +
                            "JOIN c.skills s " +
                            "WHERE s.category = :category",
                    Candidate.class
            );
            query.setParameter("category", category);

            List<Candidate> candidates = query.getResultList();
            return candidates.stream()
                    .map(CandidateDTO::new)
                    .toList();
        }
    }

    public CandidateDTO getTopCandidateByPopularity() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Candidate> query = em.createQuery(
                    "SELECT c FROM Candidate c LEFT JOIN c.skills s " +
                            "GROUP BY c.id ORDER BY AVG(s.popularityScore) DESC",
                    Candidate.class
            );
            query.setMaxResults(1);
            List<Candidate> results = query.getResultList();

            if (results.isEmpty()) return null;

            Candidate candidate = results.get(0);
            double avgPopularity = candidate.getSkills().stream()
                    .mapToLong(Skill::getPopularityScore)
                    .average()
                    .orElse(0.0);

            return new CandidateDTO(
                    candidate.getId(),
                    candidate.getName(),
                    candidate.getPhone(),
                    candidate.getEducation(),
                    candidate.getSkills(),
                    avgPopularity
            );
        }
    }

    public boolean validateSkillId(Long id) {
        return SkillDAO.getInstance(HibernateConfig.getEntityManagerFactory()).validatePrimaryKey(id);
    }

    public void populate() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            em.createQuery("DELETE FROM Candidate").executeUpdate();

            Candidate c1 = new Candidate("John Doe", "+45 12345678", "Bachelor in Computer Science",
                    java.util.Set.of(new Skill("Merovingian", "merovingian", SkillCategory.DEVOPS,
                            "The best programmer ever 'born'")));
            Candidate c2 = new Candidate("Jane Smith", "+45 87654321", "Master in Software Engineering",
                    java.util.Set.of(new Skill("Neo", "neo", SkillCategory.DATA, "The chosen one")));
            Candidate c3 = new Candidate("Bob Johnson", "+45 11223344", "Bachelor in Data Science",
                    java.util.Set.of(new Skill("Morpheus", "morpheus", SkillCategory.DB, "The guide")));

            em.persist(c1);
            em.persist(c2);
            em.persist(c3);

            em.getTransaction().commit();
        }
    }
}
