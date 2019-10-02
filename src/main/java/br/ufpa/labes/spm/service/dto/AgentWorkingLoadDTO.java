package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.AgentWorkingLoad} entity. */
public class AgentWorkingLoadDTO implements Serializable {

  private Long id;

  private LocalDate begin;

  private LocalDate end;

  private Long theAgentId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getBegin() {
    return begin;
  }

  public void setBegin(LocalDate begin) {
    this.begin = begin;
  }

  public LocalDate getEnd() {
    return end;
  }

  public void setEnd(LocalDate end) {
    this.end = end;
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

    AgentWorkingLoadDTO agentWorkingLoadDTO = (AgentWorkingLoadDTO) o;
    if (agentWorkingLoadDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), agentWorkingLoadDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AgentWorkingLoadDTO{"
        + "id="
        + getId()
        + ", begin='"
        + getBegin()
        + "'"
        + ", end='"
        + getEnd()
        + "'"
        + ", theAgent="
        + getTheAgentId()
        + "}";
  }
}
