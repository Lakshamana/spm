package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/** A LessonLearned. */
@Entity
@Table(name = "lesson_learned")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LessonLearned implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Lob
  @Column(name = "description")
  private String description;

  @Column(name = "creation_date")
  private LocalDate creationDate;

  @ManyToOne
  @JsonIgnoreProperties("theLessonLearneds")
  private Author author;

  @ManyToOne
  @JsonIgnoreProperties("lessonsLearneds")
  private Asset theAsset;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public LessonLearned name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public LessonLearned description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getCreationDate() {
    return creationDate;
  }

  public LessonLearned creationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  public void setCreationDate(LocalDate creationDate) {
    this.creationDate = creationDate;
  }

  public Author getAuthor() {
    return author;
  }

  public LessonLearned author(Author author) {
    this.author = author;
    return this;
  }

  public void setAuthor(Author author) {
    this.author = author;
  }

  public Asset getTheAsset() {
    return theAsset;
  }

  public LessonLearned theAsset(Asset asset) {
    this.theAsset = asset;
    return this;
  }

  public void setTheAsset(Asset asset) {
    this.theAsset = asset;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LessonLearned)) {
      return false;
    }
    return id != null && id.equals(((LessonLearned) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "LessonLearned{"
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
        + "}";
  }
}
