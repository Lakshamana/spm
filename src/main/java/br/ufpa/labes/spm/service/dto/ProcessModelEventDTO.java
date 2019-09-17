package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ProcessModelEvent} entity. */
public class ProcessModelEventDTO implements Serializable {

  private Long id;

  private Long theProcessModelId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTheProcessModelId() {
    return theProcessModelId;
  }

  public void setTheProcessModelId(Long processModelId) {
    this.theProcessModelId = processModelId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ProcessModelEventDTO processModelEventDTO = (ProcessModelEventDTO) o;
    if (processModelEventDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), processModelEventDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ProcessModelEventDTO{"
        + "id="
        + getId()
        + ", theProcessModel="
        + getTheProcessModelId()
        + "}";
  }
}
