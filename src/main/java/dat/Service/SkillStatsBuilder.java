package dat.builders;

import dat.dtos.SkillStatsDTO;

import java.time.ZonedDateTime;

public class SkillStatsBuilder {
    private String id;
    private String slug;
    private String name;
    private String categoryKey;
    private String description;
    private Long popularityScore;
    private Long averageSalary;
    private ZonedDateTime updatedAt;

    public SkillStatsBuilder() {}

    public SkillStatsBuilder(SkillStatsDTO dto) {
        this.id = dto.getId();
        this.slug = dto.getSlug();
        this.name = dto.getName();
        this.categoryKey = dto.getCategoryKey();
        this.description = dto.getDescription();
        this.popularityScore = dto.getPopularityScore();
        this.averageSalary = dto.getAverageSalary();
        this.updatedAt = dto.getUpdatedAt();
    }

    public SkillStatsBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public SkillStatsBuilder setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public SkillStatsBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SkillStatsBuilder setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
        return this;
    }

    public SkillStatsBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public SkillStatsBuilder setPopularityScore(Long popularityScore) {
        this.popularityScore = popularityScore;
        return this;
    }

    public SkillStatsBuilder setAverageSalary(Long averageSalary) {
        this.averageSalary = averageSalary;
        return this;
    }

    public SkillStatsBuilder setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public SkillStatsDTO build() {
        return new SkillStatsDTO(id, slug, name, categoryKey, description,
                popularityScore, averageSalary, updatedAt);
    }
}
