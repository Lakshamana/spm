package br.ufpa.labes.spm.service.dto;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.MetricDefinitionUnit} entity.
 */
@ApiModel(description = "MetricDefinition String Values List Wrapper")
public class MetricDefinitionUnitDTO implements Serializable {

    private Long id;

    private String unit;


    private Long theMetricDefinitionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getTheMetricDefinitionId() {
        return theMetricDefinitionId;
    }

    public void setTheMetricDefinitionId(Long metricDefinitionId) {
        this.theMetricDefinitionId = metricDefinitionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MetricDefinitionUnitDTO metricDefinitionUnitDTO = (MetricDefinitionUnitDTO) o;
        if (metricDefinitionUnitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), metricDefinitionUnitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MetricDefinitionUnitDTO{" +
            "id=" + getId() +
            ", unit='" + getUnit() + "'" +
            ", theMetricDefinition=" + getTheMetricDefinitionId() +
            "}";
    }
}
