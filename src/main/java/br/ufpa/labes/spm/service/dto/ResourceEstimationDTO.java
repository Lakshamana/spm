package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ResourceEstimation} entity.
 */
public class ResourceEstimationDTO implements Serializable {

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

        ResourceEstimationDTO resourceEstimationDTO = (ResourceEstimationDTO) o;
        if (resourceEstimationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resourceEstimationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResourceEstimationDTO{" +
            "id=" + getId() +
            ", resource=" + getResourceId() +
            "}";
    }
}
