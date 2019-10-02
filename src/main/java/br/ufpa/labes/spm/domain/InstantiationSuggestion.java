package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A InstantiationSuggestion. */
@Entity
@Table(name = "instantiation_suggestion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
public class InstantiationSuggestion implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JsonIgnoreProperties("theInstantiationSuggestions")
  private ActivityInstantiated theActivityInstantiated;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ActivityInstantiated getTheActivityInstantiated() {
    return theActivityInstantiated;
  }

  public InstantiationSuggestion theActivityInstantiated(
      ActivityInstantiated activityInstantiated) {
    this.theActivityInstantiated = activityInstantiated;
    return this;
  }

  public void setTheActivityInstantiated(ActivityInstantiated activityInstantiated) {
    this.theActivityInstantiated = activityInstantiated;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InstantiationSuggestion)) {
      return false;
    }
    return id != null && id.equals(((InstantiationSuggestion) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "InstantiationSuggestion{" + "id=" + getId() + "}";
  }
}
