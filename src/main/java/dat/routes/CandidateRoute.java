package dat.routes;

import dat.controllers.impl.CandidateController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CandidateRoute
{
    private final CandidateController candidateController = new CandidateController();

    protected EndpointGroup getRoutes()
    {
        return () ->
        {
            get("/populate", candidateController::populate);
            get("/filter", candidateController::filterByCategory);
            post(candidateController::create);
            get(candidateController::readAll);
            get("/{id}", candidateController::read);
            put("/{id}", candidateController::update);
            delete("/{id}", candidateController::delete);
            put("/{candidateId}/skills/{skillId}", candidateController::addSkillToCandidate);
        };
    }
}
