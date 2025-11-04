package dat.Service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.dtos.SkillStatsDTO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;
public class SkillStatsService
{
    private static final String API_URL = "https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public
    SkillStatsService
            ()
    {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public
    List<SkillStatsDTO>
    getSkillStats
            (
                    java.util.List<String> slugs
            )
            throws Exception
    {
        String slugParam = String.join(",", slugs);
        String url = API_URL + "?slugs=" + slugParam;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        var jsonNode = objectMapper.readTree(response.body());
        var dataNode = jsonNode.get("data");

        return objectMapper.readValue(
                dataNode.toString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, SkillStatsDTO.class)
        );
    }
}
