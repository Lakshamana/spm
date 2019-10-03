package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

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
@Inheritance(strategy=InheritanceType.JOINED)
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

    @ManyToOne
    @JsonIgnoreProperties("metrics")
    private MetricDefinition metricDefinition;

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

    public MetricDefinition getMetricDefinition() {
        return metricDefinition;
    }

    public Metric metricDefinition(MetricDefinition metricDefinition) {
        this.metricDefinition = metricDefinition;
        return this;
    }

    public void setMetricDefinition(MetricDefinition metricDefinition) {
        this.metricDefinition = metricDefinition;
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
