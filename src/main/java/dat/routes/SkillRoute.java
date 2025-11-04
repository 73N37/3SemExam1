package dat.routes;


import dat.controllers.impl.SkillController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class SkillRoute {

    private final SkillController skillController = new SkillController();

    protected EndpointGroup getRoutes() {

        return () ->
        {
            get("/populate", skillController::populate);
            post("/", skillController::create);
            get("/", skillController::readAll);
            get("/{id}", skillController::read);
            put("/{id}", skillController::update);
            delete("/{id}", skillController::delete);
        };
    }
}
