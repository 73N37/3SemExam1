package dat.daos.impl;


import dat.daos.IDAO;
import dat.dtos.CandidateDTO;
import dat.entities.Candidate;
import dat.entities.Skill;
import dat.entities.SkillCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CandidateDAO implements IDAO<CandidateDTO, Integer> {

    private static CandidateDAO instance;
    private static EntityManagerFactory emf;

    public static CandidateDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CandidateDAO();
        }
        return instance;
    }

    @Override
    public CandidateDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Candidate candidate = em.find(Candidate.class, id);
            return candidate != null ? new CandidateDTO(candidate) : null;
        }
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
    public CandidateDTO update(Integer id, CandidateDTO candidateDTO) {
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
    public void delete(Integer id) {
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
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Candidate candidate = em.find(Candidate.class, id);
            return candidate != null;
        }
    }

    // US-3: Link skill to candidate
    public CandidateDTO addSkillToCandidate(Integer candidateId, Integer skillId) {
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

    // US-4: Filter candidates by skill category
    public List<CandidateDTO> getCandidatesBySkillCategory(SkillCategory category) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<CandidateDTO> query = em.createQuery(
                    "SELECT DISTINCT new dat.dtos.CandidateDTO(c) FROM Candidate c " +
                            "JOIN c.skills s WHERE s.category = :category",
                    CandidateDTO.class
            );
            query.setParameter("category", category);
            return query.getResultList();
        }
    }
}
