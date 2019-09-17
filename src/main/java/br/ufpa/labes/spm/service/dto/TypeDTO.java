package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Type} entity. */
public class TypeDTO implements Serializable {

  private Long id;

  private String ident;

  @Lob private String description;

  private Boolean userDefined;

  private Long superTypeId;

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean isUserDefined() {
    return userDefined;
  }

  public void setUserDefined(Boolean userDefined) {
    this.userDefined = userDefined;
  }

  public Long getSuperTypeId() {
    return superTypeId;
  }

  public void setSuperTypeId(Long typeId) {
    this.superTypeId = typeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TypeDTO typeDTO = (TypeDTO) o;
    if (typeDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), typeDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "TypeDTO{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + ", userDefined='"
        + isUserDefined()
        + "'"
        + ", superType="
        + getSuperTypeId()
        + "}";
  }
}
