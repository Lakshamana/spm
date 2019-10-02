package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.CalendarDay} entity. */
public class CalendarDayDTO implements Serializable {

  private Long id;

  private String day;

  private Long theCalendarId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public Long getTheCalendarId() {
    return theCalendarId;
  }

  public void setTheCalendarId(Long calendarId) {
    this.theCalendarId = calendarId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CalendarDayDTO calendarDayDTO = (CalendarDayDTO) o;
    if (calendarDayDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), calendarDayDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "CalendarDayDTO{"
        + "id="
        + getId()
        + ", day='"
        + getDay()
        + "'"
        + ", theCalendar="
        + getTheCalendarId()
        + "}";
  }
}
