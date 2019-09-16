package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.AgentInstSuggestionToAgent} entity. */
public class AgentInstSuggestionToAgentDTO implements Serializable {

  private Long id;

  private Float orderCriteriaResult;

  private Long theInstAgSugId;

  private Long theAgentId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getOrderCriteriaResult() {
    return orderCriteriaResult;
  }

  public void setOrderCriteriaResult(Float orderCriteriaResult) {
    this.orderCriteriaResult = orderCriteriaResult;
  }

  public Long getTheInstAgSugId() {
    return theInstAgSugId;
  }

  public void setTheInstAgSugId(Long peopleInstSugId) {
    this.theInstAgSugId = peopleInstSugId;
  }

  public Long getTheAgentId() {
    return theAgentId;
  }

  public void setTheAgentId(Long agentId) {
    this.theAgentId = agentId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AgentInstSuggestionToAgentDTO agentInstSuggestionToAgentDTO = (AgentInstSuggestionToAgentDTO) o;
    if (agentInstSuggestionToAgentDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), agentInstSuggestionToAgentDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AgentInstSuggestionToAgentDTO{"
        + "id="
        + getId()
        + ", orderCriteriaResult="
        + getOrderCriteriaResult()
        + ", theInstAgSug="
        + getTheInstAgSugId()
        + ", theAgent="
        + getTheAgentId()
        + "}";
  }
}
