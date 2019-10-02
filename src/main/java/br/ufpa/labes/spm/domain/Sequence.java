package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A Sequence. */
@Entity
@Table(name = "sequence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sequence extends SimpleCon implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JsonIgnoreProperties("theMultipleSequences")
  private Dependency theDependencyToMultipleSequences;

  @OneToOne(mappedBy = "theSequence")
  @JsonIgnore
  private Dependency theDependency;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Dependency getTheDependencyToMultipleSequences() {
    return theDependencyToMultipleSequences;
  }

  public Sequence theDependencyToMultipleSequences(Dependency dependency) {
    this.theDependencyToMultipleSequences = dependency;
    return this;
  }

  public void setTheDependencyToMultipleSequences(Dependency dependency) {
    this.theDependencyToMultipleSequences = dependency;
  }

  public Dependency getTheDependency() {
    return theDependency;
  }

  public Sequence theDependency(Dependency dependency) {
    this.theDependency = dependency;
    return this;
  }

  public void setTheDependency(Dependency dependency) {
    this.theDependency = dependency;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Sequence)) {
      return false;
    }
    return id != null && id.equals(((Sequence) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Sequence{" + "id=" + getId() + "}";
  }
}
