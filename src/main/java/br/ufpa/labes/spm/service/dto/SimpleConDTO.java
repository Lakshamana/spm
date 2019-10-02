package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.SimpleCon} entity. */
public class SimpleConDTO implements Serializable {

  private Long id;

  private Long toActivityId;

  private Long fromActivityId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getToActivityId() {
    return toActivityId;
  }

  public void setToActivityId(Long activityId) {
    this.toActivityId = activityId;
  }

  public Long getFromActivityId() {
    return fromActivityId;
  }

  public void setFromActivityId(Long activityId) {
    this.fromActivityId = activityId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SimpleConDTO simpleConDTO = (SimpleConDTO) o;
    if (simpleConDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), simpleConDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "SimpleConDTO{"
        + "id="
        + getId()
        + ", toActivity="
        + getToActivityId()
        + ", fromActivity="
        + getFromActivityId()
        + "}";
  }
}
