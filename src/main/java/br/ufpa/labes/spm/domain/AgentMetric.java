package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A AgentMetric.
 */
@Entity
@Table(name = "agent_metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgentMetric extends Metric implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theAgentMetrics")
    private Agent theAgent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agent getTheAgent() {
        return theAgent;
    }

    public AgentMetric theAgent(Agent agent) {
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
        if (!(o instanceof AgentMetric)) {
            return false;
        }
        return id != null && id.equals(((AgentMetric) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AgentMetric{" +
            "id=" + getId() +
            "}";
    }
}
