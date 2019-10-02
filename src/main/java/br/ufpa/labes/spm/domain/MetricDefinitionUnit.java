package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * MetricDefinition String Values List Wrapper
 */
@ApiModel(description = "MetricDefinition String Values List Wrapper")
@Entity
@Table(name = "metric_definition_unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MetricDefinitionUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit")
    private String unit;

    @ManyToOne
    @JsonIgnoreProperties("units")
    private MetricDefinition theMetricDefinition;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public MetricDefinitionUnit unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public MetricDefinition getTheMetricDefinition() {
        return theMetricDefinition;
    }

    public MetricDefinitionUnit theMetricDefinition(MetricDefinition metricDefinition) {
        this.theMetricDefinition = metricDefinition;
        return this;
    }

    public void setTheMetricDefinition(MetricDefinition metricDefinition) {
        this.theMetricDefinition = metricDefinition;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetricDefinitionUnit)) {
            return false;
        }
        return id != null && id.equals(((MetricDefinitionUnit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MetricDefinitionUnit{" +
            "id=" + getId() +
            ", unit='" + getUnit() + "'" +
            "}";
    }
}
