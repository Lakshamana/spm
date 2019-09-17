package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A ActivityInstantiated. */
@Entity
@Table(name = "activity_instantiated")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityInstantiated implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JsonIgnoreProperties("theActivityInstantiateds")
  private InstantiationPolicyLog theInstantiationPolicyLog;

  @ManyToOne
  @JsonIgnoreProperties("theActivityInstantiateds")
  private Activity theActivity;

  @OneToMany(mappedBy = "theActivityInstantiated")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<InstantiationSuggestion> theInstSugs = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public InstantiationPolicyLog getTheInstantiationPolicyLog() {
    return theInstantiationPolicyLog;
  }

  public ActivityInstantiated theInstantiationPolicyLog(
      InstantiationPolicyLog instantiationPolicyLog) {
    this.theInstantiationPolicyLog = instantiationPolicyLog;
    return this;
  }

  public void setTheInstantiationPolicyLog(InstantiationPolicyLog instantiationPolicyLog) {
    this.theInstantiationPolicyLog = instantiationPolicyLog;
  }

  public Activity getTheActivity() {
    return theActivity;
  }

  public ActivityInstantiated theActivity(Activity activity) {
    this.theActivity = activity;
    return this;
  }

  public void setTheActivity(Activity activity) {
    this.theActivity = activity;
  }

  public Set<InstantiationSuggestion> getTheInstSugs() {
    return theInstSugs;
  }

  public ActivityInstantiated theInstSugs(Set<InstantiationSuggestion> instantiationSuggestions) {
    this.theInstSugs = instantiationSuggestions;
    return this;
  }

  public ActivityInstantiated addTheInstSug(InstantiationSuggestion instantiationSuggestion) {
    this.theInstSugs.add(instantiationSuggestion);
    instantiationSuggestion.setTheActivityInstantiated(this);
    return this;
  }

  public ActivityInstantiated removeTheInstSug(InstantiationSuggestion instantiationSuggestion) {
    this.theInstSugs.remove(instantiationSuggestion);
    instantiationSuggestion.setTheActivityInstantiated(null);
    return this;
  }

  public void setTheInstSugs(Set<InstantiationSuggestion> instantiationSuggestions) {
    this.theInstSugs = instantiationSuggestions;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ActivityInstantiated)) {
      return false;
    }
    return id != null && id.equals(((ActivityInstantiated) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ActivityInstantiated{" + "id=" + getId() + "}";
  }
}
