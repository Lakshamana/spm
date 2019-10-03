package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.WorkGroupMetric} entity.
 */
public class WorkGroupMetricDTO implements Serializable {

    private Long id;


    private Long workGroupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkGroupId() {
        return workGroupId;
    }

    public void setWorkGroupId(Long workGroupId) {
        this.workGroupId = workGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkGroupMetricDTO workGroupMetricDTO = (WorkGroupMetricDTO) o;
        if (workGroupMetricDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workGroupMetricDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkGroupMetricDTO{" +
            "id=" + getId() +
            ", workGroup=" + getWorkGroupId() +
            "}";
    }
}
