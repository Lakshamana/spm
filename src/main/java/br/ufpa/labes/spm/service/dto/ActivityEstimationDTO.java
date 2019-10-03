package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ActivityEstimation} entity.
 */
public class ActivityEstimationDTO implements Serializable {

    private Long id;


    private Long activityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityEstimationDTO activityEstimationDTO = (ActivityEstimationDTO) o;
        if (activityEstimationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityEstimationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityEstimationDTO{" +
            "id=" + getId() +
            ", activity=" + getActivityId() +
            "}";
    }
}
