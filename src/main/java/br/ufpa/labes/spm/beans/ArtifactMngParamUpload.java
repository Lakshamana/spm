package br.ufpa.labes.spm.beans;

import java.io.Serializable;

import br.ufpa.labes.spm.util.ResourcesUtility;
import br.ufpa.labes.spm.util.i18n.Messages;

public class ArtifactMngParamUpload implements Serializable {

  public String username; // login on WebAPSEE Artifact Management

  public String processIdent; // Process identifier
  public String activityIdent; // Activity identifier

  public String artifactIdent; // Artifact identifier that will be upload
  public ArtifactMngContent artifactContent; // Artifact Content

  public String comment; // Comment of the upload

  public ArtifactMngParamUpload() {
    this.username = "admin";
    this.processIdent = Messages.getString("managerSDK.Gui.Forms.Artifacts.ArtifactTemplates");
    this.activityIdent = Messages.getString("managerSDK.Gui.Forms.Artifacts.DefaultActivity");
    this.artifactIdent = "";
    this.artifactContent = new ArtifactMngContent();
    this.comment = "By " + ResourcesUtility.getProductID() + " Artifact Manager: ";
  }

  public ArtifactMngParamUpload(
      String username,
      String processIdent,
      String activityIdent,
      String artifactIdent,
      String comment,
      ArtifactMngContent artifactContent) {
    this.username = username;
    this.setProcessIdent(processIdent);
    this.activityIdent = activityIdent;
    this.artifactIdent = artifactIdent;
    this.comment = "By " + ResourcesUtility.getProductID() + " Artifact Manager: ";
    this.setComment(comment);
    this.artifactContent = artifactContent;
  }

  /** @return the activity id */
  public String getActivityIdent() {
    return activityIdent;
  }

  /** @param activityIdent - activity that generates the upload */
  public void setActivityIdent(String activityIdent) {
    this.activityIdent = activityIdent;
  }

  /** @return the artifact content */
  public ArtifactMngContent getArtifactContent() {
    return artifactContent;
  }

  /** @param artifactContent - artifact content */
  public void setArtifactContent(ArtifactMngContent artifactContent) {
    this.artifactContent = artifactContent;
  }

  /** @return the artifact id */
  public String getArtifactIdent() {
    return artifactIdent;
  }

  /** @param artifactIdent - artifact id */
  public void setArtifactIdent(String artifactIdent) {
    this.artifactIdent = artifactIdent;
  }

  /** @return the comment */
  public String getComment() {
    return comment;
  }

  /** @param comment - comment of the upload */
  public void setComment(String comment) {
    if (comment != null) {
      this.comment = this.comment + comment;
    }
  }

  /** @return the process id */
  public String getProcessIdent() {
    return processIdent;
  }

  /** @param processIdent - process id */
  public void setProcessIdent(String processIdent) {
    this.processIdent = ResourcesUtility.getProductID() + "/" + processIdent;
  }

  /** @return the user name */
  public String getUsername() {
    return username;
  }

  /** @param username to login on WebAPSEE Artifact Management */
  public void setUsername(String username) {
    this.username = username;
  }
}
