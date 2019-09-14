package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import br.ufpa.labes.spm.domain.enumeration.PlainActivityStatus;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.PlainActivity} entity.
 */
public class PlainActivityDTO implements Serializable {

    private Long id;

    @Lob
    private String requirements;

    private PlainActivityStatus plainActivityStatus;

    private Boolean automatic;


    private Long theEnactionDescriptionId;

    private Long theAutomaticActivitySubId;

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

    public PlainActivityStatus getPlainActivityStatus() {
        return plainActivityStatus;
    }

    public void setPlainActivityStatus(PlainActivityStatus plainActivityStatus) {
        this.plainActivityStatus = plainActivityStatus;
    }

    public Boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    public Long getTheEnactionDescriptionId() {
        return theEnactionDescriptionId;
    }

    public void setTheEnactionDescriptionId(Long enactionDescriptionId) {
        this.theEnactionDescriptionId = enactionDescriptionId;
    }

    public Long getTheAutomaticActivitySubId() {
        return theAutomaticActivitySubId;
    }

    public void setTheAutomaticActivitySubId(Long automaticActivityId) {
        this.theAutomaticActivitySubId = automaticActivityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlainActivityDTO plainActivityDTO = (PlainActivityDTO) o;
        if (plainActivityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), plainActivityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlainActivityDTO{" +
            "id=" + getId() +
            ", requirements='" + getRequirements() + "'" +
            ", plainActivityStatus='" + getPlainActivityStatus() + "'" +
            ", automatic='" + isAutomatic() + "'" +
            ", theEnactionDescription=" + getTheEnactionDescriptionId() +
            ", theAutomaticActivitySub=" + getTheAutomaticActivitySubId() +
            "}";
    }
}
