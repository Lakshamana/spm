package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A AgentAffinityAgent. */
@Entity
@Table(name = "agent_affinity_agent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgentAffinityAgent implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "degree")
  private Integer degree;

  @ManyToOne
  @JsonIgnoreProperties("fromAgentAffinities")
  private Agent toAffinity;

  @ManyToOne
  @JsonIgnoreProperties("toAgentAffinities")
  private Agent fromAffinity;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public AgentAffinityAgent() {
    this.degree = new Integer(0);
    this.toAffinity = null; // must be set explicitely
    this.fromAffinity = null; // must be set explicitely
  }

  public AgentAffinityAgent(Agent fromAffinity, Agent toAffinity) {
    this.degree = new Integer(0);
    this.setFromAffinity(fromAffinity);
    this.setToAffinity(toAffinity);
  }

  public AgentAffinityAgent(Integer degree, Agent fromAffinity, Agent toAffinity) {
    this.degree = degree;
    this.setFromAffinity(fromAffinity);
    this.setToAffinity(toAffinity);
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

  public AgentAffinityAgent degree(Integer degree) {
    this.degree = degree;
    return this;
  }

  public void setDegree(Integer degree) {
    this.degree = degree;
  }

  public Agent getToAffinity() {
    return toAffinity;
  }

  public AgentAffinityAgent toAffinity(Agent agent) {
    this.toAffinity = agent;
    return this;
  }

  public void setToAffinity(Agent agent) {
    this.toAffinity = agent;
  }

  public Agent getFromAffinity() {
    return fromAffinity;
  }

  public AgentAffinityAgent fromAffinity(Agent agent) {
    this.fromAffinity = agent;
    return this;
  }

  public void setFromAffinity(Agent agent) {
    this.fromAffinity = agent;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AgentAffinityAgent)) {
      return false;
    }
    return id != null && id.equals(((AgentAffinityAgent) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "AgentAffinityAgent{" + "id=" + getId() + ", degree=" + getDegree() + "}";
  }

  public void removeFromFromAffinity() {
    if (this.fromAffinity != null) {
      this.fromAffinity.removeToAgentAffinity(this);
    }
  }

  public void removeFromToAffinity() {
    if (this.toAffinity != null) {
      this.toAffinity.removeToAgentAffinity(this);
    }
  }
}
