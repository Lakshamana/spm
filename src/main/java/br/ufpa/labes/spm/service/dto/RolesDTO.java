package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

public class RolesDTO implements Serializable {
  private String[] namePapeis;
  private String[] typePapeis;
  private String[] superType;

  public RolesDTO() {
    this.namePapeis = new String[0];
    this.typePapeis = new String[0];
    this.superType = new String[0];
  }

  public String[] getNamePapeis() {
    return namePapeis;
  }

  public void setNamePapeis(String[] namePapeis) {
    this.namePapeis = namePapeis;
  }

  public String[] getTypePapeis() {
    return typePapeis;
  }

  public void setTypePapeis(String[] typePapeis) {
    this.typePapeis = typePapeis;
  }

  public String[] getSuperType() {
    return superType;
  }

  public void setSuperType(String[] superType) {
    this.superType = superType;
  }
}
