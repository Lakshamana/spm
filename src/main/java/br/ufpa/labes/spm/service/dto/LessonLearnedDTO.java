package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.LessonLearned} entity. */
public class LessonLearnedDTO implements Serializable {

  private Long id;

  private String name;

  @Lob private String description;

  private LocalDate creationDate;

  private Long authorId;

  private Long assetId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public Long getAssetId() {
    return assetId;
  }

  public void setAssetId(Long assetId) {
    this.assetId = assetId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LessonLearnedDTO lessonLearnedDTO = (LessonLearnedDTO) o;
    if (lessonLearnedDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), lessonLearnedDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "LessonLearnedDTO{"
        + "id="
        + getId()
        + ", name='"
        + getName()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + ", creationDate='"
        + getCreationDate()
        + "'"
        + ", author="
        + getAuthorId()
        + ", asset="
        + getAssetId()
        + "}";
  }
}
