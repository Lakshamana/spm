package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ResourceInstSug} entity. */
public class ResourceInstSugDTO implements Serializable {

  private Long id;

  private Long resourceChosenId;

  private Long resourceTypeRequiredId;

  private Set<ResourceDTO> resourceSuggesteds = new HashSet<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getResourceChosenId() {
    return resourceChosenId;
  }

  public void setResourceChosenId(Long resourceId) {
    this.resourceChosenId = resourceId;
  }

  public Long getResourceTypeRequiredId() {
    return resourceTypeRequiredId;
  }

  public void setResourceTypeRequiredId(Long resourceTypeId) {
    this.resourceTypeRequiredId = resourceTypeId;
  }

  public Set<ResourceDTO> getResourceSuggesteds() {
    return resourceSuggesteds;
  }

  public void setResourceSuggesteds(Set<ResourceDTO> resources) {
    this.resourceSuggesteds = resources;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ResourceInstSugDTO resourceInstSugDTO = (ResourceInstSugDTO) o;
    if (resourceInstSugDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), resourceInstSugDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ResourceInstSugDTO{"
        + "id="
        + getId()
        + ", resourceChosen="
        + getResourceChosenId()
        + ", resourceTypeRequired="
        + getResourceTypeRequiredId()
        + "}";
  }
}
