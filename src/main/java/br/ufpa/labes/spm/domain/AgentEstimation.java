package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A AgentEstimation.
 */
@Entity
@Table(name = "agent_estimation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgentEstimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "theAgentEstimationSub")
    @JsonIgnore
    private Estimation theEstimationSuper;

    @ManyToOne
    @JsonIgnoreProperties("theAgentEstimations")
    private Agent theAgent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estimation getTheEstimationSuper() {
        return theEstimationSuper;
    }

    public AgentEstimation theEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
        return this;
    }

    public void setTheEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
    }

    public Agent getTheAgent() {
        return theAgent;
    }

    public AgentEstimation theAgent(Agent agent) {
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
        if (!(o instanceof AgentEstimation)) {
            return false;
        }
        return id != null && id.equals(((AgentEstimation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AgentEstimation{" +
            "id=" + getId() +
            "}";
    }
}
