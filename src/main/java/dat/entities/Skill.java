package dat.entities;

import dat.dtos.SkillDTO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@Table( name = "skill")
@Entity
public class Skill
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @lombok.Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private SkillCategory category;

    @lombok.Setter
    private String name;

    @lombok.Setter
    private String description;

    @lombok.Setter
    private String slug;

    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    private Set<Candidate> candidates = new HashSet<>();

    protected Skill (){}    // Required by JPA

    public
    Skill
            (
                    @org.jetbrains.annotations.NotNull
                    java.lang.String name,

                    @org.jetbrains.annotations.NotNull
                    java.lang.String slug,

                    @org.jetbrains.annotations.NotNull
                    dat.entities.SkillCategory category,

                    @org.jetbrains.annotations.NotNull
                    java.lang.String description

            )
    {
        this.name = name;
        this.slug = slug;
        this.category = category;
        this.description = description;
    }

    public
    Skill
            (
                    @org.jetbrains.annotations.NotNull
                    SkillDTO dto
            )
    {
        if (dto.getId() != null) this.id = dto.getId();
        this.name = dto.getName();
        this.slug = dto.getSlug();
        this.description = dto.getDescription();
        this.category = dto.getCategory();
    }

    public void addCandidate(Candidate candidate)
    {
        candidates.add(candidate);
        candidate.addSkill(this);
    }

}