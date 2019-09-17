package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ArtifactCon} entity. */
public class ArtifactConDTO implements Serializable {

  private Long id;

  private Long theArtifactId;

  private Long theArtifactTypeId;

  private Set<MultipleConDTO> toMultipleCons = new HashSet<>();

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

  public Long getTheArtifactTypeId() {
    return theArtifactTypeId;
  }

  public void setTheArtifactTypeId(Long artifactTypeId) {
    this.theArtifactTypeId = artifactTypeId;
  }

  public Set<MultipleConDTO> getToMultipleCons() {
    return toMultipleCons;
  }

  public void setToMultipleCons(Set<MultipleConDTO> multipleCons) {
    this.toMultipleCons = multipleCons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ArtifactConDTO artifactConDTO = (ArtifactConDTO) o;
    if (artifactConDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), artifactConDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ArtifactConDTO{"
        + "id="
        + getId()
        + ", theArtifact="
        + getTheArtifactId()
        + ", theArtifactType="
        + getTheArtifactTypeId()
        + "}";
  }
}
