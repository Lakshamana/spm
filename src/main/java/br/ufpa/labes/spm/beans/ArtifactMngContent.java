package br.ufpa.labes.spm.beans;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

@SuppressWarnings("serial")
public class ArtifactMngContent implements Serializable {

  public String artifactName; // File name
  public byte[] artifactBytes; // File content
  public String artifactBinaries; // File extension

  public ArtifactMngContent() {
    this.artifactName = "";
    this.artifactBytes = null;
    this.artifactBinaries = "";
  }

  public ArtifactMngContent(String fileName, byte[] bytes) {
    String binaries = fileName.substring(fileName.lastIndexOf("."));
    this.artifactName = fileName.substring(0, fileName.lastIndexOf("."));
    this.artifactBytes = bytes;
    this.artifactBinaries = binaries;
  }

  public ArtifactMngContent(String artifactIdent, File artifactFile) {
    try {

      DataInputStream input = new DataInputStream(new FileInputStream(artifactFile.getPath()));
      byte[] bytes = new byte[(int) (artifactFile.length() + 1)];
      input.read(bytes);
      String binaries = artifactFile.getName().substring(artifactFile.getName().lastIndexOf("."));

      this.artifactName = artifactIdent + binaries;
      input.close();

      this.artifactBytes = bytes;
      this.artifactBinaries = binaries;
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public File buildArtifactFile(String parentPath) {
    try {
      File artifactFile = new File(parentPath, this.artifactName);
      DataOutputStream output = new DataOutputStream(new FileOutputStream(artifactFile));
      output.write(this.artifactBytes);
      output.flush();
      output.close();

      artifactFile.deleteOnExit();

      return artifactFile;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public File buildArtifactRevisionFile(String parentPath, String revision, String artifactIdent) {
    try {
      String name = artifactIdent + "_" + revision + this.artifactBinaries;
      File artifactFile = new File(parentPath, name);
      DataOutputStream output = new DataOutputStream(new FileOutputStream(artifactFile));
      output.write(this.artifactBytes);
      output.flush();
      output.close();

      return artifactFile;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /** @return the artifactName */
  public String getArtifactName() {
    return artifactName;
  }

  /** @param artifactName the artifactName to set */
  public void setArtifactName(String artifactName) {
    this.artifactName = artifactName;
  }

  /** @return the artifactBytes */
  public byte[] getArtifactBytes() {
    return artifactBytes;
  }

  /** @param artifactBytes the artifactBytes to set */
  public void setArtifactBytes(byte[] artifactBytes) {
    this.artifactBytes = artifactBytes;
  }

  /** @return the artifactBinaries */
  public String getArtifactBinaries() {
    return artifactBinaries;
  }

  /** @param artifactBinaries the artifactBinaries to set */
  public void setArtifactBinaries(String artifactBinaries) {
    this.artifactBinaries = artifactBinaries;
  }
}
