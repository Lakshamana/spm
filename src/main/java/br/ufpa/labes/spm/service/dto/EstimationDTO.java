package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Estimation} entity. */
public class EstimationDTO implements Serializable {

  private Long id;

  private Float value;

  private String unit;

  private Long theActivityEstimationSubId;

  private Long theAgentEstimationSubId;

  private Long theArtifactEstimationSubId;

  private Long theWorkGroupEstimationSubId;

  private Long theOrganizationEstimationSubId;

  private Long theProcessEstimationSubId;

  private Long theResourceEstimationSubId;

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

  public Long getTheActivityEstimationSubId() {
    return theActivityEstimationSubId;
  }

  public void setTheActivityEstimationSubId(Long activityEstimationId) {
    this.theActivityEstimationSubId = activityEstimationId;
  }

  public Long getTheAgentEstimationSubId() {
    return theAgentEstimationSubId;
  }

  public void setTheAgentEstimationSubId(Long agentEstimationId) {
    this.theAgentEstimationSubId = agentEstimationId;
  }

  public Long getTheArtifactEstimationSubId() {
    return theArtifactEstimationSubId;
  }

  public void setTheArtifactEstimationSubId(Long artifactEstimationId) {
    this.theArtifactEstimationSubId = artifactEstimationId;
  }

  public Long getTheWorkGroupEstimationSubId() {
    return theWorkGroupEstimationSubId;
  }

  public void setTheWorkGroupEstimationSubId(Long workGroupEstimationId) {
    this.theWorkGroupEstimationSubId = workGroupEstimationId;
  }

  public Long getTheOrganizationEstimationSubId() {
    return theOrganizationEstimationSubId;
  }

  public void setTheOrganizationEstimationSubId(Long organizationEstimationId) {
    this.theOrganizationEstimationSubId = organizationEstimationId;
  }

  public Long getTheProcessEstimationSubId() {
    return theProcessEstimationSubId;
  }

  public void setTheProcessEstimationSubId(Long processEstimationId) {
    this.theProcessEstimationSubId = processEstimationId;
  }

  public Long getTheResourceEstimationSubId() {
    return theResourceEstimationSubId;
  }

  public void setTheResourceEstimationSubId(Long resourceEstimationId) {
    this.theResourceEstimationSubId = resourceEstimationId;
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
        + ", theActivityEstimationSub="
        + getTheActivityEstimationSubId()
        + ", theAgentEstimationSub="
        + getTheAgentEstimationSubId()
        + ", theArtifactEstimationSub="
        + getTheArtifactEstimationSubId()
        + ", theWorkGroupEstimationSub="
        + getTheWorkGroupEstimationSubId()
        + ", theOrganizationEstimationSub="
        + getTheOrganizationEstimationSubId()
        + ", theProcessEstimationSub="
        + getTheProcessEstimationSubId()
        + ", theResourceEstimationSub="
        + getTheResourceEstimationSubId()
        + ", theMetricDefinition="
        + getTheMetricDefinitionId()
        + "}";
  }
}
