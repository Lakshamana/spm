package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.AgentMetric} entity. */
public class AgentMetricDTO implements Serializable {

  private Long id;

  private Long theAgentId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

    AgentMetricDTO agentMetricDTO = (AgentMetricDTO) o;
    if (agentMetricDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), agentMetricDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AgentMetricDTO{" + "id=" + getId() + ", theAgent=" + getTheAgentId() + "}";
  }
}
