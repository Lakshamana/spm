package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A OutOfWorkPeriod.
 */
@Entity
@Table(name = "out_of_work_period")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OutOfWorkPeriod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "why")
    private String why;

    @Column(name = "from_date")
    private String fromDate;

    @Column(name = "to_date")
    private String toDate;

    @ManyToOne
    @JsonIgnoreProperties("theOutOfWorkPeriods")
    private Agent theAgent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWhy() {
        return why;
    }

    public OutOfWorkPeriod why(String why) {
        this.why = why;
        return this;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public String getFromDate() {
        return fromDate;
    }

    public OutOfWorkPeriod fromDate(String fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public OutOfWorkPeriod toDate(String toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Agent getTheAgent() {
        return theAgent;
    }

    public OutOfWorkPeriod theAgent(Agent agent) {
        this.theAgent = agent;
        return this;
    }

    public void setTheAgent(Agent agent) {
        this.theAgent = agent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OutOfWorkPeriod)) {
            return false;
        }
        return id != null && id.equals(((OutOfWorkPeriod) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OutOfWorkPeriod{" +
            "id=" + getId() +
            ", why='" + getWhy() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            "}";
    }
}
