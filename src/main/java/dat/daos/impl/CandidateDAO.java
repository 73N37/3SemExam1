package dat.daos.impl;


import dat.config.HibernateConfig;
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
public class CandidateDAO implements IDAO<CandidateDTO, Long> {

    private static CandidateDAO instance;
    private static EntityManagerFactory emf;

    public
    static
    CandidateDAO
    getInstance
            (
                    @org.jetbrains.annotations.NotNull
                    EntityManagerFactory _emf
            )
    {
        if (instance == null)
        {
            emf = _emf;
            instance = new CandidateDAO();
        }
        return instance;
    }

    @Override
    public
    CandidateDTO
    read
            (
                    @org.jetbrains.annotations.NotNull
                    Long id
            )
    {
        try (EntityManager em = emf.createEntityManager())
        {
            Candidate candidate = em.find(Candidate.class, id);
            return candidate != null ? new CandidateDTO(candidate) : null;
        }
    }

    @Override
    public
    List<CandidateDTO>
    readAll
            ()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            TypedQuery<CandidateDTO> query = em.createQuery(
                    "SELECT new dat.dtos.CandidateDTO(c) FROM Candidate c",
                    CandidateDTO.class
            );
            return query.getResultList();
        }
    }

    @Override
    public
    CandidateDTO
    create
            (
                    @org.jetbrains.annotations.NotNull
                    CandidateDTO candidateDTO
            )
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Candidate candidate = new Candidate(candidateDTO);
            em.persist(candidate);
            em.getTransaction().commit();
            return new CandidateDTO(candidate);
        }
    }

    @Override
    public
    CandidateDTO
    update
            (
                    @org.jetbrains.annotations.NotNull
                    Long id,

                    @org.jetbrains.annotations.NotNull
                    CandidateDTO candidateDTO)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Candidate candidate = em.find(Candidate.class, id);
            if (candidate != null)
            {
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
    public
    void
    delete
            (
                    @org.jetbrains.annotations.NotNull
                    Long id
            )
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Candidate candidate = em.find(Candidate.class, id);
            if (candidate != null)
            {
                em.remove(candidate);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public
    boolean
    validatePrimaryKey
            (
                    @org.jetbrains.annotations.NotNull
                    Long id
            )
    {
        try (EntityManager em = emf.createEntityManager())
        {
            Candidate candidate = em.find(Candidate.class, id);
            return candidate != null;
        }
    }

    // US-3: Link skill to candidate
    public
    CandidateDTO
    addSkillToCandidate
    (
            @org.jetbrains.annotations.NotNull
            Long candidateId,

            @org.jetbrains.annotations.NotNull
            Long skillId
    )
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Candidate candidate = em.find(Candidate.class, candidateId);
            Skill skill = em.find(Skill.class, skillId);
            if (candidate != null && skill != null)
            {
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
    public
    List<CandidateDTO>
    getCandidatesBySkillCategory
    (
            @org.jetbrains.annotations.NotNull
            SkillCategory category
    )
    {
        try (EntityManager em = emf.createEntityManager())
        {
            TypedQuery<CandidateDTO> query = em.createQuery(
                    "SELECT DISTINCT new dat.dtos.CandidateDTO(c) FROM Candidate c " +
                            "JOIN c.skills s WHERE s.category = :category",
                    CandidateDTO.class
            );
            query.setParameter("category", category);
            return query.getResultList();
        }
    }

    public
    boolean
    validateSkillId
            (
                    @org.jetbrains.annotations.NotNull
                    Long id
            )
    {
        return SkillDAO.getInstance(HibernateConfig.getEntityManagerFactory()).validatePrimaryKey(id);
    }


    public
    List<CandidateDTO>
    filterByCategory
            (
                    @org.jetbrains.annotations.NotNull
                    SkillCategory category
            )
    {
        try (EntityManager em = emf.createEntityManager())
        {
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


    public
    void
    populate
            ()
    {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Clear existing data
            em.createQuery("DELETE FROM Candidate").executeUpdate();

            // Create sample candidates
            Candidate c1 = new Candidate("John Doe", "+45 12345678", "Bachelor in Computer Science", java.util.Set.of(new Skill("Merovingian", "Merovingian", SkillCategory.DEV_OPS, "The best programmer ever 'born'")));
            Candidate c2 = new Candidate("Jane Smith", "+45 87654321", "Master in Software Engineering", java.util.Set.of(new Skill("Neo", "Neo", SkillCategory.DATA, "The chosen one")));
            Candidate c3 = new Candidate("Bob Johnson", "+45 11223344", "Bachelor in Data Science", java.util.Set.of(new Skill("Morpheus", "Morpheus", SkillCategory.DB, "The guide")));

            em.persist(c1);
            em.persist(c2);
            em.persist(c3);

            em.getTransaction().commit();
        }
    }
}
