package br.ufpa.labes.spm.beans;

import java.io.Serializable;
import java.util.Hashtable;

public class ArtifactMngDownloadPackage implements Serializable {

  private Hashtable<String, ArtifactMngDownload[]> downloads;

  public ArtifactMngDownloadPackage() {
    this.downloads = new Hashtable<String, ArtifactMngDownload[]>();
  }

  public void addArtifact(String artifactIdent, ArtifactMngDownload[] revisions) {
    this.downloads.put(artifactIdent, revisions);
  }

  public ArtifactMngDownload[] getArtifactRevisions(String artifactIdent) {
    return this.downloads.get(artifactIdent);
  }

  public ArtifactMngDownload[] removeArtifactRevisions(String artifactIdent) {
    return this.downloads.remove(artifactIdent);
  }

  public String[] getArtifactIdents() {
    String[] artifactIdents = new String[downloads.size()];
    this.downloads.keySet().toArray(artifactIdents);
    return artifactIdents;
  }
}
