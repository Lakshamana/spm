package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ResourceMetric.
 */
@Entity
@Table(name = "resource_metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResourceMetric extends Metric implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theResourceMetrics")
    private Resource theResource;

    @OneToOne(mappedBy = "theResourceMetricSub")
    @JsonIgnore
    private Metric theMetricSuper;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getTheResource() {
        return theResource;
    }

    public ResourceMetric theResource(Resource resource) {
        this.theResource = resource;
        return this;
    }

    public void setTheResource(Resource resource) {
        this.theResource = resource;
    }

    public Metric getTheMetricSuper() {
        return theMetricSuper;
    }

    public ResourceMetric theMetricSuper(Metric metric) {
        this.theMetricSuper = metric;
        return this;
    }

    public void setTheMetricSuper(Metric metric) {
        this.theMetricSuper = metric;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceMetric)) {
            return false;
        }
        return id != null && id.equals(((ResourceMetric) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ResourceMetric{" +
            "id=" + getId() +
            "}";
    }
}
