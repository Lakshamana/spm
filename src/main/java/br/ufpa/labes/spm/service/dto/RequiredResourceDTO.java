package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.RequiredResource} entity.
 */
public class RequiredResourceDTO implements Serializable {

    private Long id;

    private Float amountNeeded;


    private Long theNormalId;

    private Long theResourceTypeId;

    private Long theResourceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmountNeeded() {
        return amountNeeded;
    }

    public void setAmountNeeded(Float amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public Long getTheNormalId() {
        return theNormalId;
    }

    public void setTheNormalId(Long normalId) {
        this.theNormalId = normalId;
    }

    public Long getTheResourceTypeId() {
        return theResourceTypeId;
    }

    public void setTheResourceTypeId(Long resourceTypeId) {
        this.theResourceTypeId = resourceTypeId;
    }

    public Long getTheResourceId() {
        return theResourceId;
    }

    public void setTheResourceId(Long resourceId) {
        this.theResourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RequiredResourceDTO requiredResourceDTO = (RequiredResourceDTO) o;
        if (requiredResourceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requiredResourceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequiredResourceDTO{" +
            "id=" + getId() +
            ", amountNeeded=" + getAmountNeeded() +
            ", theNormal=" + getTheNormalId() +
            ", theResourceType=" + getTheResourceTypeId() +
            ", theResource=" + getTheResourceId() +
            "}";
    }
}
