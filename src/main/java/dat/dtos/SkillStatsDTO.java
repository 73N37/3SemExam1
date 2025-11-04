package dat.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkillStatsDTO {
    private String id;
    private String slug;
    private String name;

    @JsonProperty("categoryKey")
    private String categoryKey;

    private String description;

    @JsonProperty("popularityScore")
    private Long popularityScore;

    @JsonProperty("averageSalary")
    private Long averageSalary;

    @JsonProperty("updatedAt")
    private ZonedDateTime updatedAt;
}
