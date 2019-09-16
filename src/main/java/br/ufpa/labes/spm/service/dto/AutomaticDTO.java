package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Automatic} entity. */
public class AutomaticDTO implements Serializable {

  private Long id;

  private Long theSubroutineId;

  private Long theArtifactId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTheSubroutineId() {
    return theSubroutineId;
  }

  public void setTheSubroutineId(Long subroutineId) {
    this.theSubroutineId = subroutineId;
  }

  public Long getTheArtifactId() {
    return theArtifactId;
  }

  public void setTheArtifactId(Long artifactId) {
    this.theArtifactId = artifactId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    AutomaticDTO automaticDTO = (AutomaticDTO) o;
    if (automaticDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), automaticDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "AutomaticDTO{"
        + "id="
        + getId()
        + ", theSubroutine="
        + getTheSubroutineId()
        + ", theArtifact="
        + getTheArtifactId()
        + "}";
  }
}
