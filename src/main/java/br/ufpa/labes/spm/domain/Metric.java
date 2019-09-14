package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Metric.
 */
@Entity
@Table(name = "metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Metric implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private Float value;

    @Column(name = "unit")
    private String unit;

    @Column(name = "period_begin")
    private LocalDate periodBegin;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @OneToOne
    @JoinColumn(unique = true)
    private ActivityMetric theActivityMetricSub;

    @OneToOne
    @JoinColumn(unique = true)
    private AgentMetric theAgentMetricSub;

    @OneToOne
    @JoinColumn(unique = true)
    private ArtifactMetric theArtifactMetricSub;

    @OneToOne
    @JoinColumn(unique = true)
    private WorkGroupMetric theWorkGroupMetricSub;

    @OneToOne
    @JoinColumn(unique = true)
    private OrganizationMetric theOrganizationMetricSub;

    @OneToOne
    @JoinColumn(unique = true)
    private ProcessMetric theProcessMetricSub;

    @OneToOne
    @JoinColumn(unique = true)
    private ResourceMetric theResourceMetricSub;

    @ManyToOne
    @JsonIgnoreProperties("theMetrics")
    private MetricDefinition theMetricDefinition;

    @ManyToOne
    @JsonIgnoreProperties("activityMetrics")
    private Activity theActivity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getValue() {
        return value;
    }

    public Metric value(Float value) {
        this.value = value;
        return this;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public Metric unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getPeriodBegin() {
        return periodBegin;
    }

    public Metric periodBegin(LocalDate periodBegin) {
        this.periodBegin = periodBegin;
        return this;
    }

    public void setPeriodBegin(LocalDate periodBegin) {
        this.periodBegin = periodBegin;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public Metric periodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
        return this;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public ActivityMetric getTheActivityMetricSub() {
        return theActivityMetricSub;
    }

    public Metric theActivityMetricSub(ActivityMetric activityMetric) {
        this.theActivityMetricSub = activityMetric;
        return this;
    }

    public void setTheActivityMetricSub(ActivityMetric activityMetric) {
        this.theActivityMetricSub = activityMetric;
    }

    public AgentMetric getTheAgentMetricSub() {
        return theAgentMetricSub;
    }

    public Metric theAgentMetricSub(AgentMetric agentMetric) {
        this.theAgentMetricSub = agentMetric;
        return this;
    }

    public void setTheAgentMetricSub(AgentMetric agentMetric) {
        this.theAgentMetricSub = agentMetric;
    }

    public ArtifactMetric getTheArtifactMetricSub() {
        return theArtifactMetricSub;
    }

    public Metric theArtifactMetricSub(ArtifactMetric artifactMetric) {
        this.theArtifactMetricSub = artifactMetric;
        return this;
    }

    public void setTheArtifactMetricSub(ArtifactMetric artifactMetric) {
        this.theArtifactMetricSub = artifactMetric;
    }

    public WorkGroupMetric getTheWorkGroupMetricSub() {
        return theWorkGroupMetricSub;
    }

    public Metric theWorkGroupMetricSub(WorkGroupMetric workGroupMetric) {
        this.theWorkGroupMetricSub = workGroupMetric;
        return this;
    }

    public void setTheWorkGroupMetricSub(WorkGroupMetric workGroupMetric) {
        this.theWorkGroupMetricSub = workGroupMetric;
    }

    public OrganizationMetric getTheOrganizationMetricSub() {
        return theOrganizationMetricSub;
    }

    public Metric theOrganizationMetricSub(OrganizationMetric organizationMetric) {
        this.theOrganizationMetricSub = organizationMetric;
        return this;
    }

    public void setTheOrganizationMetricSub(OrganizationMetric organizationMetric) {
        this.theOrganizationMetricSub = organizationMetric;
    }

    public ProcessMetric getTheProcessMetricSub() {
        return theProcessMetricSub;
    }

    public Metric theProcessMetricSub(ProcessMetric processMetric) {
        this.theProcessMetricSub = processMetric;
        return this;
    }

    public void setTheProcessMetricSub(ProcessMetric processMetric) {
        this.theProcessMetricSub = processMetric;
    }

    public ResourceMetric getTheResourceMetricSub() {
        return theResourceMetricSub;
    }

    public Metric theResourceMetricSub(ResourceMetric resourceMetric) {
        this.theResourceMetricSub = resourceMetric;
        return this;
    }

    public void setTheResourceMetricSub(ResourceMetric resourceMetric) {
        this.theResourceMetricSub = resourceMetric;
    }

    public MetricDefinition getTheMetricDefinition() {
        return theMetricDefinition;
    }

    public Metric theMetricDefinition(MetricDefinition metricDefinition) {
        this.theMetricDefinition = metricDefinition;
        return this;
    }

    public void setTheMetricDefinition(MetricDefinition metricDefinition) {
        this.theMetricDefinition = metricDefinition;
    }

    public Activity getTheActivity() {
        return theActivity;
    }

    public Metric theActivity(Activity activity) {
        this.theActivity = activity;
        return this;
    }

    public void setTheActivity(Activity activity) {
        this.theActivity = activity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metric)) {
            return false;
        }
        return id != null && id.equals(((Metric) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Metric{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", unit='" + getUnit() + "'" +
            ", periodBegin='" + getPeriodBegin() + "'" +
            ", periodEnd='" + getPeriodEnd() + "'" +
            "}";
    }
}
