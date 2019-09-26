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


    private Long groupChosenId;

    private Long groupTypeRequiredId;

    private Set<WorkGroupDTO> groupSuggesteds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupChosenId() {
        return groupChosenId;
    }

    public void setGroupChosenId(Long workGroupId) {
        this.groupChosenId = workGroupId;
    }

    public Long getGroupTypeRequiredId() {
        return groupTypeRequiredId;
    }

    public void setGroupTypeRequiredId(Long workGroupTypeId) {
        this.groupTypeRequiredId = workGroupTypeId;
    }

    public Set<WorkGroupDTO> getGroupSuggesteds() {
        return groupSuggesteds;
    }

    public void setGroupSuggesteds(Set<WorkGroupDTO> workGroups) {
        this.groupSuggesteds = workGroups;
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
            ", groupChosen=" + getGroupChosenId() +
            ", groupTypeRequired=" + getGroupTypeRequiredId() +
            "}";
    }
}
