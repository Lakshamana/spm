package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.InvolvedArtifact} entity. */
public class InvolvedArtifactDTO implements Serializable {

  private Long id;

  private Long inInvolvedArtifactsId;

  private Long outInvolvedArtifactsId;

  private Long theArtifactTypeId;

  private Long theArtifactId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getInInvolvedArtifactsId() {
    return inInvolvedArtifactsId;
  }

  public void setInInvolvedArtifactsId(Long normalId) {
    this.inInvolvedArtifactsId = normalId;
  }

  public Long getOutInvolvedArtifactsId() {
    return outInvolvedArtifactsId;
  }

  public void setOutInvolvedArtifactsId(Long normalId) {
    this.outInvolvedArtifactsId = normalId;
  }

  public Long getTheArtifactTypeId() {
    return theArtifactTypeId;
  }

  public void setTheArtifactTypeId(Long artifactTypeId) {
    this.theArtifactTypeId = artifactTypeId;
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

    InvolvedArtifactDTO involvedArtifactDTO = (InvolvedArtifactDTO) o;
    if (involvedArtifactDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), involvedArtifactDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "InvolvedArtifactDTO{"
        + "id="
        + getId()
        + ", inInvolvedArtifacts="
        + getInInvolvedArtifactsId()
        + ", outInvolvedArtifacts="
        + getOutInvolvedArtifactsId()
        + ", theArtifactType="
        + getTheArtifactTypeId()
        + ", theArtifact="
        + getTheArtifactId()
        + "}";
  }
}
