package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A Calendar. */
@Entity
@Table(name = "calendar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Calendar implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "theCalendar")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<CalendarDay> notWorkingDays = new HashSet<>();

  @OneToMany(mappedBy = "theCalendar")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Project> projects = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Calendar name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<CalendarDay> getNotWorkingDays() {
    return notWorkingDays;
  }

  public Calendar notWorkingDays(Set<CalendarDay> calendarDays) {
    this.notWorkingDays = calendarDays;
    return this;
  }

  public Calendar addNotWorkingDays(CalendarDay calendarDay) {
    this.notWorkingDays.add(calendarDay);
    calendarDay.setTheCalendar(this);
    return this;
  }

  public Calendar removeNotWorkingDays(CalendarDay calendarDay) {
    this.notWorkingDays.remove(calendarDay);
    calendarDay.setTheCalendar(null);
    return this;
  }

  public void setNotWorkingDays(Set<CalendarDay> calendarDays) {
    this.notWorkingDays = calendarDays;
  }

  public Set<Project> getProjects() {
    return projects;
  }

  public Calendar projects(Set<Project> projects) {
    this.projects = projects;
    return this;
  }

  public Calendar addProject(Project project) {
    this.projects.add(project);
    project.setTheCalendar(this);
    return this;
  }

  public Calendar removeProject(Project project) {
    this.projects.remove(project);
    project.setTheCalendar(null);
    return this;
  }

  public void setProjects(Set<Project> projects) {
    this.projects = projects;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Calendar)) {
      return false;
    }
    return id != null && id.equals(((Calendar) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Calendar{" + "id=" + getId() + ", name='" + getName() + "'" + "}";
  }
}
