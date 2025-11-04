package dat.entities;

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
    private SkillCategory category;

    @lombok.Setter
    private String name;

    @lombok.Setter
    private String description;

    @lombok.Setter
    private String slug;

    @ManyToMany(mappedBy = "skills")
    private Set<Candidate> candidates = new HashSet<>();

    protected Skill (){}    // Required by JPA

    public Skill
            (
                    @org.jetbrains.annotations.NotNull
                    java.lang.String name,

                    @org.jetbrains.annotations.NotNull
                    java.lang.String slug,

                    @org.jetbrains.annotations.NotNull
                    dat.entities.SkillCategory category,

                    java.lang.String description

            )
    {
        this.name = name;
        this.slug = slug;
        this.category = category;
        this.description = description;
    }

    public void addCandidate(Candidate candidate)
    {
        candidates.add(candidate);
        candidate.addSkill(this);
    }

}

enum SkillCategory
{
    PROG_LANG,
    DB,
    DEV_OPS,
    FRONTEND,
    TESTING,
    DATA,
    FRAMEWORK;
}