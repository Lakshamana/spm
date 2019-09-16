package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ProcessMetric.
 */
@Entity
@Table(name = "process_metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcessMetric extends Metric implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theProcessMetrics")
    private Process theProcess;

    @OneToOne(mappedBy = "theProcessMetricSub")
    @JsonIgnore
    private Metric theMetricSuper;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Process getTheProcess() {
        return theProcess;
    }

    public ProcessMetric theProcess(Process process) {
        this.theProcess = process;
        return this;
    }

    public void setTheProcess(Process process) {
        this.theProcess = process;
    }

    public Metric getTheMetricSuper() {
        return theMetricSuper;
    }

    public ProcessMetric theMetricSuper(Metric metric) {
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
        if (!(o instanceof ProcessMetric)) {
            return false;
        }
        return id != null && id.equals(((ProcessMetric) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProcessMetric{" +
            "id=" + getId() +
            "}";
    }
}
