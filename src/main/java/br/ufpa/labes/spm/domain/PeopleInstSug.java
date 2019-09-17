package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PeopleInstSug.
 */
@Entity
@Table(name = "people_inst_sug")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.JOINED)
public class PeopleInstSug implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "theInstAgSug")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentInstSuggestionToAgent> theAgentInstSugToAgents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AgentInstSuggestionToAgent> getTheAgentInstSugToAgents() {
        return theAgentInstSugToAgents;
    }

    public PeopleInstSug theAgentInstSugToAgents(Set<AgentInstSuggestionToAgent> agentInstSuggestionToAgents) {
        this.theAgentInstSugToAgents = agentInstSuggestionToAgents;
        return this;
    }

    public PeopleInstSug addTheAgentInstSugToAgent(AgentInstSuggestionToAgent agentInstSuggestionToAgent) {
        this.theAgentInstSugToAgents.add(agentInstSuggestionToAgent);
        agentInstSuggestionToAgent.setTheInstAgSug(this);
        return this;
    }

    public PeopleInstSug removeTheAgentInstSugToAgent(AgentInstSuggestionToAgent agentInstSuggestionToAgent) {
        this.theAgentInstSugToAgents.remove(agentInstSuggestionToAgent);
        agentInstSuggestionToAgent.setTheInstAgSug(null);
        return this;
    }

    public void setTheAgentInstSugToAgents(Set<AgentInstSuggestionToAgent> agentInstSuggestionToAgents) {
        this.theAgentInstSugToAgents = agentInstSuggestionToAgents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeopleInstSug)) {
            return false;
        }
        return id != null && id.equals(((PeopleInstSug) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PeopleInstSug{" +
            "id=" + getId() +
            "}";
    }
}
