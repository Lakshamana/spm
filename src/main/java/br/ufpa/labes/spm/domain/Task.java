package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/** A Task. */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "local_state")
  private String localState;

  @Column(name = "begin_date")
  private LocalDate beginDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "working_hours")
  private Float workingHours;

  @Column(name = "date_delegated_to")
  private LocalDate dateDelegatedTo;

  @Column(name = "date_delegated_from")
  private LocalDate dateDelegatedFrom;

  @ManyToOne
  @JsonIgnoreProperties("delegates")
  private Agent delegatedFrom;

  @ManyToOne
  @JsonIgnoreProperties("isDelegatedFors")
  private Agent delegatedTo;

  @ManyToOne
  @JsonIgnoreProperties("theTasks")
  private Normal theNormal;

  @ManyToOne
  @JsonIgnoreProperties("theTasks")
  private ProcessAgenda theProcessAgenda;

  @OneToMany(mappedBy = "theTask")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ArtifactTask> theArtifactTasks = new HashSet<>();

  @OneToMany(mappedBy = "theTask")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<AgendaEvent> theAgendaEvents = new HashSet<>();

  @OneToMany(mappedBy = "theTask")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Ocurrence> ocurrences = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLocalState() {
    return localState;
  }

  public Task localState(String localState) {
    this.localState = localState;
    return this;
  }

  public void setLocalState(String localState) {
    this.localState = localState;
  }

  public LocalDate getBeginDate() {
    return beginDate;
  }

  public Task beginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
    return this;
  }

  public void setBeginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public Task endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Float getWorkingHours() {
    return workingHours;
  }

  public Task workingHours(Float workingHours) {
    this.workingHours = workingHours;
    return this;
  }

  public void setWorkingHours(Float workingHours) {
    this.workingHours = workingHours;
  }

  public LocalDate getDateDelegatedTo() {
    return dateDelegatedTo;
  }

  public Task dateDelegatedTo(LocalDate dateDelegatedTo) {
    this.dateDelegatedTo = dateDelegatedTo;
    return this;
  }

  public void setDateDelegatedTo(LocalDate dateDelegatedTo) {
    this.dateDelegatedTo = dateDelegatedTo;
  }

  public LocalDate getDateDelegatedFrom() {
    return dateDelegatedFrom;
  }

  public Task dateDelegatedFrom(LocalDate dateDelegatedFrom) {
    this.dateDelegatedFrom = dateDelegatedFrom;
    return this;
  }

  public void setDateDelegatedFrom(LocalDate dateDelegatedFrom) {
    this.dateDelegatedFrom = dateDelegatedFrom;
  }

  public Agent getDelegatedFrom() {
    return delegatedFrom;
  }

  public Task delegatedFrom(Agent agent) {
    this.delegatedFrom = agent;
    return this;
  }

  public void setDelegatedFrom(Agent agent) {
    this.delegatedFrom = agent;
  }

  public Agent getDelegatedTo() {
    return delegatedTo;
  }

  public Task delegatedTo(Agent agent) {
    this.delegatedTo = agent;
    return this;
  }

  public void setDelegatedTo(Agent agent) {
    this.delegatedTo = agent;
  }

  public Normal getTheNormal() {
    return theNormal;
  }

  public Task theNormal(Normal normal) {
    this.theNormal = normal;
    return this;
  }

  public void setTheNormal(Normal normal) {
    this.theNormal = normal;
  }

  public ProcessAgenda getTheProcessAgenda() {
    return theProcessAgenda;
  }

  public Task theProcessAgenda(ProcessAgenda processAgenda) {
    this.theProcessAgenda = processAgenda;
    return this;
  }

  public void setTheProcessAgenda(ProcessAgenda processAgenda) {
    this.theProcessAgenda = processAgenda;
  }

  public Set<ArtifactTask> getTheArtifactTasks() {
    return theArtifactTasks;
  }

  public Task theArtifactTasks(Set<ArtifactTask> artifactTasks) {
    this.theArtifactTasks = artifactTasks;
    return this;
  }

  public Task addTheArtifactTask(ArtifactTask artifactTask) {
    this.theArtifactTasks.add(artifactTask);
    artifactTask.setTheTask(this);
    return this;
  }

  public Task removeTheArtifactTask(ArtifactTask artifactTask) {
    this.theArtifactTasks.remove(artifactTask);
    artifactTask.setTheTask(null);
    return this;
  }

  public void setTheArtifactTasks(Set<ArtifactTask> artifactTasks) {
    this.theArtifactTasks = artifactTasks;
  }

  public Set<AgendaEvent> getTheAgendaEvents() {
    return theAgendaEvents;
  }

  public Task theAgendaEvents(Set<AgendaEvent> agendaEvents) {
    this.theAgendaEvents = agendaEvents;
    return this;
  }

  public Task addTheAgendaEvent(AgendaEvent agendaEvent) {
    this.theAgendaEvents.add(agendaEvent);
    agendaEvent.setTheTask(this);
    return this;
  }

  public Task removeTheAgendaEvent(AgendaEvent agendaEvent) {
    this.theAgendaEvents.remove(agendaEvent);
    agendaEvent.setTheTask(null);
    return this;
  }

  public void setTheAgendaEvents(Set<AgendaEvent> agendaEvents) {
    this.theAgendaEvents = agendaEvents;
  }

  public Set<Ocurrence> getOcurrences() {
    return ocurrences;
  }

  public Task ocurrences(Set<Ocurrence> ocurrences) {
    this.ocurrences = ocurrences;
    return this;
  }

  public Task addOcurrence(Ocurrence ocurrence) {
    this.ocurrences.add(ocurrence);
    ocurrence.setTheTask(this);
    return this;
  }

  public Task removeOcurrence(Ocurrence ocurrence) {
    this.ocurrences.remove(ocurrence);
    ocurrence.setTheTask(null);
    return this;
  }

  public void setOcurrences(Set<Ocurrence> ocurrences) {
    this.ocurrences = ocurrences;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Task)) {
      return false;
    }
    return id != null && id.equals(((Task) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Task{"
        + "id="
        + getId()
        + ", localState='"
        + getLocalState()
        + "'"
        + ", beginDate='"
        + getBeginDate()
        + "'"
        + ", endDate='"
        + getEndDate()
        + "'"
        + ", workingHours="
        + getWorkingHours()
        + ", dateDelegatedTo='"
        + getDateDelegatedTo()
        + "'"
        + ", dateDelegatedFrom='"
        + getDateDelegatedFrom()
        + "'"
        + "}";
  }
}
