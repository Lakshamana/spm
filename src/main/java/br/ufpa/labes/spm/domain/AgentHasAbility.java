package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A AgentHasAbility.
 */
@Entity
@Table(name = "agent_has_ability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgentHasAbility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "degree")
    private Integer degree;

    @ManyToOne
    @JsonIgnoreProperties("theAgentHasAbilities")
    private Agent theAgent;

    @ManyToOne
    @JsonIgnoreProperties("theAgentHasAbilities")
    private Ability theAbility;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
	public AgentHasAbility() {
		this.degree = new Integer(0);
		this.theAgent = null; // must be set explicitely
		this.theAbility = null; // must be set explicitely
	}
	public AgentHasAbility(Agent theAgent, Ability theAbility) {
		this.degree = new Integer(0);
		this.setTheAgent(theAgent);
		this.setTheAbility(theAbility);
	}
	public AgentHasAbility(Integer degree, Agent theAgent, Ability theAbility) {
		this.degree = degree;
		this.setTheAgent(theAgent);
		this.setTheAbility(theAbility);
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDegree() {
        return degree;
    }

    public AgentHasAbility degree(Integer degree) {
        this.degree = degree;
        return this;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Agent getTheAgent() {
        return theAgent;
    }

    public AgentHasAbility theAgent(Agent agent) {
        this.theAgent = agent;
        return this;
    }

    public void setTheAgent(Agent agent) {
        this.theAgent = agent;
    }

    public Ability getTheAbility() {
        return theAbility;
    }

    public AgentHasAbility theAbility(Ability ability) {
        this.theAbility = ability;
        return this;
    }

    public void setTheAbility(Ability ability) {
        this.theAbility = ability;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentHasAbility)) {
            return false;
        }
        return id != null && id.equals(((AgentHasAbility) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AgentHasAbility{" +
            "id=" + getId() +
            ", degree=" + getDegree() +
            "}";
    }

    public void removeFromTheAbility() {
      if (this.theAbility != null) {
        this.theAbility.removeTheAgentHasAbility(this);
      }
    }


}
