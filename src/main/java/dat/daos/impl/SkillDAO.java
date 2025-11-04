package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.SkillDTO;
import dat.entities.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SkillDAO implements IDAO<SkillDTO, Integer> {

    private static SkillDAO instance;
    private static EntityManagerFactory emf;

    public static SkillDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SkillDAO();
        }
        return instance;
    }

    @Override
    public SkillDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Skill skill = em.find(Skill.class, id);
            return skill != null ? new SkillDTO(skill) : null;
        }
    }

    @Override
    public List<SkillDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<SkillDTO> query = em.createQuery(
                    "SELECT new dat.dtos.SkillDTO(s) FROM Skill s",
                    SkillDTO.class
            );
            return query.getResultList();
        }
    }

    @Override
    public SkillDTO create(SkillDTO skillDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill skill = new Skill(skillDTO);
            em.persist(skill);
            em.getTransaction().commit();
            return new SkillDTO(skill);
        }
    }

    @Override
    public SkillDTO update(Integer id, SkillDTO skillDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill skill = em.find(Skill.class, id);
            if (skill != null) {
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
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill skill = em.find(Skill.class, id);
            if (skill != null) {
                em.remove(skill);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Skill skill = em.find(Skill.class, id);
            return skill != null;
        }
    }
}
