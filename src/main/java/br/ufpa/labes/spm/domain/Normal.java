package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Normal.
 */
@Entity
@Table(name = "normal")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Normal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "how_long")
    private Float howLong;

    @Column(name = "how_long_unit")
    private String howLongUnit;

    @Column(name = "planned_begin")
    private LocalDate plannedBegin;

    @Column(name = "planned_end")
    private LocalDate plannedEnd;

    @Lob
    @Column(name = "script")
    private String script;

    @Column(name = "delegable")
    private Boolean delegable;

    @Column(name = "auto_allocable")
    private Boolean autoAllocable;

    @OneToMany(mappedBy = "theNormal")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> theTasks = new HashSet<>();

    @OneToMany(mappedBy = "theNormal")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> theReservations = new HashSet<>();

    @OneToOne(mappedBy = "theNormal")
    @JsonIgnore
    private ResourceEvent theResourceEvent;

    @OneToMany(mappedBy = "inInvolvedArtifacts")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvolvedArtifact> theInvolvedArtifactToNormals = new HashSet<>();

    @OneToMany(mappedBy = "outInvolvedArtifacts")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvolvedArtifact> theInvolvedArtifactsFromNormals = new HashSet<>();

    @OneToMany(mappedBy = "theNormal")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RequiredPeople> theRequiredPeople = new HashSet<>();

    @OneToMany(mappedBy = "theNormal")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RequiredResource> theRequiredResources = new HashSet<>();

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

    public Normal ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Float getHowLong() {
        return howLong;
    }

    public Normal howLong(Float howLong) {
        this.howLong = howLong;
        return this;
    }

    public void setHowLong(Float howLong) {
        this.howLong = howLong;
    }

    public String getHowLongUnit() {
        return howLongUnit;
    }

    public Normal howLongUnit(String howLongUnit) {
        this.howLongUnit = howLongUnit;
        return this;
    }

    public void setHowLongUnit(String howLongUnit) {
        this.howLongUnit = howLongUnit;
    }

    public LocalDate getPlannedBegin() {
        return plannedBegin;
    }

    public Normal plannedBegin(LocalDate plannedBegin) {
        this.plannedBegin = plannedBegin;
        return this;
    }

    public void setPlannedBegin(LocalDate plannedBegin) {
        this.plannedBegin = plannedBegin;
    }

    public LocalDate getPlannedEnd() {
        return plannedEnd;
    }

    public Normal plannedEnd(LocalDate plannedEnd) {
        this.plannedEnd = plannedEnd;
        return this;
    }

    public void setPlannedEnd(LocalDate plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    public String getScript() {
        return script;
    }

    public Normal script(String script) {
        this.script = script;
        return this;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Boolean isDelegable() {
        return delegable;
    }

    public Normal delegable(Boolean delegable) {
        this.delegable = delegable;
        return this;
    }

    public void setDelegable(Boolean delegable) {
        this.delegable = delegable;
    }

    public Boolean isAutoAllocable() {
        return autoAllocable;
    }

    public Normal autoAllocable(Boolean autoAllocable) {
        this.autoAllocable = autoAllocable;
        return this;
    }

    public void setAutoAllocable(Boolean autoAllocable) {
        this.autoAllocable = autoAllocable;
    }

    public Set<Task> getTheTasks() {
        return theTasks;
    }

    public Normal theTasks(Set<Task> tasks) {
        this.theTasks = tasks;
        return this;
    }

    public Normal addTheTasks(Task task) {
        this.theTasks.add(task);
        task.setTheNormal(this);
        return this;
    }

    public Normal removeTheTasks(Task task) {
        this.theTasks.remove(task);
        task.setTheNormal(null);
        return this;
    }

    public void setTheTasks(Set<Task> tasks) {
        this.theTasks = tasks;
    }

    public Set<Reservation> getTheReservations() {
        return theReservations;
    }

    public Normal theReservations(Set<Reservation> reservations) {
        this.theReservations = reservations;
        return this;
    }

    public Normal addTheReservation(Reservation reservation) {
        this.theReservations.add(reservation);
        reservation.setTheNormal(this);
        return this;
    }

    public Normal removeTheReservation(Reservation reservation) {
        this.theReservations.remove(reservation);
        reservation.setTheNormal(null);
        return this;
    }

    public void setTheReservations(Set<Reservation> reservations) {
        this.theReservations = reservations;
    }

    public ResourceEvent getTheResourceEvent() {
        return theResourceEvent;
    }

    public Normal theResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvent = resourceEvent;
        return this;
    }

    public void setTheResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvent = resourceEvent;
    }

    public Set<InvolvedArtifact> getTheInvolvedArtifactToNormals() {
        return theInvolvedArtifactToNormals;
    }

    public Normal theInvolvedArtifactToNormals(Set<InvolvedArtifact> involvedArtifacts) {
        this.theInvolvedArtifactToNormals = involvedArtifacts;
        return this;
    }

    public Normal addTheInvolvedArtifactToNormal(InvolvedArtifact involvedArtifact) {
        this.theInvolvedArtifactToNormals.add(involvedArtifact);
        involvedArtifact.setInInvolvedArtifacts(this);
        return this;
    }

    public Normal removeTheInvolvedArtifactToNormal(InvolvedArtifact involvedArtifact) {
        this.theInvolvedArtifactToNormals.remove(involvedArtifact);
        involvedArtifact.setInInvolvedArtifacts(null);
        return this;
    }

    public void setTheInvolvedArtifactToNormals(Set<InvolvedArtifact> involvedArtifacts) {
        this.theInvolvedArtifactToNormals = involvedArtifacts;
    }

    public Set<InvolvedArtifact> getTheInvolvedArtifactsFromNormals() {
        return theInvolvedArtifactsFromNormals;
    }

    public Normal theInvolvedArtifactsFromNormals(Set<InvolvedArtifact> involvedArtifacts) {
        this.theInvolvedArtifactsFromNormals = involvedArtifacts;
        return this;
    }

    public Normal addTheInvolvedArtifactsFromNormal(InvolvedArtifact involvedArtifact) {
        this.theInvolvedArtifactsFromNormals.add(involvedArtifact);
        involvedArtifact.setOutInvolvedArtifacts(this);
        return this;
    }

    public Normal removeTheInvolvedArtifactsFromNormal(InvolvedArtifact involvedArtifact) {
        this.theInvolvedArtifactsFromNormals.remove(involvedArtifact);
        involvedArtifact.setOutInvolvedArtifacts(null);
        return this;
    }

    public void setTheInvolvedArtifactsFromNormals(Set<InvolvedArtifact> involvedArtifacts) {
        this.theInvolvedArtifactsFromNormals = involvedArtifacts;
    }

    public Set<RequiredPeople> getTheRequiredPeople() {
        return theRequiredPeople;
    }

    public Normal theRequiredPeople(Set<RequiredPeople> requiredPeople) {
        this.theRequiredPeople = requiredPeople;
        return this;
    }

    public Normal addTheRequiredPeople(RequiredPeople requiredPeople) {
        this.theRequiredPeople.add(requiredPeople);
        requiredPeople.setTheNormal(this);
        return this;
    }

    public Normal removeTheRequiredPeople(RequiredPeople requiredPeople) {
        this.theRequiredPeople.remove(requiredPeople);
        requiredPeople.setTheNormal(null);
        return this;
    }

    public void setTheRequiredPeople(Set<RequiredPeople> requiredPeople) {
        this.theRequiredPeople = requiredPeople;
    }

    public Set<RequiredResource> getTheRequiredResources() {
        return theRequiredResources;
    }

    public Normal theRequiredResources(Set<RequiredResource> requiredResources) {
        this.theRequiredResources = requiredResources;
        return this;
    }

    public Normal addTheRequiredResource(RequiredResource requiredResource) {
        this.theRequiredResources.add(requiredResource);
        requiredResource.setTheNormal(this);
        return this;
    }

    public Normal removeTheRequiredResource(RequiredResource requiredResource) {
        this.theRequiredResources.remove(requiredResource);
        requiredResource.setTheNormal(null);
        return this;
    }

    public void setTheRequiredResources(Set<RequiredResource> requiredResources) {
        this.theRequiredResources = requiredResources;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Normal)) {
            return false;
        }
        return id != null && id.equals(((Normal) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Normal{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", howLong=" + getHowLong() +
            ", howLongUnit='" + getHowLongUnit() + "'" +
            ", plannedBegin='" + getPlannedBegin() + "'" +
            ", plannedEnd='" + getPlannedEnd() + "'" +
            ", script='" + getScript() + "'" +
            ", delegable='" + isDelegable() + "'" +
            ", autoAllocable='" + isAutoAllocable() + "'" +
            "}";
    }
}
