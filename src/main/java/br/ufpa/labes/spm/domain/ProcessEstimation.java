package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ProcessEstimation.
 */
@Entity
@Table(name = "process_estimation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcessEstimation extends Estimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theProcessEstimations")
    private Process theProcess;

    @OneToOne(mappedBy = "theProcessEstimationSub")
    @JsonIgnore
    private Estimation theEstimationSuper;

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

    public ProcessEstimation theProcess(Process process) {
        this.theProcess = process;
        return this;
    }

    public void setTheProcess(Process process) {
        this.theProcess = process;
    }

    public Estimation getTheEstimationSuper() {
        return theEstimationSuper;
    }

    public ProcessEstimation theEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
        return this;
    }

    public void setTheEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessEstimation)) {
            return false;
        }
        return id != null && id.equals(((ProcessEstimation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProcessEstimation{" +
            "id=" + getId() +
            "}";
    }
}
