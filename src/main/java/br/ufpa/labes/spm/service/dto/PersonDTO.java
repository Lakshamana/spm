package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Person} entity. */
public class PersonDTO implements Serializable {

  private Long id;

  private String gender;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PersonDTO personDTO = (PersonDTO) o;
    if (personDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), personDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "PersonDTO{" + "id=" + getId() + ", gender='" + getGender() + "'" + "}";
  }
}
