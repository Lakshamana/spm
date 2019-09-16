package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.RequiredPeople} entity. */
public class RequiredPeopleDTO implements Serializable {

  private Long id;

  private Long theReqWorkGroupSubId;

  private Long theReqAgentSubId;

  private Long theNormalId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTheReqWorkGroupSubId() {
    return theReqWorkGroupSubId;
  }

  public void setTheReqWorkGroupSubId(Long reqWorkGroupId) {
    this.theReqWorkGroupSubId = reqWorkGroupId;
  }

  public Long getTheReqAgentSubId() {
    return theReqAgentSubId;
  }

  public void setTheReqAgentSubId(Long reqAgentId) {
    this.theReqAgentSubId = reqAgentId;
  }

  public Long getTheNormalId() {
    return theNormalId;
  }

  public void setTheNormalId(Long normalId) {
    this.theNormalId = normalId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RequiredPeopleDTO requiredPeopleDTO = (RequiredPeopleDTO) o;
    if (requiredPeopleDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), requiredPeopleDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "RequiredPeopleDTO{"
        + "id="
        + getId()
        + ", theReqWorkGroupSub="
        + getTheReqWorkGroupSubId()
        + ", theReqAgentSub="
        + getTheReqAgentSubId()
        + ", theNormal="
        + getTheNormalId()
        + "}";
  }
}
