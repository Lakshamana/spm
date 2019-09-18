package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MetricDefinition.
 */
@Entity
@Table(name = "metric_definition")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MetricDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "kind")
    private String kind;

    @Column(name = "range_from")
    private Float rangeFrom;

    @Column(name = "range_to")
    private Float rangeTo;

    @Lob
    @Column(name = "how_to_measure")
    private String howToMeasure;

    @OneToMany(mappedBy = "theMetricDefinition")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MetricDefinitionUnit> units = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("theMetricDefinitions")
    private MetricType theMetricType;

    @OneToMany(mappedBy = "theMetricDefinition")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Estimation> theEstimations = new HashSet<>();

    @OneToMany(mappedBy = "theMetricDefinition")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Metric> theMetrics = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MetricDefinition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public MetricDefinition description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKind() {
        return kind;
    }

    public MetricDefinition kind(String kind) {
        this.kind = kind;
        return this;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Float getRangeFrom() {
        return rangeFrom;
    }

    public MetricDefinition rangeFrom(Float rangeFrom) {
        this.rangeFrom = rangeFrom;
        return this;
    }

    public void setRangeFrom(Float rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public Float getRangeTo() {
        return rangeTo;
    }

    public MetricDefinition rangeTo(Float rangeTo) {
        this.rangeTo = rangeTo;
        return this;
    }

    public void setRangeTo(Float rangeTo) {
        this.rangeTo = rangeTo;
    }

    public String getHowToMeasure() {
        return howToMeasure;
    }

    public MetricDefinition howToMeasure(String howToMeasure) {
        this.howToMeasure = howToMeasure;
        return this;
    }

    public void setHowToMeasure(String howToMeasure) {
        this.howToMeasure = howToMeasure;
    }

    public Set<MetricDefinitionUnit> getUnits() {
        return units;
    }

    public MetricDefinition units(Set<MetricDefinitionUnit> metricDefinitionUnits) {
        this.units = metricDefinitionUnits;
        return this;
    }

    public MetricDefinition addUnits(MetricDefinitionUnit metricDefinitionUnit) {
        this.units.add(metricDefinitionUnit);
        metricDefinitionUnit.setTheMetricDefinition(this);
        return this;
    }

    public MetricDefinition removeUnits(MetricDefinitionUnit metricDefinitionUnit) {
        this.units.remove(metricDefinitionUnit);
        metricDefinitionUnit.setTheMetricDefinition(null);
        return this;
    }

    public void setUnits(Set<MetricDefinitionUnit> metricDefinitionUnits) {
        this.units = metricDefinitionUnits;
    }

    public MetricType getTheMetricType() {
        return theMetricType;
    }

    public MetricDefinition theMetricType(MetricType metricType) {
        this.theMetricType = metricType;
        return this;
    }

    public void setTheMetricType(MetricType metricType) {
        this.theMetricType = metricType;
    }

    public Set<Estimation> getTheEstimations() {
        return theEstimations;
    }

    public MetricDefinition theEstimations(Set<Estimation> estimations) {
        this.theEstimations = estimations;
        return this;
    }

    public MetricDefinition addTheEstimation(Estimation estimation) {
        this.theEstimations.add(estimation);
        estimation.setTheMetricDefinition(this);
        return this;
    }

    public MetricDefinition removeTheEstimation(Estimation estimation) {
        this.theEstimations.remove(estimation);
        estimation.setTheMetricDefinition(null);
        return this;
    }

    public void setTheEstimations(Set<Estimation> estimations) {
        this.theEstimations = estimations;
    }

    public Set<Metric> getTheMetrics() {
        return theMetrics;
    }

    public MetricDefinition theMetrics(Set<Metric> metrics) {
        this.theMetrics = metrics;
        return this;
    }

    public MetricDefinition addTheMetric(Metric metric) {
        this.theMetrics.add(metric);
        metric.setTheMetricDefinition(this);
        return this;
    }

    public MetricDefinition removeTheMetric(Metric metric) {
        this.theMetrics.remove(metric);
        metric.setTheMetricDefinition(null);
        return this;
    }

    public void setTheMetrics(Set<Metric> metrics) {
        this.theMetrics = metrics;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetricDefinition)) {
            return false;
        }
        return id != null && id.equals(((MetricDefinition) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MetricDefinition{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", kind='" + getKind() + "'" +
            ", rangeFrom=" + getRangeFrom() +
            ", rangeTo=" + getRangeTo() +
            ", howToMeasure='" + getHowToMeasure() + "'" +
            "}";
    }
}
