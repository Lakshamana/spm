package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.EmailConfiguration} entity. */
public class EmailConfigurationDTO implements Serializable {

  private Long id;

  private Boolean processFinished;

  private Boolean firstActStarted;

  private Boolean consumableResourceAmount;

  private Boolean activityInstantied;

  private Boolean taskDelegated;

  private Boolean decisionBranchCond;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean isProcessFinished() {
    return processFinished;
  }

  public void setProcessFinished(Boolean processFinished) {
    this.processFinished = processFinished;
  }

  public Boolean isFirstActStarted() {
    return firstActStarted;
  }

  public void setFirstActStarted(Boolean firstActStarted) {
    this.firstActStarted = firstActStarted;
  }

  public Boolean isConsumableResourceAmount() {
    return consumableResourceAmount;
  }

  public void setConsumableResourceAmount(Boolean consumableResourceAmount) {
    this.consumableResourceAmount = consumableResourceAmount;
  }

  public Boolean isActivityInstantied() {
    return activityInstantied;
  }

  public void setActivityInstantied(Boolean activityInstantied) {
    this.activityInstantied = activityInstantied;
  }

  public Boolean isTaskDelegated() {
    return taskDelegated;
  }

  public void setTaskDelegated(Boolean taskDelegated) {
    this.taskDelegated = taskDelegated;
  }

  public Boolean isDecisionBranchCond() {
    return decisionBranchCond;
  }

  public void setDecisionBranchCond(Boolean decisionBranchCond) {
    this.decisionBranchCond = decisionBranchCond;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EmailConfigurationDTO emailConfigurationDTO = (EmailConfigurationDTO) o;
    if (emailConfigurationDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), emailConfigurationDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "EmailConfigurationDTO{"
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
