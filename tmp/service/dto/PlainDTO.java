package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Plain} entity.
 */
public class PlainDTO implements Serializable {

    private Long id;

    @Lob
    private String requirements;


    private Long theEnactionDescriptionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Long getTheEnactionDescriptionId() {
        return theEnactionDescriptionId;
    }

    public void setTheEnactionDescriptionId(Long enactionDescriptionId) {
        this.theEnactionDescriptionId = enactionDescriptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlainDTO plainDTO = (PlainDTO) o;
        if (plainDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), plainDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlainDTO{" +
            "id=" + getId() +
            ", requirements='" + getRequirements() + "'" +
            ", theEnactionDescription=" + getTheEnactionDescriptionId() +
            "}";
    }
}
