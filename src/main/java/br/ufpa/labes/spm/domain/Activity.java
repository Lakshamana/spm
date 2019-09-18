package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Activity.
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "theActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> theModelingActivityEvents = new HashSet<>();

    @OneToMany(mappedBy = "theAncestorActitvity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Activity> hasVersions = new HashSet<>();

    @OneToMany(mappedBy = "fromActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SimpleCon> fromSimpleCons = new HashSet<>();

    @OneToMany(mappedBy = "toActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SimpleCon> toSimpleCons = new HashSet<>();

    @OneToMany(mappedBy = "toActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JoinCon> fromJoinCons = new HashSet<>();

    @OneToMany(mappedBy = "fromActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BranchCon> toBranchCons = new HashSet<>();

    @OneToMany(mappedBy = "theActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Metric> activityMetrics = new HashSet<>();

    @OneToMany(mappedBy = "theActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ActivityEstimation> theActivityEstimations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("theActivities")
    private ActivityType theActivityType;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "activity_to_join_con",
               joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "to_join_con_id", referencedColumnName = "id"))
    private Set<JoinCon> toJoinCons = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "activity_to_branchandcon",
               joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "to_branchandcon_id", referencedColumnName = "id"))
    private Set<BranchANDCon> toBranchANDCons = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "activity_from_artifact_con",
               joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "from_artifact_con_id", referencedColumnName = "id"))
    private Set<ArtifactCon> fromArtifactCons = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "activity_to_artifact_con",
               joinColumns = @JoinColumn(name = "activity_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "to_artifact_con_id", referencedColumnName = "id"))
    private Set<ArtifactCon> toArtifactCons = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("hasVersions")
    private Activity theAncestorActitvity;

    @ManyToOne
    @JsonIgnoreProperties("theActivities")
    private ProcessModel theProcessModel;

    @OneToMany(mappedBy = "theActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BranchConCondToActivity> theBranchConCondToActivities = new HashSet<>();

    @OneToMany(mappedBy = "theActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ActivityInstantiated> theActivityInstantiateds = new HashSet<>();

    @OneToMany(mappedBy = "theActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ActivityMetric> theActivityMetrics = new HashSet<>();

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

    public Set<Event> getTheModelingActivityEvents() {
        return theModelingActivityEvents;
    }

    public Activity theModelingActivityEvents(Set<Event> events) {
        this.theModelingActivityEvents = events;
        return this;
    }

    public Activity addTheModelingActivityEvent(Event event) {
        this.theModelingActivityEvents.add(event);
        event.setTheActivity(this);
        return this;
    }

    public Activity removeTheModelingActivityEvent(Event event) {
        this.theModelingActivityEvents.remove(event);
        event.setTheActivity(null);
        return this;
    }

    public void setTheModelingActivityEvents(Set<Event> events) {
        this.theModelingActivityEvents = events;
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
        activity.setTheAncestorActitvity(this);
        return this;
    }

    public Activity removeHasVersions(Activity activity) {
        this.hasVersions.remove(activity);
        activity.setTheAncestorActitvity(null);
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
        simpleCon.setFromActivity(this);
        return this;
    }

    public Activity removeFromSimpleCon(SimpleCon simpleCon) {
        this.fromSimpleCons.remove(simpleCon);
        simpleCon.setFromActivity(null);
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
        simpleCon.setToActivity(this);
        return this;
    }

    public Activity removeToSimpleCon(SimpleCon simpleCon) {
        this.toSimpleCons.remove(simpleCon);
        simpleCon.setToActivity(null);
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

    public Set<Metric> getActivityMetrics() {
        return activityMetrics;
    }

    public Activity activityMetrics(Set<Metric> metrics) {
        this.activityMetrics = metrics;
        return this;
    }

    public Activity addActivityMetric(Metric metric) {
        this.activityMetrics.add(metric);
        metric.setTheActivity(this);
        return this;
    }

    public Activity removeActivityMetric(Metric metric) {
        this.activityMetrics.remove(metric);
        metric.setTheActivity(null);
        return this;
    }

    public void setActivityMetrics(Set<Metric> metrics) {
        this.activityMetrics = metrics;
    }

    public Set<ActivityEstimation> getTheActivityEstimations() {
        return theActivityEstimations;
    }

    public Activity theActivityEstimations(Set<ActivityEstimation> activityEstimations) {
        this.theActivityEstimations = activityEstimations;
        return this;
    }

    public Activity addTheActivityEstimation(ActivityEstimation activityEstimation) {
        this.theActivityEstimations.add(activityEstimation);
        activityEstimation.setTheActivity(this);
        return this;
    }

    public Activity removeTheActivityEstimation(ActivityEstimation activityEstimation) {
        this.theActivityEstimations.remove(activityEstimation);
        activityEstimation.setTheActivity(null);
        return this;
    }

    public void setTheActivityEstimations(Set<ActivityEstimation> activityEstimations) {
        this.theActivityEstimations = activityEstimations;
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

    public Set<BranchANDCon> getToBranchANDCons() {
        return toBranchANDCons;
    }

    public Activity toBranchANDCons(Set<BranchANDCon> branchANDCons) {
        this.toBranchANDCons = branchANDCons;
        return this;
    }

    public Activity addToBranchANDCon(BranchANDCon branchANDCon) {
        this.toBranchANDCons.add(branchANDCon);
        branchANDCon.getFromActivities().add(this);
        return this;
    }

    public Activity removeToBranchANDCon(BranchANDCon branchANDCon) {
        this.toBranchANDCons.remove(branchANDCon);
        branchANDCon.getFromActivities().remove(this);
        return this;
    }

    public void setToBranchANDCons(Set<BranchANDCon> branchANDCons) {
        this.toBranchANDCons = branchANDCons;
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

    public Activity getTheAncestorActitvity() {
        return theAncestorActitvity;
    }

    public Activity theAncestorActitvity(Activity activity) {
        this.theAncestorActitvity = activity;
        return this;
    }

    public void setTheAncestorActitvity(Activity activity) {
        this.theAncestorActitvity = activity;
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

    public Activity theBranchConCondToActivities(Set<BranchConCondToActivity> branchConCondToActivities) {
        this.theBranchConCondToActivities = branchConCondToActivities;
        return this;
    }

    public Activity addTheBranchConCondToActivity(BranchConCondToActivity branchConCondToActivity) {
        this.theBranchConCondToActivities.add(branchConCondToActivity);
        branchConCondToActivity.setTheActivity(this);
        return this;
    }

    public Activity removeTheBranchConCondToActivity(BranchConCondToActivity branchConCondToActivity) {
        this.theBranchConCondToActivities.remove(branchConCondToActivity);
        branchConCondToActivity.setTheActivity(null);
        return this;
    }

    public void setTheBranchConCondToActivities(Set<BranchConCondToActivity> branchConCondToActivities) {
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

    public Set<ActivityMetric> getTheActivityMetrics() {
        return theActivityMetrics;
    }

    public Activity theActivityMetrics(Set<ActivityMetric> activityMetrics) {
        this.theActivityMetrics = activityMetrics;
        return this;
    }

    public Activity addTheActivityMetric(ActivityMetric activityMetric) {
        this.theActivityMetrics.add(activityMetric);
        activityMetric.setTheActivity(this);
        return this;
    }

    public Activity removeTheActivityMetric(ActivityMetric activityMetric) {
        this.theActivityMetrics.remove(activityMetric);
        activityMetric.setTheActivity(null);
        return this;
    }

    public void setTheActivityMetrics(Set<ActivityMetric> activityMetrics) {
        this.theActivityMetrics = activityMetrics;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return "Activity{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
