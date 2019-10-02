package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A MultipleCon. */
@Entity
@Table(name = "multiple_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
public class MultipleCon extends Connection implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fired")
  private Boolean fired;

  @OneToOne
  @JoinColumn(unique = true)
  private Dependency theDependency;

  @ManyToOne
  @JsonIgnoreProperties("theMultipleConsToDependencies")
  private Dependency theDependencyToMultipleCons;

  @OneToMany(mappedBy = "fromMultipleConnection")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<BranchCon> toBranchCons = new HashSet<>();

  @OneToMany(mappedBy = "theMultipleCon")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<BranchConCondToMultipleCon> theBranchConCondToMultipleCons = new HashSet<>();

  @OneToMany(mappedBy = "toMultipleCon")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<JoinCon> theJoinCons = new HashSet<>();

  @ManyToMany(mappedBy = "toMultipleCons")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<ArtifactCon> fromArtifactCons = new HashSet<>();

  @ManyToMany(mappedBy = "toMultipleCons")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<BranchANDCon> theBranchANDCons = new HashSet<>();

  @ManyToMany(mappedBy = "fromMultipleCons")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<JoinCon> theJoinConToMultipleCons = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean isFired() {
    return fired;
  }

  public MultipleCon fired(Boolean fired) {
    this.fired = fired;
    return this;
  }

  public void setFired(Boolean fired) {
    this.fired = fired;
  }

  public Dependency getTheDependency() {
    return theDependency;
  }

  public MultipleCon theDependency(Dependency dependency) {
    this.theDependency = dependency;
    return this;
  }

  public void setTheDependency(Dependency dependency) {
    this.theDependency = dependency;
  }

  public Dependency getTheDependencyToMultipleCons() {
    return theDependencyToMultipleCons;
  }

  public MultipleCon theDependencyToMultipleCons(Dependency dependency) {
    this.theDependencyToMultipleCons = dependency;
    return this;
  }

  public void setTheDependencyToMultipleCons(Dependency dependency) {
    this.theDependencyToMultipleCons = dependency;
  }

  public Set<BranchCon> getToBranchCons() {
    return toBranchCons;
  }

  public MultipleCon toBranchCons(Set<BranchCon> branchCons) {
    this.toBranchCons = branchCons;
    return this;
  }

  public MultipleCon addToBranchCon(BranchCon branchCon) {
    this.toBranchCons.add(branchCon);
    branchCon.setFromMultipleConnection(this);
    return this;
  }

  public MultipleCon removeToBranchCon(BranchCon branchCon) {
    this.toBranchCons.remove(branchCon);
    branchCon.setFromMultipleConnection(null);
    return this;
  }

  public void setToBranchCons(Set<BranchCon> branchCons) {
    this.toBranchCons = branchCons;
  }

  public Set<BranchConCondToMultipleCon> getTheBranchConCondToMultipleCons() {
    return theBranchConCondToMultipleCons;
  }

  public MultipleCon theBranchConCondToMultipleCons(
      Set<BranchConCondToMultipleCon> branchConCondToMultipleCons) {
    this.theBranchConCondToMultipleCons = branchConCondToMultipleCons;
    return this;
  }

  public MultipleCon addTheBranchConCondToMultipleCon(
      BranchConCondToMultipleCon branchConCondToMultipleCon) {
    this.theBranchConCondToMultipleCons.add(branchConCondToMultipleCon);
    branchConCondToMultipleCon.setTheMultipleCon(this);
    return this;
  }

  public MultipleCon removeTheBranchConCondToMultipleCon(
      BranchConCondToMultipleCon branchConCondToMultipleCon) {
    this.theBranchConCondToMultipleCons.remove(branchConCondToMultipleCon);
    branchConCondToMultipleCon.setTheMultipleCon(null);
    return this;
  }

  public void setTheBranchConCondToMultipleCons(
      Set<BranchConCondToMultipleCon> branchConCondToMultipleCons) {
    this.theBranchConCondToMultipleCons = branchConCondToMultipleCons;
  }

  public Set<JoinCon> getTheJoinCons() {
    return theJoinCons;
  }

  public MultipleCon theJoinCons(Set<JoinCon> joinCons) {
    this.theJoinCons = joinCons;
    return this;
  }

  public MultipleCon addTheJoinCon(JoinCon joinCon) {
    this.theJoinCons.add(joinCon);
    joinCon.setToMultipleCon(this);
    return this;
  }

  public MultipleCon removeTheJoinCon(JoinCon joinCon) {
    this.theJoinCons.remove(joinCon);
    joinCon.setToMultipleCon(null);
    return this;
  }

  public void setTheJoinCons(Set<JoinCon> joinCons) {
    this.theJoinCons = joinCons;
  }

  public Set<ArtifactCon> getFromArtifactCons() {
    return fromArtifactCons;
  }

  public MultipleCon fromArtifactCons(Set<ArtifactCon> artifactCons) {
    this.fromArtifactCons = artifactCons;
    return this;
  }

  public MultipleCon addFromArtifactCon(ArtifactCon artifactCon) {
    this.fromArtifactCons.add(artifactCon);
    artifactCon.getToMultipleCons().add(this);
    return this;
  }

  public MultipleCon removeFromArtifactCon(ArtifactCon artifactCon) {
    this.fromArtifactCons.remove(artifactCon);
    artifactCon.getToMultipleCons().remove(this);
    return this;
  }

  public void setFromArtifactCons(Set<ArtifactCon> artifactCons) {
    this.fromArtifactCons = artifactCons;
  }

  public Set<BranchANDCon> getTheBranchANDCons() {
    return theBranchANDCons;
  }

  public MultipleCon theBranchANDCons(Set<BranchANDCon> branchANDCons) {
    this.theBranchANDCons = branchANDCons;
    return this;
  }

  public MultipleCon addTheBranchANDCon(BranchANDCon branchANDCon) {
    this.theBranchANDCons.add(branchANDCon);
    branchANDCon.getToMultipleCons().add(this);
    return this;
  }

  public MultipleCon removeTheBranchANDCon(BranchANDCon branchANDCon) {
    this.theBranchANDCons.remove(branchANDCon);
    branchANDCon.getToMultipleCons().remove(this);
    return this;
  }

  public void setTheBranchANDCons(Set<BranchANDCon> branchANDCons) {
    this.theBranchANDCons = branchANDCons;
  }

  public Set<JoinCon> getTheJoinConToMultipleCons() {
    return theJoinConToMultipleCons;
  }

  public MultipleCon theJoinConToMultipleCons(Set<JoinCon> joinCons) {
    this.theJoinConToMultipleCons = joinCons;
    return this;
  }

  public MultipleCon addTheJoinConToMultipleCon(JoinCon joinCon) {
    this.theJoinConToMultipleCons.add(joinCon);
    joinCon.getFromMultipleCons().add(this);
    return this;
  }

  public MultipleCon removeTheJoinConToMultipleCon(JoinCon joinCon) {
    this.theJoinConToMultipleCons.remove(joinCon);
    joinCon.getFromMultipleCons().remove(this);
    return this;
  }

  public void setTheJoinConToMultipleCons(Set<JoinCon> joinCons) {
    this.theJoinConToMultipleCons = joinCons;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MultipleCon)) {
      return false;
    }
    return id != null && id.equals(((MultipleCon) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "MultipleCon{" + "id=" + getId() + ", fired='" + isFired() + "'" + "}";
  }
}
