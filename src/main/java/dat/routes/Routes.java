package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes
{

    private final SkillRoute        skillRoute      = new SkillRoute();
    private final CandidateRoute    candidateRoute  = new CandidateRoute();


    public EndpointGroup getRoutes()
    {
        return () ->
        {
                path("/skills",     skillRoute.getRoutes());
                path("/candidates",  candidateRoute.getRoutes());
        };
    }
}
