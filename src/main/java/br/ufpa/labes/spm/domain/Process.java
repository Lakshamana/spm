package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.ufpa.labes.spm.domain.enumeration.ProcessStatus;

/** A Process. */
@Entity
@Table(name = "process")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
public class Process implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ident")
  private String ident;

  @Enumerated(EnumType.STRING)
  @Column(name = "p_status")
  private ProcessStatus pStatus;

  @OneToOne
  @JoinColumn(unique = true)
  private ProcessModel theProcessModel;

  @OneToMany(mappedBy = "theProcess")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ProcessAgenda> theProcessAgendas = new HashSet<>();

  @ManyToOne
  @JsonIgnoreProperties("theProcesses")
  private ActivityType theActivityType;

  @OneToOne(mappedBy = "theProcess")
  @JsonIgnore
  private SpmLog theLog;

  @ManyToOne
  @JsonIgnoreProperties("theProcesses")
  private EmailConfiguration theEmailConfiguration;

  @OneToMany(mappedBy = "theProcess")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ProcessEvent> theProcessEvents = new HashSet<>();

  @OneToMany(mappedBy = "processRefered")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Project> theProjects = new HashSet<>();

  @OneToMany(mappedBy = "theProcess")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ProcessMetric> theProcessMetrics = new HashSet<>();

  @OneToMany(mappedBy = "theProcess")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ProcessEstimation> theProcessEstimations = new HashSet<>();

  @ManyToMany(mappedBy = "theProcesses")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<Agent> theAgents = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdent() {
    return ident;
  }

  public Process ident(String ident) {
    this.ident = ident;
    return this;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public ProcessStatus getpStatus() {
    return pStatus;
  }

  public Process pStatus(ProcessStatus pStatus) {
    this.pStatus = pStatus;
    return this;
  }

  public void setpStatus(ProcessStatus pStatus) {
    this.pStatus = pStatus;
  }

  public ProcessModel getTheProcessModel() {
    return theProcessModel;
  }

  public Process theProcessModel(ProcessModel processModel) {
    this.theProcessModel = processModel;
    return this;
  }

  public void setTheProcessModel(ProcessModel processModel) {
    this.theProcessModel = processModel;
  }

  public Set<ProcessAgenda> getTheProcessAgendas() {
    return theProcessAgendas;
  }

  public Process theProcessAgendas(Set<ProcessAgenda> processAgenda) {
    this.theProcessAgendas = processAgenda;
    return this;
  }

  public Process addTheProcessAgendas(ProcessAgenda processAgenda) {
    this.theProcessAgendas.add(processAgenda);
    processAgenda.setTheProcess(this);
    return this;
  }

  public Process removeTheProcessAgendas(ProcessAgenda processAgenda) {
    this.theProcessAgendas.remove(processAgenda);
    processAgenda.setTheProcess(null);
    return this;
  }

  public void setTheProcessAgendas(Set<ProcessAgenda> processAgenda) {
    this.theProcessAgendas = processAgenda;
  }

  public ActivityType getTheActivityType() {
    return theActivityType;
  }

  public Process theActivityType(ActivityType activityType) {
    this.theActivityType = activityType;
    return this;
  }

  public void setTheActivityType(ActivityType activityType) {
    this.theActivityType = activityType;
  }

  public SpmLog getTheLog() {
    return theLog;
  }

  public Process theLog(SpmLog spmLog) {
    this.theLog = spmLog;
    return this;
  }

  public void setTheLog(SpmLog spmLog) {
    this.theLog = spmLog;
  }

  public EmailConfiguration getTheEmailConfiguration() {
    return theEmailConfiguration;
  }

  public Process theEmailConfiguration(EmailConfiguration emailConfiguration) {
    this.theEmailConfiguration = emailConfiguration;
    return this;
  }

  public void setTheEmailConfiguration(EmailConfiguration emailConfiguration) {
    this.theEmailConfiguration = emailConfiguration;
  }

  public Set<ProcessEvent> getTheProcessEvents() {
    return theProcessEvents;
  }

  public Process theProcessEvents(Set<ProcessEvent> processEvents) {
    this.theProcessEvents = processEvents;
    return this;
  }

  public Process addTheProcessEvent(ProcessEvent processEvent) {
    this.theProcessEvents.add(processEvent);
    processEvent.setTheProcess(this);
    return this;
  }

  public Process removeTheProcessEvent(ProcessEvent processEvent) {
    this.theProcessEvents.remove(processEvent);
    processEvent.setTheProcess(null);
    return this;
  }

  public void setTheProcessEvents(Set<ProcessEvent> processEvents) {
    this.theProcessEvents = processEvents;
  }

  public Set<Project> getTheProjects() {
    return theProjects;
  }

  public Process theProjects(Set<Project> projects) {
    this.theProjects = projects;
    return this;
  }

  public Process addTheProject(Project project) {
    this.theProjects.add(project);
    project.setProcessRefered(this);
    return this;
  }

  public Process removeTheProject(Project project) {
    this.theProjects.remove(project);
    project.setProcessRefered(null);
    return this;
  }

  public void setTheProjects(Set<Project> projects) {
    this.theProjects = projects;
  }

  public Set<ProcessMetric> getTheProcessMetrics() {
    return theProcessMetrics;
  }

  public Process theProcessMetrics(Set<ProcessMetric> processMetrics) {
    this.theProcessMetrics = processMetrics;
    return this;
  }

  public Process addTheProcessMetric(ProcessMetric processMetric) {
    this.theProcessMetrics.add(processMetric);
    processMetric.setTheProcess(this);
    return this;
  }

  public Process removeTheProcessMetric(ProcessMetric processMetric) {
    this.theProcessMetrics.remove(processMetric);
    processMetric.setTheProcess(null);
    return this;
  }

  public void setTheProcessMetrics(Set<ProcessMetric> processMetrics) {
    this.theProcessMetrics = processMetrics;
  }

  public Set<ProcessEstimation> getTheProcessEstimations() {
    return theProcessEstimations;
  }

  public Process theProcessEstimations(Set<ProcessEstimation> processEstimations) {
    this.theProcessEstimations = processEstimations;
    return this;
  }

  public Process addTheProcessEstimation(ProcessEstimation processEstimation) {
    this.theProcessEstimations.add(processEstimation);
    processEstimation.setTheProcess(this);
    return this;
  }

  public Process removeTheProcessEstimation(ProcessEstimation processEstimation) {
    this.theProcessEstimations.remove(processEstimation);
    processEstimation.setTheProcess(null);
    return this;
  }

  public void setTheProcessEstimations(Set<ProcessEstimation> processEstimations) {
    this.theProcessEstimations = processEstimations;
  }

  public Set<Agent> getTheAgents() {
    return theAgents;
  }

  public Process theAgents(Set<Agent> agents) {
    this.theAgents = agents;
    return this;
  }

  public Process addTheAgent(Agent agent) {
    this.theAgents.add(agent);
    agent.getTheProcesses().add(this);
    return this;
  }

  public Process removeTheAgent(Agent agent) {
    this.theAgents.remove(agent);
    agent.getTheProcesses().remove(this);
    return this;
  }

  public void setTheAgents(Set<Agent> agents) {
    this.theAgents = agents;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Process)) {
      return false;
    }
    return id != null && id.equals(((Process) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Process{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", pStatus='"
        + getpStatus()
        + "'"
        + "}";
  }
}
