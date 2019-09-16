package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Event} entity. */
public class EventDTO implements Serializable {

  private Long id;

  @Lob private String why;

  private LocalDate when;

  private Boolean isCreatedByApsee;

  private Long theAgendaEventSubId;

  private Long theCatalogEventSubId;

  private Long theConnectionEventSubId;

  private Long theGlobalActivityEventSubId;

  private Long theModelingActivityEventSubId;

  private Long theProcessEventSubId;

  private Long theProcessModelEventSubId;

  private Long theResourceEventSubId;

  private Long theCatalogEventsId;

  private Long theTaskId;

  private Long theLogId;

  private Long theEventTypeId;

  private Long theActivityId;

  private Long thePlainId;

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

  public LocalDate getWhen() {
    return when;
  }

  public void setWhen(LocalDate when) {
    this.when = when;
  }

  public Boolean isIsCreatedByApsee() {
    return isCreatedByApsee;
  }

  public void setIsCreatedByApsee(Boolean isCreatedByApsee) {
    this.isCreatedByApsee = isCreatedByApsee;
  }

  public Long getTheAgendaEventSubId() {
    return theAgendaEventSubId;
  }

  public void setTheAgendaEventSubId(Long agendaEventId) {
    this.theAgendaEventSubId = agendaEventId;
  }

  public Long getTheCatalogEventSubId() {
    return theCatalogEventSubId;
  }

  public void setTheCatalogEventSubId(Long catalogEventId) {
    this.theCatalogEventSubId = catalogEventId;
  }

  public Long getTheConnectionEventSubId() {
    return theConnectionEventSubId;
  }

  public void setTheConnectionEventSubId(Long connectionEventId) {
    this.theConnectionEventSubId = connectionEventId;
  }

  public Long getTheGlobalActivityEventSubId() {
    return theGlobalActivityEventSubId;
  }

  public void setTheGlobalActivityEventSubId(Long globalActivityEventId) {
    this.theGlobalActivityEventSubId = globalActivityEventId;
  }

  public Long getTheModelingActivityEventSubId() {
    return theModelingActivityEventSubId;
  }

  public void setTheModelingActivityEventSubId(Long modelingActivityEventId) {
    this.theModelingActivityEventSubId = modelingActivityEventId;
  }

  public Long getTheProcessEventSubId() {
    return theProcessEventSubId;
  }

  public void setTheProcessEventSubId(Long processEventId) {
    this.theProcessEventSubId = processEventId;
  }

  public Long getTheProcessModelEventSubId() {
    return theProcessModelEventSubId;
  }

  public void setTheProcessModelEventSubId(Long processModelEventId) {
    this.theProcessModelEventSubId = processModelEventId;
  }

  public Long getTheResourceEventSubId() {
    return theResourceEventSubId;
  }

  public void setTheResourceEventSubId(Long resourceEventId) {
    this.theResourceEventSubId = resourceEventId;
  }

  public Long getTheCatalogEventsId() {
    return theCatalogEventsId;
  }

  public void setTheCatalogEventsId(Long catalogEventId) {
    this.theCatalogEventsId = catalogEventId;
  }

  public Long getTheTaskId() {
    return theTaskId;
  }

  public void setTheTaskId(Long taskId) {
    this.theTaskId = taskId;
  }

  public Long getTheLogId() {
    return theLogId;
  }

  public void setTheLogId(Long spmLogId) {
    this.theLogId = spmLogId;
  }

  public Long getTheEventTypeId() {
    return theEventTypeId;
  }

  public void setTheEventTypeId(Long eventTypeId) {
    this.theEventTypeId = eventTypeId;
  }

  public Long getTheActivityId() {
    return theActivityId;
  }

  public void setTheActivityId(Long activityId) {
    this.theActivityId = activityId;
  }

  public Long getThePlainId() {
    return thePlainId;
  }

  public void setThePlainId(Long plainId) {
    this.thePlainId = plainId;
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

    EventDTO eventDTO = (EventDTO) o;
    if (eventDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), eventDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "EventDTO{"
        + "id="
        + getId()
        + ", why='"
        + getWhy()
        + "'"
        + ", when='"
        + getWhen()
        + "'"
        + ", isCreatedByApsee='"
        + isIsCreatedByApsee()
        + "'"
        + ", theAgendaEventSub="
        + getTheAgendaEventSubId()
        + ", theCatalogEventSub="
        + getTheCatalogEventSubId()
        + ", theConnectionEventSub="
        + getTheConnectionEventSubId()
        + ", theGlobalActivityEventSub="
        + getTheGlobalActivityEventSubId()
        + ", theModelingActivityEventSub="
        + getTheModelingActivityEventSubId()
        + ", theProcessEventSub="
        + getTheProcessEventSubId()
        + ", theProcessModelEventSub="
        + getTheProcessModelEventSubId()
        + ", theResourceEventSub="
        + getTheResourceEventSubId()
        + ", theCatalogEvents="
        + getTheCatalogEventsId()
        + ", theTask="
        + getTheTaskId()
        + ", theLog="
        + getTheLogId()
        + ", theEventType="
        + getTheEventTypeId()
        + ", theActivity="
        + getTheActivityId()
        + ", thePlain="
        + getThePlainId()
        + ", theAgent="
        + getTheAgentId()
        + "}";
  }
}
