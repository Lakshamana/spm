package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Type} entity. */
public class TypeDTO implements Serializable {

  private Long id;

  private String ident;

  @Lob private String description;

  private Boolean userDefined;

  private Long theAbilityTypeSubId;

  private Long theActivityTypeSubId;

  private Long theArtifactTypeSubId;

  private Long theConnectionTypeSubId;

  private Long theEventTypeSubId;

  private Long theWorkGroupTypeSubId;

  private Long theMetricTypeSubId;

  private Long theResourceTypeSubId;

  private Long theRoleTypeSubId;

  private Long theToolTypeSubId;

  private Long superTypeId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdent() {
    return ident;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean isUserDefined() {
    return userDefined;
  }

  public void setUserDefined(Boolean userDefined) {
    this.userDefined = userDefined;
  }

  public Long getTheAbilityTypeSubId() {
    return theAbilityTypeSubId;
  }

  public void setTheAbilityTypeSubId(Long abilityTypeId) {
    this.theAbilityTypeSubId = abilityTypeId;
  }

  public Long getTheActivityTypeSubId() {
    return theActivityTypeSubId;
  }

  public void setTheActivityTypeSubId(Long activityTypeId) {
    this.theActivityTypeSubId = activityTypeId;
  }

  public Long getTheArtifactTypeSubId() {
    return theArtifactTypeSubId;
  }

  public void setTheArtifactTypeSubId(Long artifactTypeId) {
    this.theArtifactTypeSubId = artifactTypeId;
  }

  public Long getTheConnectionTypeSubId() {
    return theConnectionTypeSubId;
  }

  public void setTheConnectionTypeSubId(Long connectionTypeId) {
    this.theConnectionTypeSubId = connectionTypeId;
  }

  public Long getTheEventTypeSubId() {
    return theEventTypeSubId;
  }

  public void setTheEventTypeSubId(Long eventTypeId) {
    this.theEventTypeSubId = eventTypeId;
  }

  public Long getTheWorkGroupTypeSubId() {
    return theWorkGroupTypeSubId;
  }

  public void setTheWorkGroupTypeSubId(Long workGroupTypeId) {
    this.theWorkGroupTypeSubId = workGroupTypeId;
  }

  public Long getTheMetricTypeSubId() {
    return theMetricTypeSubId;
  }

  public void setTheMetricTypeSubId(Long metricTypeId) {
    this.theMetricTypeSubId = metricTypeId;
  }

  public Long getTheResourceTypeSubId() {
    return theResourceTypeSubId;
  }

  public void setTheResourceTypeSubId(Long resourceTypeId) {
    this.theResourceTypeSubId = resourceTypeId;
  }

  public Long getTheRoleTypeSubId() {
    return theRoleTypeSubId;
  }

  public void setTheRoleTypeSubId(Long roleTypeId) {
    this.theRoleTypeSubId = roleTypeId;
  }

  public Long getTheToolTypeSubId() {
    return theToolTypeSubId;
  }

  public void setTheToolTypeSubId(Long toolTypeId) {
    this.theToolTypeSubId = toolTypeId;
  }

  public Long getSuperTypeId() {
    return superTypeId;
  }

  public void setSuperTypeId(Long typeId) {
    this.superTypeId = typeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TypeDTO typeDTO = (TypeDTO) o;
    if (typeDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), typeDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "TypeDTO{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + ", userDefined='"
        + isUserDefined()
        + "'"
        + ", theAbilityTypeSub="
        + getTheAbilityTypeSubId()
        + ", theActivityTypeSub="
        + getTheActivityTypeSubId()
        + ", theArtifactTypeSub="
        + getTheArtifactTypeSubId()
        + ", theConnectionTypeSub="
        + getTheConnectionTypeSubId()
        + ", theEventTypeSub="
        + getTheEventTypeSubId()
        + ", theWorkGroupTypeSub="
        + getTheWorkGroupTypeSubId()
        + ", theMetricTypeSub="
        + getTheMetricTypeSubId()
        + ", theResourceTypeSub="
        + getTheResourceTypeSubId()
        + ", theRoleTypeSub="
        + getTheRoleTypeSubId()
        + ", theToolTypeSub="
        + getTheToolTypeSubId()
        + ", superType="
        + getSuperTypeId()
        + "}";
  }
}
