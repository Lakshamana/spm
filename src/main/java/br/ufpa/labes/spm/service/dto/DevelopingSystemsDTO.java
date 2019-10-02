package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DevelopingSystemsDTO implements Serializable {
  private String[] nameSystemas;
  private String[] nomeProjetos;
  private List<SystemDTO> systemsDTO;

  public DevelopingSystemsDTO() {
    this.setNameSystemas(new String[0]);
    this.setNomeProjetos(new String[0]);
    systemsDTO = new ArrayList<SystemDTO>();
  }

  public String[] getNameSystemas() {
    return nameSystemas;
  }

  public void setNameSystemas(String[] nameSystemas) {
    this.nameSystemas = nameSystemas;
  }

  public String[] getNomeProjetos() {
    return nomeProjetos;
  }

  public void setNomeProjetos(String[] nomeProjetos) {
    this.nomeProjetos = nomeProjetos;
  }

  public List<SystemDTO> getSystemsDTO() {
    return systemsDTO;
  }

  public void setSystemsDTO(List<SystemDTO> systemsDTO) {
    this.systemsDTO = systemsDTO;
  }
}
