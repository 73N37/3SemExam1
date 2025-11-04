package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.SkillDTO;
import dat.entities.Skill;
import dat.entities.SkillCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SkillDAO implements IDAO<SkillDTO, Long> {

    private static SkillDAO instance;
    private static EntityManagerFactory emf;

    public
    static
    SkillDAO
    getInstance
            (
                    @org.jetbrains.annotations.NotNull
                    EntityManagerFactory _emf
            )
    {
        if (instance == null)
        {
            emf = _emf;
            instance = new SkillDAO();
        }
        return instance;
    }

    @Override
    public
    SkillDTO
    read
            (
                    @org.jetbrains.annotations.NotNull
                    Long id
            )
    {
        try (EntityManager em = emf.createEntityManager()) {
            Skill skill = em.find(Skill.class, id);
            return skill != null ? new SkillDTO(skill) : null;
        }
    }




    @Override
    public
    List<SkillDTO>
    readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            TypedQuery<SkillDTO> query = em.createQuery(
                    "SELECT new dat.dtos.SkillDTO(s) FROM Skill s",
                    SkillDTO.class
            );
            return query.getResultList();
        }
    }



    public
    List<SkillDTO>
    readByCategory
            (
                    @org.jetbrains.annotations.NotNull
                    SkillCategory category
            )
    {
        try (EntityManager em = emf.createEntityManager())
        {
            TypedQuery<SkillDTO> query = em.createQuery
                    (
                    "SELECT new dat.dtos.SkillDTO(s) FROM Skill s WHERE s.category = :category",
                    SkillDTO.class
                    );
            query.setParameter("category", category);
            return query.getResultList();
        }
    }


    public
    List<SkillDTO>
    readBySlug
            (
                    @org.jetbrains.annotations.NotNull
                    java.lang.String slug
            )
    {
        List<SkillDTO> all = readAll();
        if (all == null || all.isEmpty()) return List.of();
        return all.stream()
                .filter(index -> slug == null ? index.getSlug() == null : slug.equals(index.getSlug()))
                .toList();
    }



    @Override
    public
    SkillDTO
    create
            (
                    @org.jetbrains.annotations.NotNull
                    SkillDTO skillDTO
            )
    {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill skill = new Skill(skillDTO);
            em.persist(skill);
            em.getTransaction().commit();
            return new SkillDTO(skill);
        }
    }

    @Override
    public
    SkillDTO
    update
            (
                    @org.jetbrains.annotations.NotNull
                    Long id,

                    @org.jetbrains.annotations.NotNull
                    SkillDTO skillDTO
            )
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Skill skill = em.find(Skill.class, id);
            if (skill != null)
            {
                skill.setName(skillDTO.getName());
                skill.setCategory(skillDTO.getCategory());
                skill.setDescription(skillDTO.getDescription());
                skill.setSlug(skillDTO.getSlug());
                Skill mergedSkill = em.merge(skill);
                em.getTransaction().commit();
                return new SkillDTO(mergedSkill);
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
            Skill skill = em.find(Skill.class, id);
            if (skill != null)
            {
                em.remove(skill);
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
            Skill skill = em.find(Skill.class, id);
            return skill != null;
        }
    }
}
