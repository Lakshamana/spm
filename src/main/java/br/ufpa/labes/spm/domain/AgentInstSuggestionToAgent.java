package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A AgentInstSuggestionToAgent. */
@Entity
@Table(name = "agent_inst_suggestion_to_agent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgentInstSuggestionToAgent implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_criteria_result")
  private Float orderCriteriaResult;

  @ManyToOne
  @JsonIgnoreProperties("theAgentInstSugToAgents")
  private PeopleInstSug theInstAgSug;

  @ManyToOne
  @JsonIgnoreProperties("theAgentInstSugToAgents")
  private Agent theAgent;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getOrderCriteriaResult() {
    return orderCriteriaResult;
  }

  public AgentInstSuggestionToAgent orderCriteriaResult(Float orderCriteriaResult) {
    this.orderCriteriaResult = orderCriteriaResult;
    return this;
  }

  public void setOrderCriteriaResult(Float orderCriteriaResult) {
    this.orderCriteriaResult = orderCriteriaResult;
  }

  public PeopleInstSug getTheInstAgSug() {
    return theInstAgSug;
  }

  public AgentInstSuggestionToAgent theInstAgSug(PeopleInstSug peopleInstSug) {
    this.theInstAgSug = peopleInstSug;
    return this;
  }

  public void setTheInstAgSug(PeopleInstSug peopleInstSug) {
    this.theInstAgSug = peopleInstSug;
  }

  public Agent getTheAgent() {
    return theAgent;
  }

  public AgentInstSuggestionToAgent theAgent(Agent agent) {
    this.theAgent = agent;
    return this;
  }

  public void setTheAgent(Agent agent) {
    this.theAgent = agent;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AgentInstSuggestionToAgent)) {
      return false;
    }
    return id != null && id.equals(((AgentInstSuggestionToAgent) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "AgentInstSuggestionToAgent{"
        + "id="
        + getId()
        + ", orderCriteriaResult="
        + getOrderCriteriaResult()
        + "}";
  }
}
