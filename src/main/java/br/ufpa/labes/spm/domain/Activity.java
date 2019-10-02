package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A Activity. */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
public class Activity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ident")
  private String ident;

  @Column(name = "name")
  private String name;

  @Column(name = "is_version")
  private Boolean isVersion;

  @OneToMany(mappedBy = "theActivity")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ModelingActivityEvent> theModelingActivityEvents = new HashSet<>();

  @OneToMany(mappedBy = "isVersionOf")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Activity> hasVersions = new HashSet<>();

  @OneToMany(mappedBy = "toActivity")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<SimpleCon> fromSimpleCons = new HashSet<>();

  @OneToMany(mappedBy = "fromActivity")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<SimpleCon> toSimpleCons = new HashSet<>();

  @OneToMany(mappedBy = "toActivity")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<JoinCon> fromJoinCons = new HashSet<>();

  @OneToMany(mappedBy = "fromActivity")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<BranchCon> toBranchCons = new HashSet<>();

  @OneToMany(mappedBy = "activity")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ActivityEstimation> activityEstimations = new HashSet<>();

  @ManyToOne
  @JsonIgnoreProperties("theActivities")
  private ActivityType theActivityType;

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "activity_to_join_con",
      joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "to_join_con_id", referencedColumnName = "id"))
  private Set<JoinCon> toJoinCons = new HashSet<>();

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "activity_from_branchandcon",
      joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "from_branchandcon_id", referencedColumnName = "id"))
  private Set<BranchANDCon> fromBranchANDCons = new HashSet<>();

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "activity_from_artifact_con",
      joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "from_artifact_con_id", referencedColumnName = "id"))
  private Set<ArtifactCon> fromArtifactCons = new HashSet<>();

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "activity_to_artifact_con",
      joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "to_artifact_con_id", referencedColumnName = "id"))
  private Set<ArtifactCon> toArtifactCons = new HashSet<>();

  @ManyToOne
  @JsonIgnoreProperties("hasVersions")
  private Activity isVersionOf;

  @ManyToOne
  @JsonIgnoreProperties("theActivities")
  private ProcessModel theProcessModel;

  @OneToMany(mappedBy = "theActivity")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<BranchConCondToActivity> theBranchConCondToActivities = new HashSet<>();

  @OneToMany(mappedBy = "theActivity")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ActivityInstantiated> theActivityInstantiateds = new HashSet<>();

  @OneToMany(mappedBy = "activity")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ActivityMetric> activityMetrics = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdent() {
    return ident;
  }

  public Activity ident(String ident) {
    this.ident = ident;
    return this;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getName() {
    return name;
  }

  public Activity name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean isIsVersion() {
    return isVersion;
  }

  public Activity isVersion(Boolean isVersion) {
    this.isVersion = isVersion;
    return this;
  }

  public void setIsVersion(Boolean isVersion) {
    this.isVersion = isVersion;
  }

  public Set<ModelingActivityEvent> getTheModelingActivityEvents() {
    return theModelingActivityEvents;
  }

  public Activity theModelingActivityEvents(Set<ModelingActivityEvent> modelingActivityEvents) {
    this.theModelingActivityEvents = modelingActivityEvents;
    return this;
  }

  public Activity addTheModelingActivityEvent(ModelingActivityEvent modelingActivityEvent) {
    this.theModelingActivityEvents.add(modelingActivityEvent);
    modelingActivityEvent.setTheActivity(this);
    return this;
  }

  public Activity removeTheModelingActivityEvent(ModelingActivityEvent modelingActivityEvent) {
    this.theModelingActivityEvents.remove(modelingActivityEvent);
    modelingActivityEvent.setTheActivity(null);
    return this;
  }

  public void setTheModelingActivityEvents(Set<ModelingActivityEvent> modelingActivityEvents) {
    this.theModelingActivityEvents = modelingActivityEvents;
  }

  public Set<Activity> getHasVersions() {
    return hasVersions;
  }

  public Activity hasVersions(Set<Activity> activities) {
    this.hasVersions = activities;
    return this;
  }

  public Activity addHasVersions(Activity activity) {
    this.hasVersions.add(activity);
    activity.setIsVersionOf(this);
    return this;
  }

  public Activity removeHasVersions(Activity activity) {
    this.hasVersions.remove(activity);
    activity.setIsVersionOf(null);
    return this;
  }

  public void setHasVersions(Set<Activity> activities) {
    this.hasVersions = activities;
  }

  public Set<SimpleCon> getFromSimpleCons() {
    return fromSimpleCons;
  }

  public Activity fromSimpleCons(Set<SimpleCon> simpleCons) {
    this.fromSimpleCons = simpleCons;
    return this;
  }

  public Activity addFromSimpleCon(SimpleCon simpleCon) {
    this.fromSimpleCons.add(simpleCon);
    simpleCon.setToActivity(this);
    return this;
  }

  public Activity removeFromSimpleCon(SimpleCon simpleCon) {
    this.fromSimpleCons.remove(simpleCon);
    simpleCon.setToActivity(null);
    return this;
  }

  public void setFromSimpleCons(Set<SimpleCon> simpleCons) {
    this.fromSimpleCons = simpleCons;
  }

  public Set<SimpleCon> getToSimpleCons() {
    return toSimpleCons;
  }

  public Activity toSimpleCons(Set<SimpleCon> simpleCons) {
    this.toSimpleCons = simpleCons;
    return this;
  }

  public Activity addToSimpleCon(SimpleCon simpleCon) {
    this.toSimpleCons.add(simpleCon);
    simpleCon.setFromActivity(this);
    return this;
  }

  public Activity removeToSimpleCon(SimpleCon simpleCon) {
    this.toSimpleCons.remove(simpleCon);
    simpleCon.setFromActivity(null);
    return this;
  }

  public void setToSimpleCons(Set<SimpleCon> simpleCons) {
    this.toSimpleCons = simpleCons;
  }

  public Set<JoinCon> getFromJoinCons() {
    return fromJoinCons;
  }

  public Activity fromJoinCons(Set<JoinCon> joinCons) {
    this.fromJoinCons = joinCons;
    return this;
  }

  public Activity addFromJoinCon(JoinCon joinCon) {
    this.fromJoinCons.add(joinCon);
    joinCon.setToActivity(this);
    return this;
  }

  public Activity removeFromJoinCon(JoinCon joinCon) {
    this.fromJoinCons.remove(joinCon);
    joinCon.setToActivity(null);
    return this;
  }

  public void setFromJoinCons(Set<JoinCon> joinCons) {
    this.fromJoinCons = joinCons;
  }

  public Set<BranchCon> getToBranchCons() {
    return toBranchCons;
  }

  public Activity toBranchCons(Set<BranchCon> branchCons) {
    this.toBranchCons = branchCons;
    return this;
  }

  public Activity addToBranchCon(BranchCon branchCon) {
    this.toBranchCons.add(branchCon);
    branchCon.setFromActivity(this);
    return this;
  }

  public Activity removeToBranchCon(BranchCon branchCon) {
    this.toBranchCons.remove(branchCon);
    branchCon.setFromActivity(null);
    return this;
  }

  public void setToBranchCons(Set<BranchCon> branchCons) {
    this.toBranchCons = branchCons;
  }

  public Set<ActivityEstimation> getActivityEstimations() {
    return activityEstimations;
  }

  public Activity activityEstimations(Set<ActivityEstimation> activityEstimations) {
    this.activityEstimations = activityEstimations;
    return this;
  }

  public Activity addActivityEstimation(ActivityEstimation activityEstimation) {
    this.activityEstimations.add(activityEstimation);
    activityEstimation.setActivity(this);
    return this;
  }

  public Activity removeActivityEstimation(ActivityEstimation activityEstimation) {
    this.activityEstimations.remove(activityEstimation);
    activityEstimation.setActivity(null);
    return this;
  }

  public void setActivityEstimations(Set<ActivityEstimation> activityEstimations) {
    this.activityEstimations = activityEstimations;
  }

  public ActivityType getTheActivityType() {
    return theActivityType;
  }

  public Activity theActivityType(ActivityType activityType) {
    this.theActivityType = activityType;
    return this;
  }

  public void setTheActivityType(ActivityType activityType) {
    this.theActivityType = activityType;
  }

  public Set<JoinCon> getToJoinCons() {
    return toJoinCons;
  }

  public Activity toJoinCons(Set<JoinCon> joinCons) {
    this.toJoinCons = joinCons;
    return this;
  }

  public Activity addToJoinCon(JoinCon joinCon) {
    this.toJoinCons.add(joinCon);
    joinCon.getFromActivities().add(this);
    return this;
  }

  public Activity removeToJoinCon(JoinCon joinCon) {
    this.toJoinCons.remove(joinCon);
    joinCon.getFromActivities().remove(this);
    return this;
  }

  public void setToJoinCons(Set<JoinCon> joinCons) {
    this.toJoinCons = joinCons;
  }

  public Set<BranchANDCon> getFromBranchANDCons() {
    return fromBranchANDCons;
  }

  public Activity fromBranchANDCons(Set<BranchANDCon> branchANDCons) {
    this.fromBranchANDCons = branchANDCons;
    return this;
  }

  public Activity addFromBranchANDCon(BranchANDCon branchANDCon) {
    this.fromBranchANDCons.add(branchANDCon);
    branchANDCon.getToActivities().add(this);
    return this;
  }

  public Activity removeFromBranchANDCon(BranchANDCon branchANDCon) {
    this.fromBranchANDCons.remove(branchANDCon);
    branchANDCon.getToActivities().remove(this);
    return this;
  }

  public void setFromBranchANDCons(Set<BranchANDCon> branchANDCons) {
    this.fromBranchANDCons = branchANDCons;
  }

  public Set<ArtifactCon> getFromArtifactCons() {
    return fromArtifactCons;
  }

  public Activity fromArtifactCons(Set<ArtifactCon> artifactCons) {
    this.fromArtifactCons = artifactCons;
    return this;
  }

  public Activity addFromArtifactCon(ArtifactCon artifactCon) {
    this.fromArtifactCons.add(artifactCon);
    artifactCon.getToActivities().add(this);
    return this;
  }

  public Activity removeFromArtifactCon(ArtifactCon artifactCon) {
    this.fromArtifactCons.remove(artifactCon);
    artifactCon.getToActivities().remove(this);
    return this;
  }

  public void setFromArtifactCons(Set<ArtifactCon> artifactCons) {
    this.fromArtifactCons = artifactCons;
  }

  public Set<ArtifactCon> getToArtifactCons() {
    return toArtifactCons;
  }

  public Activity toArtifactCons(Set<ArtifactCon> artifactCons) {
    this.toArtifactCons = artifactCons;
    return this;
  }

  public Activity addToArtifactCon(ArtifactCon artifactCon) {
    this.toArtifactCons.add(artifactCon);
    artifactCon.getFromActivities().add(this);
    return this;
  }

  public Activity removeToArtifactCon(ArtifactCon artifactCon) {
    this.toArtifactCons.remove(artifactCon);
    artifactCon.getFromActivities().remove(this);
    return this;
  }

  public void setToArtifactCons(Set<ArtifactCon> artifactCons) {
    this.toArtifactCons = artifactCons;
  }

  public Activity getIsVersionOf() {
    return isVersionOf;
  }

  public Activity isVersionOf(Activity activity) {
    this.isVersionOf = activity;
    return this;
  }

  public void setIsVersionOf(Activity activity) {
    this.isVersionOf = activity;
  }

  public ProcessModel getTheProcessModel() {
    return theProcessModel;
  }

  public Activity theProcessModel(ProcessModel processModel) {
    this.theProcessModel = processModel;
    return this;
  }

  public void setTheProcessModel(ProcessModel processModel) {
    this.theProcessModel = processModel;
  }

  public Set<BranchConCondToActivity> getTheBranchConCondToActivities() {
    return theBranchConCondToActivities;
  }

  public Activity theBranchConCondToActivities(
      Set<BranchConCondToActivity> branchConCondToActivities) {
    this.theBranchConCondToActivities = branchConCondToActivities;
    return this;
  }

  public Activity addTheBranchConCondToActivity(BranchConCondToActivity branchConCondToActivity) {
    this.theBranchConCondToActivities.add(branchConCondToActivity);
    branchConCondToActivity.setTheActivity(this);
    return this;
  }

  public Activity removeTheBranchConCondToActivity(
      BranchConCondToActivity branchConCondToActivity) {
    this.theBranchConCondToActivities.remove(branchConCondToActivity);
    branchConCondToActivity.setTheActivity(null);
    return this;
  }

  public void setTheBranchConCondToActivities(
      Set<BranchConCondToActivity> branchConCondToActivities) {
    this.theBranchConCondToActivities = branchConCondToActivities;
  }

  public Set<ActivityInstantiated> getTheActivityInstantiateds() {
    return theActivityInstantiateds;
  }

  public Activity theActivityInstantiateds(Set<ActivityInstantiated> activityInstantiateds) {
    this.theActivityInstantiateds = activityInstantiateds;
    return this;
  }

  public Activity addTheActivityInstantiated(ActivityInstantiated activityInstantiated) {
    this.theActivityInstantiateds.add(activityInstantiated);
    activityInstantiated.setTheActivity(this);
    return this;
  }

  public Activity removeTheActivityInstantiated(ActivityInstantiated activityInstantiated) {
    this.theActivityInstantiateds.remove(activityInstantiated);
    activityInstantiated.setTheActivity(null);
    return this;
  }

  public void setTheActivityInstantiateds(Set<ActivityInstantiated> activityInstantiateds) {
    this.theActivityInstantiateds = activityInstantiateds;
  }

  public Set<ActivityMetric> getActivityMetrics() {
    return activityMetrics;
  }

  public Activity activityMetrics(Set<ActivityMetric> activityMetrics) {
    this.activityMetrics = activityMetrics;
    return this;
  }

  public Activity addActivityMetric(ActivityMetric activityMetric) {
    this.activityMetrics.add(activityMetric);
    activityMetric.setActivity(this);
    return this;
  }

  public Activity removeActivityMetric(ActivityMetric activityMetric) {
    this.activityMetrics.remove(activityMetric);
    activityMetric.setActivity(null);
    return this;
  }

  public void setActivityMetrics(Set<ActivityMetric> activityMetrics) {
    this.activityMetrics = activityMetrics;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Activity)) {
      return false;
    }
    return id != null && id.equals(((Activity) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Activity{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", name='"
        + getName()
        + "'"
        + ", isVersion='"
        + isIsVersion()
        + "'"
        + "}";
  }
}
