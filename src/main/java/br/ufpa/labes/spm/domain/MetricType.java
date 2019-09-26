package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MetricType.
 */
@Entity
@Table(name = "metric_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MetricType extends Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "metricType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MetricDefinition> theMetricDefinitions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<MetricDefinition> getTheMetricDefinitions() {
        return theMetricDefinitions;
    }

    public MetricType theMetricDefinitions(Set<MetricDefinition> metricDefinitions) {
        this.theMetricDefinitions = metricDefinitions;
        return this;
    }

    public MetricType addTheMetricDefinition(MetricDefinition metricDefinition) {
        this.theMetricDefinitions.add(metricDefinition);
        metricDefinition.setMetricType(this);
        return this;
    }

    public MetricType removeTheMetricDefinition(MetricDefinition metricDefinition) {
        this.theMetricDefinitions.remove(metricDefinition);
        metricDefinition.setMetricType(null);
        return this;
    }

    public void setTheMetricDefinitions(Set<MetricDefinition> metricDefinitions) {
        this.theMetricDefinitions = metricDefinitions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetricType)) {
            return false;
        }
        return id != null && id.equals(((MetricType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MetricType{" +
            "id=" + getId() +
            "}";
    }
}
