package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Role} entity. */
public class RoleDTO implements Serializable {

  private Long id;

  private String ident;

  private String name;

  @Lob private String description;

  private Long subordinateId;

  private Long theRoleTypeId;

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

  public Long getSubordinateId() {
    return subordinateId;
  }

  public void setSubordinateId(Long roleId) {
    this.subordinateId = roleId;
  }

  public Long getTheRoleTypeId() {
    return theRoleTypeId;
  }

  public void setTheRoleTypeId(Long roleTypeId) {
    this.theRoleTypeId = roleTypeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RoleDTO roleDTO = (RoleDTO) o;
    if (roleDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), roleDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "RoleDTO{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", name='"
        + getName()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + ", subordinate="
        + getSubordinateId()
        + ", theRoleType="
        + getTheRoleTypeId()
        + "}";
  }
}
