package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ActivityMetric} entity.
 */
public class ActivityMetricDTO implements Serializable {

    private Long id;


    private Long theActivityId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityMetricDTO activityMetricDTO = (ActivityMetricDTO) o;
        if (activityMetricDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityMetricDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityMetricDTO{" +
            "id=" + getId() +
            ", theActivity=" + getTheActivityId() +
            "}";
    }
}
