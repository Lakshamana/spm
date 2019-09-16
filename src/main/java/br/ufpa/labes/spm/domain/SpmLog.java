package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A SpmLog. */
@Entity
@Table(name = "spm_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SpmLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(unique = true)
  private Process theProcess;

  @OneToMany(mappedBy = "theLog")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Event> theEvents = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Process getTheProcess() {
    return theProcess;
  }

  public SpmLog theProcess(Process process) {
    this.theProcess = process;
    return this;
  }

  public void setTheProcess(Process process) {
    this.theProcess = process;
  }

  public Set<Event> getTheEvents() {
    return theEvents;
  }

  public SpmLog theEvents(Set<Event> events) {
    this.theEvents = events;
    return this;
  }

  public SpmLog addTheEvent(Event event) {
    this.theEvents.add(event);
    event.setTheLog(this);
    return this;
  }

  public SpmLog removeTheEvent(Event event) {
    this.theEvents.remove(event);
    event.setTheLog(null);
    return this;
  }

  public void setTheEvents(Set<Event> events) {
    this.theEvents = events;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SpmLog)) {
      return false;
    }
    return id != null && id.equals(((SpmLog) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "SpmLog{" + "id=" + getId() + "}";
  }
}
