package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Estimation.
 */
@Entity
@Table(name = "estimation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.JOINED)
public class Estimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private Float value;

    @Column(name = "unit")
    private String unit;

    @ManyToOne
    @JsonIgnoreProperties("theEstimations")
    private MetricDefinition theMetricDefinition;

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

    public Estimation value(Float value) {
        this.value = value;
        return this;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public Estimation unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public MetricDefinition getTheMetricDefinition() {
        return theMetricDefinition;
    }

    public Estimation theMetricDefinition(MetricDefinition metricDefinition) {
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
        if (!(o instanceof Estimation)) {
            return false;
        }
        return id != null && id.equals(((Estimation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Estimation{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", unit='" + getUnit() + "'" +
            "}";
    }
}
