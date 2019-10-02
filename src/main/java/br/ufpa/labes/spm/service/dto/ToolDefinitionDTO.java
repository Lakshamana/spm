package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ToolDefinition} entity. */
public class ToolDefinitionDTO implements Serializable {

  private Long id;

  private String ident;

  private String name;

  @Lob private String description;

  private Long theToolTypeId;

  private Set<ArtifactTypeDTO> theArtifactTypes = new HashSet<>();

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getTheToolTypeId() {
    return theToolTypeId;
  }

  public void setTheToolTypeId(Long toolTypeId) {
    this.theToolTypeId = toolTypeId;
  }

  public Set<ArtifactTypeDTO> getTheArtifactTypes() {
    return theArtifactTypes;
  }

  public void setTheArtifactTypes(Set<ArtifactTypeDTO> artifactTypes) {
    this.theArtifactTypes = artifactTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ToolDefinitionDTO toolDefinitionDTO = (ToolDefinitionDTO) o;
    if (toolDefinitionDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), toolDefinitionDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ToolDefinitionDTO{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", name='"
        + getName()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + ", theToolType="
        + getTheToolTypeId()
        + "}";
  }
}
