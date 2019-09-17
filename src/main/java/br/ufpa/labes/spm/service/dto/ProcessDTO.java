package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import br.ufpa.labes.spm.domain.enumeration.ProcessStatus;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Process} entity. */
public class ProcessDTO implements Serializable {

  private Long id;

  private String ident;

  private ProcessStatus pStatus;

  private Long theProcessModelId;

  private Long theActivityTypeId;

  private Long theEmailConfigurationId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdent() {
    return ident;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public ProcessStatus getpStatus() {
    return pStatus;
  }

  public void setpStatus(ProcessStatus pStatus) {
    this.pStatus = pStatus;
  }

  public Long getTheProcessModelId() {
    return theProcessModelId;
  }

  public void setTheProcessModelId(Long processModelId) {
    this.theProcessModelId = processModelId;
  }

  public Long getTheActivityTypeId() {
    return theActivityTypeId;
  }

  public void setTheActivityTypeId(Long activityTypeId) {
    this.theActivityTypeId = activityTypeId;
  }

  public Long getTheEmailConfigurationId() {
    return theEmailConfigurationId;
  }

  public void setTheEmailConfigurationId(Long emailConfigurationId) {
    this.theEmailConfigurationId = emailConfigurationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ProcessDTO processDTO = (ProcessDTO) o;
    if (processDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), processDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ProcessDTO{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", pStatus='"
        + getpStatus()
        + "'"
        + ", theProcessModel="
        + getTheProcessModelId()
        + ", theActivityType="
        + getTheActivityTypeId()
        + ", theEmailConfiguration="
        + getTheEmailConfigurationId()
        + "}";
  }
}
