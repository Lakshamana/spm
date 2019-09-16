package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import br.ufpa.labes.spm.domain.enumeration.EmailSecurityLevels;
import br.ufpa.labes.spm.domain.enumeration.EmailNotificationConfig;
import br.ufpa.labes.spm.domain.enumeration.EmailProcessStatusNotifications;

/** A DTO for the {@link br.ufpa.labes.spm.domain.EmailConfiguration} entity. */
public class EmailConfigurationDTO implements Serializable {

  private Long id;

  private Boolean processFinished;

  private Boolean firstActStarted;

  private Boolean consumableResourceAmount;

  private Boolean activityInstantied;

  private Boolean taskDelegated;

  private EmailSecurityLevels securityLevels;

  private EmailNotificationConfig notificationConfig;

  private EmailProcessStatusNotifications processNotifications;

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

  public EmailSecurityLevels getSecurityLevels() {
    return securityLevels;
  }

  public void setSecurityLevels(EmailSecurityLevels securityLevels) {
    this.securityLevels = securityLevels;
  }

  public EmailNotificationConfig getNotificationConfig() {
    return notificationConfig;
  }

  public void setNotificationConfig(EmailNotificationConfig notificationConfig) {
    this.notificationConfig = notificationConfig;
  }

  public EmailProcessStatusNotifications getProcessNotifications() {
    return processNotifications;
  }

  public void setProcessNotifications(EmailProcessStatusNotifications processNotifications) {
    this.processNotifications = processNotifications;
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
        + ", securityLevels='"
        + getSecurityLevels()
        + "'"
        + ", notificationConfig='"
        + getNotificationConfig()
        + "'"
        + ", processNotifications='"
        + getProcessNotifications()
        + "'"
        + "}";
  }
}
