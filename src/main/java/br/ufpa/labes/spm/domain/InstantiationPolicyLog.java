package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A InstantiationPolicyLog. */
@Entity
@Table(name = "instantiation_policy_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InstantiationPolicyLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "theInstantiationPolicyLog")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ActivityInstantiated> theActivityInstantiateds = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<ActivityInstantiated> getTheActivityInstantiateds() {
    return theActivityInstantiateds;
  }

  public InstantiationPolicyLog theActivityInstantiateds(
      Set<ActivityInstantiated> activityInstantiateds) {
    this.theActivityInstantiateds = activityInstantiateds;
    return this;
  }

  public InstantiationPolicyLog addTheActivityInstantiated(
      ActivityInstantiated activityInstantiated) {
    this.theActivityInstantiateds.add(activityInstantiated);
    activityInstantiated.setTheInstantiationPolicyLog(this);
    return this;
  }

  public InstantiationPolicyLog removeTheActivityInstantiated(
      ActivityInstantiated activityInstantiated) {
    this.theActivityInstantiateds.remove(activityInstantiated);
    activityInstantiated.setTheInstantiationPolicyLog(null);
    return this;
  }

  public void setTheActivityInstantiateds(Set<ActivityInstantiated> activityInstantiateds) {
    this.theActivityInstantiateds = activityInstantiateds;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InstantiationPolicyLog)) {
      return false;
    }
    return id != null && id.equals(((InstantiationPolicyLog) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "InstantiationPolicyLog{" + "id=" + getId() + "}";
  }
}
