package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ResourceEvent} entity. */
public class ResourceEventDTO implements Serializable {

  private Long id;

  private Long theNormalId;

  private Long theResourceId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTheNormalId() {
    return theNormalId;
  }

  public void setTheNormalId(Long normalId) {
    this.theNormalId = normalId;
  }

  public Long getTheResourceId() {
    return theResourceId;
  }

  public void setTheResourceId(Long resourceId) {
    this.theResourceId = resourceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResourceEventDTO resourceEventDTO = (ResourceEventDTO) o;
    if (resourceEventDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), resourceEventDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ResourceEventDTO{"
        + "id="
        + getId()
        + ", theNormal="
        + getTheNormalId()
        + ", theResource="
        + getTheResourceId()
        + "}";
  }
}
