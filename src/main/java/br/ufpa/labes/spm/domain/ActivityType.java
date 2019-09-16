package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ActivityType.
 */
@Entity
@Table(name = "activity_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityType extends Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "theActivityTypeSub")
    @JsonIgnore
    private Type theTypeSuper;

    @OneToMany(mappedBy = "theActivityType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Activity> theActivities = new HashSet<>();

    @OneToMany(mappedBy = "theActivityType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Process> theProcesses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getTheTypeSuper() {
        return theTypeSuper;
    }

    public ActivityType theTypeSuper(Type type) {
        this.theTypeSuper = type;
        return this;
    }

    public void setTheTypeSuper(Type type) {
        this.theTypeSuper = type;
    }

    public Set<Activity> getTheActivities() {
        return theActivities;
    }

    public ActivityType theActivities(Set<Activity> activities) {
        this.theActivities = activities;
        return this;
    }

    public ActivityType addTheActivity(Activity activity) {
        this.theActivities.add(activity);
        activity.setTheActivityType(this);
        return this;
    }

    public ActivityType removeTheActivity(Activity activity) {
        this.theActivities.remove(activity);
        activity.setTheActivityType(null);
        return this;
    }

    public void setTheActivities(Set<Activity> activities) {
        this.theActivities = activities;
    }

    public Set<Process> getTheProcesses() {
        return theProcesses;
    }

    public ActivityType theProcesses(Set<Process> processes) {
        this.theProcesses = processes;
        return this;
    }

    public ActivityType addTheProcess(Process process) {
        this.theProcesses.add(process);
        process.setTheActivityType(this);
        return this;
    }

    public ActivityType removeTheProcess(Process process) {
        this.theProcesses.remove(process);
        process.setTheActivityType(null);
        return this;
    }

    public void setTheProcesses(Set<Process> processes) {
        this.theProcesses = processes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityType)) {
            return false;
        }
        return id != null && id.equals(((ActivityType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ActivityType{" +
            "id=" + getId() +
            "}";
    }
}
