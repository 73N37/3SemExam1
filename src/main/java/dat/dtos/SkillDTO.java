package dat.dtos;

import dat.entities.Candidate;
import dat.entities.Skill;
import dat.entities.SkillCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

@lombok.Getter
public class SkillDTO
{
    private Long id;

    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    private String name;

    private String description;

    private String slug;

    private Set<Candidate> candidates = new HashSet<>();

    public
    SkillDTO
            (
                    java.lang.Long id,

                    @org.jetbrains.annotations.NotNull
                    java.lang.String name,

                    @org.jetbrains.annotations.NotNull
                    java.lang.String description,

                    @org.jetbrains.annotations.NotNull
                    java.lang.String slug,

                    java.util.Set<Candidate> candidates
            )
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.candidates = candidates;
    }

    public
    SkillDTO
            (
                    @org.jetbrains.annotations.NotNull
                    Skill entity
            )
    {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.slug = entity.getSlug();
        this.candidates = entity.getCandidates();
    }
}
