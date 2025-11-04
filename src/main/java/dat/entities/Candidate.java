package dat.entities;

import dat.dtos.CandidateDTO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@Entity
@Table(name = "candidates")
public
class
Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @lombok.Setter
    private String name;

    @lombok.Setter
    private String phone;

    @lombok.Setter
    private String education;


    private double AveragePopularityScore;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "candidate_skill",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    protected Candidate(){}

    public
    Candidate
            (
                    java.lang.String name,
                    java.lang.String phone,
                    java.lang.String education,
                    java.util.Set<Skill> skills
            )
    {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.education = education;
        this.skills = skills;
    }


    public
    Candidate
            (
                    CandidateDTO dto
            )
    {
        if (dto.getId() != null) this.id = dto.getId();
        this.name = dto.getName();
        this.phone = dto.getPhone();
        this.education = dto.getEducation();
        this.skills = dto.getSkills();
    }

    public
    void
    addSkill
            (
                    dat.entities.Skill skill
            )
    {
        this.skills.add(skill);
    }

    public
    void
    setAveragePopularityScore
            (
                    @org.jetbrains.annotations.NotNull
                    double value
            )
    {
        this.AveragePopularityScore = value;
    }
}