package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RepositoriesDTO implements Serializable {
  private String[] idents;
  private String[] controlVersions;
  private String[] Servidores;

  public RepositoriesDTO() {
    this.setIdents(new String[0]);
    this.setServidores(new String[0]);
    this.setControlVersions(new String[0]);
  }

  public String[] getIdents() {
    return idents;
  }

  public void setIdents(String[] idents) {
    this.idents = idents;
  }

  public String[] getControlVersions() {
    return controlVersions;
  }

  public void setControlVersions(String[] controlVersions) {
    this.controlVersions = controlVersions;
  }

  public String[] getServidores() {
    return Servidores;
  }

  public void setServidores(String[] servidores) {
    Servidores = servidores;
  }
}
