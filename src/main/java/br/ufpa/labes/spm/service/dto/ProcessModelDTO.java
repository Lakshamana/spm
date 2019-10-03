package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ProcessModel} entity.
 */
public class ProcessModelDTO implements Serializable {

    private Long id;

    private String requirements;

    private String pmState;


    private Long theOriginId;

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

    public String getPmState() {
        return pmState;
    }

    public void setPmState(String pmState) {
        this.pmState = pmState;
    }

    public Long getTheOriginId() {
        return theOriginId;
    }

    public void setTheOriginId(Long templateId) {
        this.theOriginId = templateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProcessModelDTO processModelDTO = (ProcessModelDTO) o;
        if (processModelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), processModelDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcessModelDTO{" +
            "id=" + getId() +
            ", requirements='" + getRequirements() + "'" +
            ", pmState='" + getPmState() + "'" +
            ", theOrigin=" + getTheOriginId() +
            "}";
    }
}
