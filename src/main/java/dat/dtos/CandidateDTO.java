package dat.dtos;

import dat.entities.Skill;

@lombok.Getter
public class CandidateDTO
{
    private java.lang.Long id;
    private java.lang.String name;
    private java.lang.String phone;
    private java.lang.String education;
    private java.util.Set<Skill> skills;

    public CandidateDTO
            (
                    java.lang.Long id,
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

}
