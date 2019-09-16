package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.OutOfWorkPeriod} entity. */
public class OutOfWorkPeriodDTO implements Serializable {

  private Long id;

  @Lob private String why;

  private String fromDate;

  private String toDate;

  private Long theAgentId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getWhy() {
    return why;
  }

  public void setWhy(String why) {
    this.why = why;
  }

  public String getFromDate() {
    return fromDate;
  }

  public void setFromDate(String fromDate) {
    this.fromDate = fromDate;
  }

  public String getToDate() {
    return toDate;
  }

  public void setToDate(String toDate) {
    this.toDate = toDate;
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

    OutOfWorkPeriodDTO outOfWorkPeriodDTO = (OutOfWorkPeriodDTO) o;
    if (outOfWorkPeriodDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), outOfWorkPeriodDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "OutOfWorkPeriodDTO{"
        + "id="
        + getId()
        + ", why='"
        + getWhy()
        + "'"
        + ", fromDate='"
        + getFromDate()
        + "'"
        + ", toDate='"
        + getToDate()
        + "'"
        + ", theAgent="
        + getTheAgentId()
        + "}";
  }
}
