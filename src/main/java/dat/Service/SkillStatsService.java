package dat.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.dtos.SkillStatsDTO;
import dat.entities.Skill;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SkillStatsService {
    private static final String API_URL = "https://api.example.com/tech-skills";
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public SkillStatsService() {
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public List<SkillStatsDTO> getSkillStats(List<String> slugs) {
        if (slugs == null || slugs.isEmpty()) {
            return List.of();
        }

        try {
            String slugParams = slugs.stream()
                    .map(slug -> "slug=" + slug)
                    .collect(Collectors.joining("&"));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "?" + slugParams))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, Object> responseMap = objectMapper.readValue(response.body(), Map.class);
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) responseMap.get("data");

                List<SkillStatsDTO> stats = new ArrayList<>();
                for (Map<String, Object> data : dataList) {
                    SkillStatsDTO dto = objectMapper.convertValue(data, SkillStatsDTO.class);
                    stats.add(dto);
                }
                return stats;
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch skill stats: " + e.getMessage());
        }
        return List.of();
    }

    public List<Skill> enrichSkills(java.util.Set<Skill> skills) {
        if (skills == null || skills.isEmpty()) {
            return List.of();
        }

        List<String> slugs = skills.stream()
                .map(Skill::getSlug)
                .collect(Collectors.toList());

        List<SkillStatsDTO> stats = getSkillStats(slugs);
        Map<String, SkillStatsDTO> statsBySlug = stats.stream()
                .collect(Collectors.toMap(SkillStatsDTO::getSlug, dto -> dto));

        List<Skill> enrichedSkills = new ArrayList<>();
        for (Skill skill : skills) {
            SkillStatsDTO statsDTO = statsBySlug.get(skill.getSlug());
            if (statsDTO != null) {
                skill.setPopularityScore(statsDTO.getPopularityScore() != null ?
                        statsDTO.getPopularityScore().longValue() : null);
                skill.setAverageSalary(statsDTO.getAverageSalary() != null ?
                        statsDTO.getAverageSalary().longValue() : null);
            }
            enrichedSkills.add(skill);
        }
        return enrichedSkills;
    }
}
