package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.InstantiationPolicyLog} entity. */
public class InstantiationPolicyLogDTO implements Serializable {

  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    InstantiationPolicyLogDTO instantiationPolicyLogDTO = (InstantiationPolicyLogDTO) o;
    if (instantiationPolicyLogDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), instantiationPolicyLogDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "InstantiationPolicyLogDTO{" + "id=" + getId() + "}";
  }
}
