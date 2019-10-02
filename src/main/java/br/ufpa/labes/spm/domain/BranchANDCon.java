package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A BranchANDCon. */
@Entity
@Table(name = "branch_and_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BranchANDCon extends BranchCon implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "branch_and_con_to_multiple_con",
      joinColumns = @JoinColumn(name = "branchandcon_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "to_multiple_con_id", referencedColumnName = "id"))
  private Set<MultipleCon> toMultipleCons = new HashSet<>();

  @ManyToMany(mappedBy = "fromBranchANDCons")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<Activity> toActivities = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<MultipleCon> getToMultipleCons() {
    return toMultipleCons;
  }

  public BranchANDCon toMultipleCons(Set<MultipleCon> multipleCons) {
    this.toMultipleCons = multipleCons;
    return this;
  }

  public BranchANDCon addToMultipleCon(MultipleCon multipleCon) {
    this.toMultipleCons.add(multipleCon);
    multipleCon.getTheBranchANDCons().add(this);
    return this;
  }

  public BranchANDCon removeToMultipleCon(MultipleCon multipleCon) {
    this.toMultipleCons.remove(multipleCon);
    multipleCon.getTheBranchANDCons().remove(this);
    return this;
  }

  public void setToMultipleCons(Set<MultipleCon> multipleCons) {
    this.toMultipleCons = multipleCons;
  }

  public Set<Activity> getToActivities() {
    return toActivities;
  }

  public BranchANDCon toActivities(Set<Activity> activities) {
    this.toActivities = activities;
    return this;
  }

  public BranchANDCon addToActivity(Activity activity) {
    this.toActivities.add(activity);
    activity.getFromBranchANDCons().add(this);
    return this;
  }

  public BranchANDCon removeToActivity(Activity activity) {
    this.toActivities.remove(activity);
    activity.getFromBranchANDCons().remove(this);
    return this;
  }

  public void setToActivities(Set<Activity> activities) {
    this.toActivities = activities;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BranchANDCon)) {
      return false;
    }
    return id != null && id.equals(((BranchANDCon) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "BranchANDCon{" + "id=" + getId() + "}";
  }
}
