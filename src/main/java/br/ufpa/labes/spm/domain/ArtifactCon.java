package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A ArtifactCon. */
@Entity
@Table(name = "artifact_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArtifactCon extends Connection implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JsonIgnoreProperties("theArtifactCons")
  private Artifact theArtifact;

  @ManyToOne
  @JsonIgnoreProperties("theArtifactCons")
  private ArtifactType theArtifactType;

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "artifact_con_to_multiple_con",
      joinColumns = @JoinColumn(name = "artifact_con_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "to_multiple_con_id", referencedColumnName = "id"))
  private Set<MultipleCon> toMultipleCons = new HashSet<>();

  @OneToOne(mappedBy = "theArtifactConSub")
  @JsonIgnore
  private Connection theConnectionSuper;

  @ManyToMany(mappedBy = "fromArtifactCons")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<Activity> toActivities = new HashSet<>();

  @ManyToMany(mappedBy = "toArtifactCons")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<Activity> fromActivities = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Artifact getTheArtifact() {
    return theArtifact;
  }

  public ArtifactCon theArtifact(Artifact artifact) {
    this.theArtifact = artifact;
    return this;
  }

  public void setTheArtifact(Artifact artifact) {
    this.theArtifact = artifact;
  }

  public ArtifactType getTheArtifactType() {
    return theArtifactType;
  }

  public ArtifactCon theArtifactType(ArtifactType artifactType) {
    this.theArtifactType = artifactType;
    return this;
  }

  public void setTheArtifactType(ArtifactType artifactType) {
    this.theArtifactType = artifactType;
  }

  public Set<MultipleCon> getToMultipleCons() {
    return toMultipleCons;
  }

  public ArtifactCon toMultipleCons(Set<MultipleCon> multipleCons) {
    this.toMultipleCons = multipleCons;
    return this;
  }

  public ArtifactCon addToMultipleCon(MultipleCon multipleCon) {
    this.toMultipleCons.add(multipleCon);
    multipleCon.getTheArtifactCons().add(this);
    return this;
  }

  public ArtifactCon removeToMultipleCon(MultipleCon multipleCon) {
    this.toMultipleCons.remove(multipleCon);
    multipleCon.getTheArtifactCons().remove(this);
    return this;
  }

  public void setToMultipleCons(Set<MultipleCon> multipleCons) {
    this.toMultipleCons = multipleCons;
  }

  public Connection getTheConnectionSuper() {
    return theConnectionSuper;
  }

  public ArtifactCon theConnectionSuper(Connection connection) {
    this.theConnectionSuper = connection;
    return this;
  }

  public void setTheConnectionSuper(Connection connection) {
    this.theConnectionSuper = connection;
  }

  public Set<Activity> getToActivities() {
    return toActivities;
  }

  public ArtifactCon toActivities(Set<Activity> activities) {
    this.toActivities = activities;
    return this;
  }

  public ArtifactCon addToActivity(Activity activity) {
    this.toActivities.add(activity);
    activity.getFromArtifactCons().add(this);
    return this;
  }

  public ArtifactCon removeToActivity(Activity activity) {
    this.toActivities.remove(activity);
    activity.getFromArtifactCons().remove(this);
    return this;
  }

  public void setToActivities(Set<Activity> activities) {
    this.toActivities = activities;
  }

  public Set<Activity> getFromActivities() {
    return fromActivities;
  }

  public ArtifactCon fromActivities(Set<Activity> activities) {
    this.fromActivities = activities;
    return this;
  }

  public ArtifactCon addFromActivity(Activity activity) {
    this.fromActivities.add(activity);
    activity.getToArtifactCons().add(this);
    return this;
  }

  public ArtifactCon removeFromActivity(Activity activity) {
    this.fromActivities.remove(activity);
    activity.getToArtifactCons().remove(this);
    return this;
  }

  public void setFromActivities(Set<Activity> activities) {
    this.fromActivities = activities;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArtifactCon)) {
      return false;
    }
    return id != null && id.equals(((ArtifactCon) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ArtifactCon{" + "id=" + getId() + "}";
  }
}
