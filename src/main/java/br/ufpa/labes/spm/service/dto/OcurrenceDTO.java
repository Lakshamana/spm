package br.ufpa.labes.spm.service.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Ocurrence} entity. */
public class OcurrenceDTO implements Serializable {

  private Long id;

  private LocalDate date;

  private Instant time;

  private String event;

  private Long theTaskId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Instant getTime() {
    return time;
  }

  public void setTime(Instant time) {
    this.time = time;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public Long getTheTaskId() {
    return theTaskId;
  }

  public void setTheTaskId(Long taskId) {
    this.theTaskId = taskId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    OcurrenceDTO ocurrenceDTO = (OcurrenceDTO) o;
    if (ocurrenceDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), ocurrenceDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "OcurrenceDTO{"
        + "id="
        + getId()
        + ", date='"
        + getDate()
        + "'"
        + ", time='"
        + getTime()
        + "'"
        + ", event='"
        + getEvent()
        + "'"
        + ", theTask="
        + getTheTaskId()
        + "}";
  }
}
