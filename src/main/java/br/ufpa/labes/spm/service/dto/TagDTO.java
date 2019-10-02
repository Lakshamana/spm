package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Tag} entity. */
public class TagDTO implements Serializable {

  private Long id;

  private String ident;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TagDTO tagDTO = (TagDTO) o;
    if (tagDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), tagDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "TagDTO{" + "id=" + getId() + ", ident='" + getIdent() + "'" + "}";
  }
}
