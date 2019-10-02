package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import br.ufpa.labes.spm.annotations.IgnoreMapping;

@SuppressWarnings("serial")
public class ToolDTO implements Serializable {
  private Long id;

  private String ident;

  private String name;

  private String description;

  @IgnoreMapping private String theToolType;

  @IgnoreMapping private Collection<String> theArtifactType;

  public ToolDTO() {
    this.theArtifactType = new ArrayList<String>();
  }

  public ToolDTO(String name, String theToolType, Boolean active, String description) {
    this.name = name;
    this.ident = name;
    this.theToolType = theToolType;
    this.description = description;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTheToolType() {
    return theToolType;
  }

  public void setTheToolType(String theToolType) {
    this.theToolType = theToolType;
  }

  public Collection<String> getTheArtifactType() {
    return theArtifactType;
  }

  public void setTheArtifactType(Collection<String> theArtifactType) {
    this.theArtifactType = theArtifactType;
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
    ToolDTO other = (ToolDTO) obj;
    if (ident == null) {
      if (other.ident != null) return false;
    } else if (!ident.equals(other.ident)) return false;
    return true;
  }
}
