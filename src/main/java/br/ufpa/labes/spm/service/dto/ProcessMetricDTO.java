package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ProcessMetric} entity.
 */
public class ProcessMetricDTO implements Serializable {

    private Long id;


    private Long processId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProcessMetricDTO processMetricDTO = (ProcessMetricDTO) o;
        if (processMetricDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), processMetricDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcessMetricDTO{" +
            "id=" + getId() +
            ", process=" + getProcessId() +
            "}";
    }
}
