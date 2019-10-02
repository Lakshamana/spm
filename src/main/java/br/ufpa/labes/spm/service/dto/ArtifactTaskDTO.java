package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ArtifactTask} entity. */
public class ArtifactTaskDTO implements Serializable {

  private Long id;

  private String inWorkspaceVersion;

  private String outWorkspaceVersion;

  private Long theArtifactId;

  private Long theTaskId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getInWorkspaceVersion() {
    return inWorkspaceVersion;
  }

  public void setInWorkspaceVersion(String inWorkspaceVersion) {
    this.inWorkspaceVersion = inWorkspaceVersion;
  }

  public String getOutWorkspaceVersion() {
    return outWorkspaceVersion;
  }

  public void setOutWorkspaceVersion(String outWorkspaceVersion) {
    this.outWorkspaceVersion = outWorkspaceVersion;
  }

  public Long getTheArtifactId() {
    return theArtifactId;
  }

  public void setTheArtifactId(Long artifactId) {
    this.theArtifactId = artifactId;
  }

  public Long getTheTaskId() {
    return theTaskId;
  }

  public void setTheTaskId(Long taskId) {
    this.theTaskId = taskId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ArtifactTaskDTO artifactTaskDTO = (ArtifactTaskDTO) o;
    if (artifactTaskDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), artifactTaskDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ArtifactTaskDTO{"
        + "id="
        + getId()
        + ", inWorkspaceVersion='"
        + getInWorkspaceVersion()
        + "'"
        + ", outWorkspaceVersion='"
        + getOutWorkspaceVersion()
        + "'"
        + ", theArtifact="
        + getTheArtifactId()
        + ", theTask="
        + getTheTaskId()
        + "}";
  }
}
