package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ArtifactType} entity. */
public class ArtifactTypeDTO implements Serializable {

  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ArtifactTypeDTO artifactTypeDTO = (ArtifactTypeDTO) o;
    if (artifactTypeDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), artifactTypeDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ArtifactTypeDTO{" + "id=" + getId() + "}";
  }
}
