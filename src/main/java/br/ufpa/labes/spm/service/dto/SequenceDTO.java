package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Sequence} entity. */
public class SequenceDTO implements Serializable {

  private Long id;

  private Long theDependencyToMultipleSequencesId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTheDependencyToMultipleSequencesId() {
    return theDependencyToMultipleSequencesId;
  }

  public void setTheDependencyToMultipleSequencesId(Long dependencyId) {
    this.theDependencyToMultipleSequencesId = dependencyId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SequenceDTO sequenceDTO = (SequenceDTO) o;
    if (sequenceDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), sequenceDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "SequenceDTO{"
        + "id="
        + getId()
        + ", theDependencyToMultipleSequences="
        + getTheDependencyToMultipleSequencesId()
        + "}";
  }
}
