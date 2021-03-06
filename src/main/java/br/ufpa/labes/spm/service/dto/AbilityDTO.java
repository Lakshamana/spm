package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Ability} entity.
 */
public class AbilityDTO implements Serializable {

    private Long id;

    private String ident;

    private String name;

    @Lob
    private String description;


    private Long theAbilityTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTheAbilityTypeId() {
        return theAbilityTypeId;
    }

    public void setTheAbilityTypeId(Long abilityTypeId) {
        this.theAbilityTypeId = abilityTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbilityDTO abilityDTO = (AbilityDTO) o;
        if (abilityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), abilityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AbilityDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", theAbilityType=" + getTheAbilityTypeId() +
            "}";
    }
}
