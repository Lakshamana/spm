package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A EmailConfiguration. */
@Entity
@Table(name = "email_configuration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmailConfiguration implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "process_finished")
  private Boolean processFinished;

  @Column(name = "first_act_started")
  private Boolean firstActStarted;

  @Column(name = "consumable_resource_amount")
  private Boolean consumableResourceAmount;

  @Column(name = "activity_instantied")
  private Boolean activityInstantied;

  @Column(name = "task_delegated")
  private Boolean taskDelegated;

  @Column(name = "decision_branch_cond")
  private Boolean decisionBranchCond;

  @OneToMany(mappedBy = "theEmailConfiguration")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Process> theProcesses = new HashSet<>();

  @OneToMany(mappedBy = "theEmailConfiguration")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Agent> theManagers = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean isProcessFinished() {
    return processFinished;
  }

  public EmailConfiguration processFinished(Boolean processFinished) {
    this.processFinished = processFinished;
    return this;
  }

  public void setProcessFinished(Boolean processFinished) {
    this.processFinished = processFinished;
  }

  public Boolean isFirstActStarted() {
    return firstActStarted;
  }

  public EmailConfiguration firstActStarted(Boolean firstActStarted) {
    this.firstActStarted = firstActStarted;
    return this;
  }

  public void setFirstActStarted(Boolean firstActStarted) {
    this.firstActStarted = firstActStarted;
  }

  public Boolean isConsumableResourceAmount() {
    return consumableResourceAmount;
  }

  public EmailConfiguration consumableResourceAmount(Boolean consumableResourceAmount) {
    this.consumableResourceAmount = consumableResourceAmount;
    return this;
  }

  public void setConsumableResourceAmount(Boolean consumableResourceAmount) {
    this.consumableResourceAmount = consumableResourceAmount;
  }

  public Boolean isActivityInstantied() {
    return activityInstantied;
  }

  public EmailConfiguration activityInstantied(Boolean activityInstantied) {
    this.activityInstantied = activityInstantied;
    return this;
  }

  public void setActivityInstantied(Boolean activityInstantied) {
    this.activityInstantied = activityInstantied;
  }

  public Boolean isTaskDelegated() {
    return taskDelegated;
  }

  public EmailConfiguration taskDelegated(Boolean taskDelegated) {
    this.taskDelegated = taskDelegated;
    return this;
  }

  public void setTaskDelegated(Boolean taskDelegated) {
    this.taskDelegated = taskDelegated;
  }

  public Boolean isDecisionBranchCond() {
    return decisionBranchCond;
  }

  public EmailConfiguration decisionBranchCond(Boolean decisionBranchCond) {
    this.decisionBranchCond = decisionBranchCond;
    return this;
  }

  public void setDecisionBranchCond(Boolean decisionBranchCond) {
    this.decisionBranchCond = decisionBranchCond;
  }

  public Set<Process> getTheProcesses() {
    return theProcesses;
  }

  public EmailConfiguration theProcesses(Set<Process> processes) {
    this.theProcesses = processes;
    return this;
  }

  public EmailConfiguration addTheProcesses(Process process) {
    this.theProcesses.add(process);
    process.setTheEmailConfiguration(this);
    return this;
  }

  public EmailConfiguration removeTheProcesses(Process process) {
    this.theProcesses.remove(process);
    process.setTheEmailConfiguration(null);
    return this;
  }

  public void setTheProcesses(Set<Process> processes) {
    this.theProcesses = processes;
  }

  public Set<Agent> getTheManagers() {
    return theManagers;
  }

  public EmailConfiguration theManagers(Set<Agent> agents) {
    this.theManagers = agents;
    return this;
  }

  public EmailConfiguration addTheManagers(Agent agent) {
    this.theManagers.add(agent);
    agent.setTheEmailConfiguration(this);
    return this;
  }

  public EmailConfiguration removeTheManagers(Agent agent) {
    this.theManagers.remove(agent);
    agent.setTheEmailConfiguration(null);
    return this;
  }

  public void setTheManagers(Set<Agent> agents) {
    this.theManagers = agents;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EmailConfiguration)) {
      return false;
    }
    return id != null && id.equals(((EmailConfiguration) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "EmailConfiguration{"
        + "id="
        + getId()
        + ", processFinished='"
        + isProcessFinished()
        + "'"
        + ", firstActStarted='"
        + isFirstActStarted()
        + "'"
        + ", consumableResourceAmount='"
        + isConsumableResourceAmount()
        + "'"
        + ", activityInstantied='"
        + isActivityInstantied()
        + "'"
        + ", taskDelegated='"
        + isTaskDelegated()
        + "'"
        + ", decisionBranchCond='"
        + isDecisionBranchCond()
        + "'"
        + "}";
  }
}
