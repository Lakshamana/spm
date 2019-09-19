package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.BranchConCondToActivity} entity.
 */
public class BranchConCondToActivityDTO implements Serializable {

    private Long id;


    private Long theActivityId;

    private Long theBranchConCondId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheActivityId() {
        return theActivityId;
    }

    public void setTheActivityId(Long activityId) {
        this.theActivityId = activityId;
    }

    public Long getTheBranchConCondId() {
        return theBranchConCondId;
    }

    public void setTheBranchConCondId(Long branchConCondId) {
        this.theBranchConCondId = branchConCondId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BranchConCondToActivityDTO branchConCondToActivityDTO = (BranchConCondToActivityDTO) o;
        if (branchConCondToActivityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), branchConCondToActivityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BranchConCondToActivityDTO{" +
            "id=" + getId() +
            ", theActivity=" + getTheActivityId() +
            ", theBranchConCond=" + getTheBranchConCondId() +
            "}";
    }
}
