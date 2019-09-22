package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A AgentPlaysRole.
 */
@Entity
@Table(name = "agent_plays_role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgentPlaysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "since_date")
    private LocalDate sinceDate;

    @ManyToOne
    @JsonIgnoreProperties("theAgentPlaysRoles")
    private Agent theAgent;

    @ManyToOne
    @JsonIgnoreProperties("theAgentPlaysRoles")
    private Role theRole;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSinceDate() {
        return sinceDate;
    }

    public AgentPlaysRole sinceDate(LocalDate sinceDate) {
        this.sinceDate = sinceDate;
        return this;
    }

    public void setSinceDate(LocalDate sinceDate) {
        this.sinceDate = sinceDate;
    }

    public Agent getTheAgent() {
        return theAgent;
    }

    public AgentPlaysRole theAgent(Agent agent) {
        this.theAgent = agent;
        return this;
    }

    public void setTheAgent(Agent agent) {
        this.theAgent = agent;
    }

    public Role getTheRole() {
        return theRole;
    }

    public AgentPlaysRole theRole(Role role) {
        this.theRole = role;
        return this;
    }

    public void setTheRole(Role role) {
        this.theRole = role;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgentPlaysRole)) {
            return false;
        }
        return id != null && id.equals(((AgentPlaysRole) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AgentPlaysRole{" +
            "id=" + getId() +
            ", sinceDate='" + getSinceDate() + "'" +
            "}";
    }

    public void removeFromTheRole() {
      if (this.theRole != null) {
        this.theRole.removeTheAgentPlaysRole(this);
      }
    }
}
