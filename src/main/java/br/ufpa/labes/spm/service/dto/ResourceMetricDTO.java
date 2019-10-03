package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ResourceMetric} entity.
 */
public class ResourceMetricDTO implements Serializable {

    private Long id;


    private Long resourceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResourceMetricDTO resourceMetricDTO = (ResourceMetricDTO) o;
        if (resourceMetricDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resourceMetricDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResourceMetricDTO{" +
            "id=" + getId() +
            ", resource=" + getResourceId() +
            "}";
    }
}
