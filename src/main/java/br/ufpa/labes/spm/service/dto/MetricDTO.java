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


    private Long theActivityMetricSubId;

    private Long theAgentMetricSubId;

    private Long theArtifactMetricSubId;

    private Long theWorkGroupMetricSubId;

    private Long theOrganizationMetricSubId;

    private Long theProcessMetricSubId;

    private Long theResourceMetricSubId;

    private Long theMetricDefinitionId;

    private Long theActivityId;

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

    public Long getTheActivityMetricSubId() {
        return theActivityMetricSubId;
    }

    public void setTheActivityMetricSubId(Long activityMetricId) {
        this.theActivityMetricSubId = activityMetricId;
    }

    public Long getTheAgentMetricSubId() {
        return theAgentMetricSubId;
    }

    public void setTheAgentMetricSubId(Long agentMetricId) {
        this.theAgentMetricSubId = agentMetricId;
    }

    public Long getTheArtifactMetricSubId() {
        return theArtifactMetricSubId;
    }

    public void setTheArtifactMetricSubId(Long artifactMetricId) {
        this.theArtifactMetricSubId = artifactMetricId;
    }

    public Long getTheWorkGroupMetricSubId() {
        return theWorkGroupMetricSubId;
    }

    public void setTheWorkGroupMetricSubId(Long workGroupMetricId) {
        this.theWorkGroupMetricSubId = workGroupMetricId;
    }

    public Long getTheOrganizationMetricSubId() {
        return theOrganizationMetricSubId;
    }

    public void setTheOrganizationMetricSubId(Long organizationMetricId) {
        this.theOrganizationMetricSubId = organizationMetricId;
    }

    public Long getTheProcessMetricSubId() {
        return theProcessMetricSubId;
    }

    public void setTheProcessMetricSubId(Long processMetricId) {
        this.theProcessMetricSubId = processMetricId;
    }

    public Long getTheResourceMetricSubId() {
        return theResourceMetricSubId;
    }

    public void setTheResourceMetricSubId(Long resourceMetricId) {
        this.theResourceMetricSubId = resourceMetricId;
    }

    public Long getTheMetricDefinitionId() {
        return theMetricDefinitionId;
    }

    public void setTheMetricDefinitionId(Long metricDefinitionId) {
        this.theMetricDefinitionId = metricDefinitionId;
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
            ", theActivityMetricSub=" + getTheActivityMetricSubId() +
            ", theAgentMetricSub=" + getTheAgentMetricSubId() +
            ", theArtifactMetricSub=" + getTheArtifactMetricSubId() +
            ", theWorkGroupMetricSub=" + getTheWorkGroupMetricSubId() +
            ", theOrganizationMetricSub=" + getTheOrganizationMetricSubId() +
            ", theProcessMetricSub=" + getTheProcessMetricSubId() +
            ", theResourceMetricSub=" + getTheResourceMetricSubId() +
            ", theMetricDefinition=" + getTheMetricDefinitionId() +
            ", theActivity=" + getTheActivityId() +
            "}";
    }
}
