package org.qrconsult.spm.dtos.agenda;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SimpleArtifactDescriptorDTO implements Serializable {
  private String artifactIdent;
  private String artifactName;
  private String artifactRemoteVersion;
  private String artifactLocalVersion;
  private String artifactFileName;
  private String repositoryIdent;

  public SimpleArtifactDescriptorDTO() {}

  public SimpleArtifactDescriptorDTO(
      String artifactIdent,
      String artifactName,
      String artifactVersion,
      String fileName,
      String repositoryIdent) {
    this.artifactIdent = artifactIdent;
    this.artifactName = artifactName;
    this.artifactRemoteVersion = artifactVersion;
    this.setArtifactFileName(fileName);
    this.setRepositoryIdent(repositoryIdent);
  }

  public String getArtifactIdent() {
    return artifactIdent;
  }

  public void setArtifactIdent(String artifactIdent) {
    this.artifactIdent = artifactIdent;
  }

  public String getArtifactName() {
    return artifactName;
  }

  public void setArtifactName(String artifactName) {
    this.artifactName = artifactName;
  }

  public String getArtifactRemoteVersion() {
    return artifactRemoteVersion;
  }

  public void setArtifactRemoteVersion(String artifactRemoteVersion) {
    this.artifactRemoteVersion = artifactRemoteVersion;
  }

  public String getArtifactLocalVersion() {
    return artifactLocalVersion;
  }

  public void setArtifactLocalVersion(String artifactLocalVersion) {
    this.artifactLocalVersion = artifactLocalVersion;
  }

  @Override
  public String toString() {
    return "SimpleArtifactDescriptorDTO [artifactIdent="
        + artifactIdent
        + ", artifactName="
        + artifactName
        + ", artifactRemoteVersion="
        + artifactRemoteVersion
        + ", artifactLocalVersion="
        + artifactLocalVersion
        + "]";
  }

  public String getArtifactFileName() {
    return artifactFileName;
  }

  public void setArtifactFileName(String artifactFileName) {
    this.artifactFileName = artifactFileName;
  }

  public String getRepositoryIdent() {
    return repositoryIdent;
  }

  public void setRepositoryIdent(String repositoryIdent) {
    this.repositoryIdent = repositoryIdent;
  }
}
