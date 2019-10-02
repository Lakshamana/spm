package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Set;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class NodeDTO implements Serializable {
  private Long id;

  private String ident;

  private String data;

  @IgnoreMapping private StructureDTO theStructureDTO;

  @IgnoreMapping private Set<NodeDTO> children;

  public NodeDTO() {
    super();
    this.children = new HashSet<NodeDTO>();
    this.setIdent("");
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdent() {
    return ident;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public Set<NodeDTO> getChildren() {
    return children;
  }

  public void setChildren(Set<NodeDTO> children) {
    this.children = children;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public StructureDTO getTheStructureDTO() {
    return theStructureDTO;
  }

  public void setTheStructureDTO(StructureDTO theStructureDTO) {
    this.theStructureDTO = theStructureDTO;
  }

  @Override
  public String toString() {
    return "NodeDTO [ident=" + ident + ", children=" + children + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ident == null) ? 0 : ident.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    NodeDTO other = (NodeDTO) obj;
    if (ident == null) {
      if (other.ident != null) return false;
    } else if (!ident.equals(other.ident)) return false;
    return true;
  }
}
