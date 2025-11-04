package dat.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.entities.Candidate;
import dat.entities.Skill;

import java.util.Set;

@lombok.Getter
public class CandidateDTO
{
    private java.lang.Long id;
    private java.lang.String name;
    private java.lang.String phone;
    private java.lang.String education;
    private java.util.Set<Skill> skills
            ;
    @JsonProperty("avgPopularityScore")
    private double avgPopularityScore;

    public CandidateDTO(){}

    public CandidateDTO
            (
                    java.lang.Long                      id,
                    java.lang.String                    name,
                    java.lang.String                    phone,
                    java.lang.String                    education,
                    java.util.Set<Skill>                skills
            )
    {
                    this.id                     =       id;
                    this.name                   =       name;
                    this.phone                  =       phone;
                    this.education              =       education;
                    this.skills                 =       skills;
    }


    public
    CandidateDTO
            (
                    Candidate                           entity
            )
    {
                    this.id                     =       entity.getId();
                    this.name                   =       entity.getName();
                    this.phone                  =       entity.getPhone();
                    this.education              =       entity.getEducation();
                    this.skills                 =       entity.getSkills();
    }


    @JsonCreator
    public CandidateDTO(
                    @JsonProperty("id")
                    Long                                id,

                    @JsonProperty("name")
                    String                              name,

                    @JsonProperty("phone")
                    String                              phone,

                    @JsonProperty("education")
                    String                              education,

                    @JsonProperty("skills")
                    Set<Skill>                          skills,

                    @JsonProperty("avgPopularityScore")
                    double                              avgPopularityScore
    )
    {
                    this.id                     =       id;
                    this.name                   =       name;
                    this.phone                  =       phone;
                    this.education              =       education;
                    this.skills                 =       skills;
                    this.avgPopularityScore     =       avgPopularityScore;
    }
}
