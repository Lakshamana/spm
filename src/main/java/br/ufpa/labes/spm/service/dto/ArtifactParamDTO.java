package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ArtifactParam} entity. */
public class ArtifactParamDTO implements Serializable {

  private Long id;

  private Long theArtifactId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

    ArtifactParamDTO artifactParamDTO = (ArtifactParamDTO) o;
    if (artifactParamDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), artifactParamDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ArtifactParamDTO{" + "id=" + getId() + ", theArtifact=" + getTheArtifactId() + "}";
  }
}
