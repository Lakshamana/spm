package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Estimation} entity. */
public class EstimationDTO implements Serializable {

  private Long id;

  private Float value;

  private String unit;

  private Long theMetricDefinitionId;

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

    EstimationDTO estimationDTO = (EstimationDTO) o;
    if (estimationDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), estimationDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "EstimationDTO{"
        + "id="
        + getId()
        + ", value="
        + getValue()
        + ", unit='"
        + getUnit()
        + "'"
        + ", theMetricDefinition="
        + getTheMetricDefinitionId()
        + "}";
  }
}
