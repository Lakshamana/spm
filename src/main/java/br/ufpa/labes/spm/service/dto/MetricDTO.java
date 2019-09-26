package br.ufpa.labes.spm.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Metric} entity.
 */
public class MetricDTO implements Serializable {

    private Long id;

    private Float value;

    private String unit;

    private LocalDate periodBegin;

    private LocalDate periodEnd;


    private Long metricDefinitionId;

    private Long activityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(LocalDate periodBegin) {
        this.periodBegin = periodBegin;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public Long getMetricDefinitionId() {
        return metricDefinitionId;
    }

    public void setMetricDefinitionId(Long metricDefinitionId) {
        this.metricDefinitionId = metricDefinitionId;
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

        MetricDTO metricDTO = (MetricDTO) o;
        if (metricDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), metricDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MetricDTO{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", unit='" + getUnit() + "'" +
            ", periodBegin='" + getPeriodBegin() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            ", metricDefinition=" + getMetricDefinitionId() +
            ", activity=" + getActivityId() +
            "}";
    }
}
