package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.CatalogEvent} entity. */
public class CatalogEventDTO implements Serializable {

  private Long id;

  @Lob private String description;

  private Long thePlainId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getThePlainId() {
    return thePlainId;
  }

  public void setThePlainId(Long plainId) {
    this.thePlainId = plainId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CatalogEventDTO catalogEventDTO = (CatalogEventDTO) o;
    if (catalogEventDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), catalogEventDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "CatalogEventDTO{"
        + "id="
        + getId()
        + ", description='"
        + getDescription()
        + "'"
        + ", thePlain="
        + getThePlainId()
        + "}";
  }
}
