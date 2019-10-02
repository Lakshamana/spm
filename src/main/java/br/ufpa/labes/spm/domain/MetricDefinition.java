package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A MetricDefinition. */
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
  private MetricType metricType;

  @OneToMany(mappedBy = "metricDefinition")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Estimation> estimations = new HashSet<>();

  @OneToMany(mappedBy = "metricDefinition")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Metric> metrics = new HashSet<>();

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

  public MetricType getMetricType() {
    return metricType;
  }

  public MetricDefinition metricType(MetricType metricType) {
    this.metricType = metricType;
    return this;
  }

  public void setMetricType(MetricType metricType) {
    this.metricType = metricType;
  }

  public Set<Estimation> getEstimations() {
    return estimations;
  }

  public MetricDefinition estimations(Set<Estimation> estimations) {
    this.estimations = estimations;
    return this;
  }

  public MetricDefinition addEstimation(Estimation estimation) {
    this.estimations.add(estimation);
    estimation.setMetricDefinition(this);
    return this;
  }

  public MetricDefinition removeEstimation(Estimation estimation) {
    this.estimations.remove(estimation);
    estimation.setMetricDefinition(null);
    return this;
  }

  public void setEstimations(Set<Estimation> estimations) {
    this.estimations = estimations;
  }

  public Set<Metric> getMetrics() {
    return metrics;
  }

  public MetricDefinition metrics(Set<Metric> metrics) {
    this.metrics = metrics;
    return this;
  }

  public MetricDefinition addMetric(Metric metric) {
    this.metrics.add(metric);
    metric.setMetricDefinition(this);
    return this;
  }

  public MetricDefinition removeMetric(Metric metric) {
    this.metrics.remove(metric);
    metric.setMetricDefinition(null);
    return this;
  }

  public void setMetrics(Set<Metric> metrics) {
    this.metrics = metrics;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

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
    return "MetricDefinition{"
        + "id="
        + getId()
        + ", name='"
        + getName()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + ", kind='"
        + getKind()
        + "'"
        + ", rangeFrom="
        + getRangeFrom()
        + ", rangeTo="
        + getRangeTo()
        + ", howToMeasure='"
        + getHowToMeasure()
        + "'"
        + "}";
  }
}
