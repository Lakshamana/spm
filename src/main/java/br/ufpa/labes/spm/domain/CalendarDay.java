package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A CalendarDay. */
@Entity
@Table(name = "calendar_day")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CalendarDay implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "day")
  private String day;

  @ManyToOne
  @JsonIgnoreProperties("notWorkingDays")
  private Calendar theCalendar;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDay() {
    return day;
  }

  public CalendarDay day(String day) {
    this.day = day;
    return this;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public Calendar getTheCalendar() {
    return theCalendar;
  }

  public CalendarDay theCalendar(Calendar calendar) {
    this.theCalendar = calendar;
    return this;
  }

  public void setTheCalendar(Calendar calendar) {
    this.theCalendar = calendar;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CalendarDay)) {
      return false;
    }
    return id != null && id.equals(((CalendarDay) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "CalendarDay{" + "id=" + getId() + ", day='" + getDay() + "'" + "}";
  }
}
