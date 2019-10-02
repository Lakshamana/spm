package br.ufpa.labes.spm.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JOptionPane;

import br.ufpa.labes.spm.util.i18n.Messages;

@SuppressWarnings("serial")
public class ArtifactMngDownload implements Serializable {

  private ArtifactMngContent content;
  private String revision;

  public ArtifactMngDownload() {
    this.content = new ArtifactMngContent();
    this.revision = "";
  }

  /** @param file - directory to save the artifact content */
  public String buildArtifactContent(File dir) {
    try {
      String fileName = dir.getPath() + File.separator + content.getArtifactName();
      FileOutputStream out = new FileOutputStream(fileName);
      out.write(content.getArtifactBytes());
      out.close();
      return fileName;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
          null,
          Messages.getString("managerSDK.Gui.Forms.Artifacts.JOpPaneSMDMsgFileNotFound"),
          Messages.getString("managerSDK.Gui.Forms.Artifacts.JOpPaneSMDTitleCVSWSError"),
          JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          Messages.getString("managerSDK.Gui.Forms.Artifacts.JOpPaneSMDMsgErrorWriting"),
          Messages.getString("managerSDK.Gui.Forms.Artifacts.JOpPaneSMDTitleCVSWSError"),
          JOptionPane.ERROR_MESSAGE);
    }
    return null;
  }

  public String buildArtifactContent(File dir, String binaries) {
    try {
      this.changeBinariesContent(binaries);

      String fileName = dir.getPath() + File.separator + content.getArtifactName();
      FileOutputStream out = new FileOutputStream(fileName);
      out.write(content.getArtifactBytes());
      out.close();

      return fileName;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
          null,
          Messages.getString("managerSDK.Gui.Forms.Artifacts.JOpPaneSMDMsgFileNotFound"),
          Messages.getString("managerSDK.Gui.Forms.Artifacts.JOpPaneSMDTitleCVSWSError"),
          JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(
          null,
          Messages.getString("managerSDK.Gui.Forms.Artifacts.JOpPaneSMDMsgErrorWriting"),
          Messages.getString("managerSDK.Gui.Forms.Artifacts.JOpPaneSMDTitleCVSWSError"),
          JOptionPane.ERROR_MESSAGE);
    }
    return null;
  }

  private void changeBinariesContent(String binaries) {
    content.artifactName = content.artifactName.substring(0, content.artifactName.lastIndexOf("."));
    content.artifactBinaries = binaries;
    content.artifactName = content.artifactName + binaries;
  }

  public String getRevision() {
    return revision;
  }

  public void setRevision(String revision) {
    this.revision = revision;
  }

  /** @return the artifact content */
  public ArtifactMngContent getContent() {
    return content;
  }

  /** @param content - artifact content */
  public void setContent(ArtifactMngContent content) {
    this.content = content;
  }
}
