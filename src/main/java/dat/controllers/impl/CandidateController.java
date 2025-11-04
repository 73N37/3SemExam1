package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.CandidateDAO;
import dat.dtos.CandidateDTO;
import dat.entities.SkillCategory;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CandidateController implements IController<CandidateDTO, Long> {

    private final CandidateDAO dao;

    public CandidateController()
    {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = CandidateDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx)
    {
        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        CandidateDTO candidateDTO = dao.read(id);
        ctx.res().setStatus(200);
        ctx.json(candidateDTO, CandidateDTO.class);
    }

    @Override
    public void readAll(Context ctx)
    {
        List<CandidateDTO> candidateDTOs = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(candidateDTOs, CandidateDTO.class);
    }

    @Override
    public void create(Context ctx)
    {
        CandidateDTO jsonRequest = validateEntity(ctx);
        CandidateDTO candidateDTO = dao.create(jsonRequest);
        ctx.res().setStatus(201);
        ctx.json(candidateDTO, CandidateDTO.class);
    }

    @Override
    public void update(Context ctx)
    {
        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        CandidateDTO candidateDTO = dao.update(id, validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(candidateDTO, CandidateDTO.class);
    }

    @Override
    public void delete(Context ctx)
    {
        Long id = ctx.pathParamAsClass("id", Long.class)
                .check(this::validatePrimaryKey, "Not a valid id")
                .get();
        dao.delete(id);
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Long id)
    {
        return dao.validatePrimaryKey(id);
    }

    @Override
    public CandidateDTO validateEntity(Context ctx)
    {
        return ctx.bodyValidator(CandidateDTO.class)
                .check(c -> c.getName() != null && !c.getName().isEmpty(), "Name must be set")
                .check(c -> c.getPhone() != null && !c.getPhone().isEmpty(), "Phone must be set")
                .check(c -> c.getEducation() != null && !c.getEducation().isEmpty(), "Education must be set")
                .get();
    }

    public void addSkillToCandidate(Context ctx)
    {
        Long candidateId = ctx.pathParamAsClass("candidateId", Long.class)
                .check(this::validatePrimaryKey, "Not a valid candidate id")
                .get();
        Long skillId = ctx.pathParamAsClass("skillId", Long.class)
                .check(id -> dao.validateSkillId(id), "Not a valid skill id")
                .get();

        CandidateDTO candidateDTO = dao.addSkillToCandidate(candidateId, skillId);
        ctx.res().setStatus(200);
        ctx.json(candidateDTO, CandidateDTO.class);
    }

    // In CandidateController
    public void filterByCategory(Context ctx) {
        String category = ctx.queryParam("category");
        if (category == null) {
            readAll(ctx);
            return;
        }
        List<CandidateDTO> filtered = dao.filterByCategory(SkillCategory.valueOf(category.toUpperCase()));
        ctx.res().setStatus(200);
        ctx.json(filtered, CandidateDTO.class);
    }

    public void populate(Context ctx)
    {
        dao.populate();
        ctx.res().setStatus(200);
        ctx.json("{ \"message\": \"Candidates have been populate\" }");
    }
}
