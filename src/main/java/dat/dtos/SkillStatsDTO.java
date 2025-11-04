package dat.dtos;

@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class SkillStatsDTO
{
    private String id;
    private String slug;
    private String name;
    private String categoryKey;
    private String description;
    private int popularityScore;
    private double averageSalary;
    private java.time.ZonedDateTime updatedAt;
}
