package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AgentInstSug.
 */
@Entity
@Table(name = "agent_inst_sug")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgentInstSug implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theAgentInstSugs")
    private Role theRole;

    @ManyToOne
    @JsonIgnoreProperties("theAgentInstSugs")
    private Agent chosenAgent;

    @OneToMany(mappedBy = "theInstAgSugg")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentInstSuggestionToAgent> agentSuggesteds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getTheRole() {
        return theRole;
    }

    public AgentInstSug theRole(Role role) {
        this.theRole = role;
        return this;
    }

    public void setTheRole(Role role) {
        this.theRole = role;
    }

    public Agent getChosenAgent() {
        return chosenAgent;
    }

    public AgentInstSug chosenAgent(Agent agent) {
        this.chosenAgent = agent;
        return this;
    }

    public void setChosenAgent(Agent agent) {
        this.chosenAgent = agent;
    }

    public Set<AgentInstSuggestionToAgent> getAgentSuggesteds() {
        return agentSuggesteds;
    }

    public AgentInstSug agentSuggesteds(Set<AgentInstSuggestionToAgent> agentInstSuggestionToAgents) {
        this.agentSuggesteds = agentInstSuggestionToAgents;
        return this;
    }

    public AgentInstSug addAgentSuggested(AgentInstSuggestionToAgent agentInstSuggestionToAgent) {
        this.agentSuggesteds.add(agentInstSuggestionToAgent);
        agentInstSuggestionToAgent.setTheInstAgSugg(this);
        return this;
    }

    public AgentInstSug removeAgentSuggested(AgentInstSuggestionToAgent agentInstSuggestionToAgent) {
        this.agentSuggesteds.remove(agentInstSuggestionToAgent);
        agentInstSuggestionToAgent.setTheInstAgSugg(null);
        return this;
    }

    public void setAgentSuggesteds(Set<AgentInstSuggestionToAgent> agentInstSuggestionToAgents) {
        this.agentSuggesteds = agentInstSuggestionToAgents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentInstSug)) {
            return false;
        }
        return id != null && id.equals(((AgentInstSug) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AgentInstSug{" +
            "id=" + getId() +
            "}";
    }
}
