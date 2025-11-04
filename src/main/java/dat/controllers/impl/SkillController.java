package dat.controllers.impl;


import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.SkillDAO;
import dat.dtos.SkillDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SkillController implements IController<SkillDTO, Long> {

    private final SkillDAO dao;

    public SkillController()
    {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = SkillDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx)
    {
        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        SkillDTO skillDTO = dao.read(Long.valueOf(id));
        ctx.res().setStatus(200);
        ctx.json(skillDTO, SkillDTO.class);
    }

    @Override
    public void readAll(Context ctx)
    {
        List<SkillDTO> skillDTOs = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(skillDTOs, SkillDTO.class);
    }

    @Override
    public void create(Context ctx)
    {
        SkillDTO jsonRequest = validateEntity(ctx);
        SkillDTO skillDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(skillDTO, SkillDTO.class);
    }

    @Override
    public void update(Context ctx)
    {
        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        SkillDTO skillDTO = dao.update(Long.valueOf(id), validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(skillDTO, SkillDTO.class);
    }

    @Override
    public void delete(Context ctx)
    {
        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        dao.delete(Long.valueOf(id));
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Long id)
    {
        return dao.validatePrimaryKey(id);
    }

    @Override
    public SkillDTO validateEntity(Context ctx)
    {
        return ctx.bodyValidator(SkillDTO.class)
                .check(s -> s.getName() != null && !s.getName().isEmpty(), "Skill name must be set")
                .check(s -> s.getCategory() != null, "Skill category must be set")
                .check(s -> s.getDescription() != null && !s.getDescription().isEmpty(), "Skill description must be set")
                .get();
    }

    public void populate(Context ctx)
    {
        dao.populate();
        ctx.res().setStatus(200);
        ctx.json("{ \"message\": \"Candidates have been populate\" }");
    }
}