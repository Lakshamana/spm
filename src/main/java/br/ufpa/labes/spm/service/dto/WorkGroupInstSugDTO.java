package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.WorkGroupInstSug} entity.
 */
public class WorkGroupInstSugDTO implements Serializable {

    private Long id;


    private Long chosenWorkGroupId;

    private Long workGroupTypeRequiredId;

    private Set<WorkGroupDTO> sugWorkGroups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChosenWorkGroupId() {
        return chosenWorkGroupId;
    }

    public void setChosenWorkGroupId(Long workGroupId) {
        this.chosenWorkGroupId = workGroupId;
    }

    public Long getWorkGroupTypeRequiredId() {
        return workGroupTypeRequiredId;
    }

    public void setWorkGroupTypeRequiredId(Long typeId) {
        this.workGroupTypeRequiredId = typeId;
    }

    public Set<WorkGroupDTO> getSugWorkGroups() {
        return sugWorkGroups;
    }

    public void setSugWorkGroups(Set<WorkGroupDTO> workGroups) {
        this.sugWorkGroups = workGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkGroupInstSugDTO workGroupInstSugDTO = (WorkGroupInstSugDTO) o;
        if (workGroupInstSugDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workGroupInstSugDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkGroupInstSugDTO{" +
            "id=" + getId() +
            ", chosenWorkGroup=" + getChosenWorkGroupId() +
            ", workGroupTypeRequired=" + getWorkGroupTypeRequiredId() +
            "}";
    }
}
