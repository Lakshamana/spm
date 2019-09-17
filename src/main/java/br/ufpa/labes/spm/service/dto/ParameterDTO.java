package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Parameter} entity. */
public class ParameterDTO implements Serializable {

  private Long id;

  @Lob private String description;

  private Long theAutomaticId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getTheAutomaticId() {
    return theAutomaticId;
  }

  public void setTheAutomaticId(Long automaticId) {
    this.theAutomaticId = automaticId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ParameterDTO parameterDTO = (ParameterDTO) o;
    if (parameterDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), parameterDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ParameterDTO{"
        + "id="
        + getId()
        + ", description='"
        + getDescription()
        + "'"
        + ", theAutomatic="
        + getTheAutomaticId()
        + "}";
  }
}
