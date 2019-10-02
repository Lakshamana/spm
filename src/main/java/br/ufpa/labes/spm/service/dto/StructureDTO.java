package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class StructureDTO implements Serializable {
  private Long id;

  @IgnoreMapping private RepositoryDTO theRepository;

  @IgnoreMapping private NodeDTO rootElement;

  public StructureDTO() {
    this.theRepository = null;
    this.rootElement = null;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RepositoryDTO getTheRepository() {
    return theRepository;
  }

  public void setTheRepository(RepositoryDTO theRepository) {
    this.theRepository = theRepository;
  }

  public NodeDTO getRootElement() {
    return rootElement;
  }

  public void setRootElement(NodeDTO rootElement) {
    this.rootElement = rootElement;
  }

  @Override
  public String toString() {
    return "StructureDTO [id=" + id + ", rootElement=" + rootElement + "]";
  }
}
