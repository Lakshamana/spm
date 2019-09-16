package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A WorkGroupMetric.
 */
@Entity
@Table(name = "work_group_metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WorkGroupMetric extends Metric implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "theWorkGroupMetricSub")
    @JsonIgnore
    private Metric theMetricSuper;

    @ManyToOne
    @JsonIgnoreProperties("theWorkGroupMetrics")
    private WorkGroup theWorkGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Metric getTheMetricSuper() {
        return theMetricSuper;
    }

    public WorkGroupMetric theMetricSuper(Metric metric) {
        this.theMetricSuper = metric;
        return this;
    }

    public void setTheMetricSuper(Metric metric) {
        this.theMetricSuper = metric;
    }

    public WorkGroup getTheWorkGroup() {
        return theWorkGroup;
    }

    public WorkGroupMetric theWorkGroup(WorkGroup workGroup) {
        this.theWorkGroup = workGroup;
        return this;
    }

    public void setTheWorkGroup(WorkGroup workGroup) {
        this.theWorkGroup = workGroup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkGroupMetric)) {
            return false;
        }
        return id != null && id.equals(((WorkGroupMetric) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WorkGroupMetric{" +
            "id=" + getId() +
            "}";
    }
}
