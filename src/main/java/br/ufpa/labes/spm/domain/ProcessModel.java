package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ProcessModel.
 */
@Entity
@Table(name = "process_model")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcessModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final transient String
    REQUIREMENTS	= "Requirements",
    ABSTRACT        = "Abstract",
    INSTANTIATED	= "Instantiated",
    ENACTING		= "Enacting",
    FAILED          = "Failed",
    CANCELED        = "Canceled",
    MIXED           = "Mixed",
    FINISHED        = "Finished";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requirements")
    private String requirements;

    @Column(name = "pm_state")
    private String pmState;

    @OneToMany(mappedBy = "theProcessModel")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Activity> theActivities = new HashSet<>();

    @OneToOne(mappedBy = "theReferedProcessModel")
    @JsonIgnore
    private Decomposed theDecomposed;

    @OneToOne(mappedBy = "theProcessModel")
    @JsonIgnore
    private Process theProcess;

    @ManyToOne
    @JsonIgnoreProperties("theInstances")
    private Template theOrigin;

    @OneToMany(mappedBy = "theProcessModel")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Connection> theConnections = new HashSet<>();

    @OneToMany(mappedBy = "theProcessModel")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProcessModelEvent> theProcessModelEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequirements() {
        return requirements;
    }

    public ProcessModel requirements(String requirements) {
        this.requirements = requirements;
        return this;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getPmState() {
        return pmState;
    }

    public ProcessModel pmState(String pmState) {
        this.pmState = pmState;
        return this;
    }

    public void setPmState(String pmState) {
        this.pmState = pmState;
    }

    public Set<Activity> getTheActivities() {
        return theActivities;
    }

    public ProcessModel theActivities(Set<Activity> activities) {
        this.theActivities = activities;
        return this;
    }

    public ProcessModel addTheActivity(Activity activity) {
        this.theActivities.add(activity);
        activity.setTheProcessModel(this);
        return this;
    }

    public ProcessModel removeTheActivity(Activity activity) {
        this.theActivities.remove(activity);
        activity.setTheProcessModel(null);
        return this;
    }

    public void setTheActivities(Set<Activity> activities) {
        this.theActivities = activities;
    }

    public Decomposed getTheDecomposed() {
        return theDecomposed;
    }

    public ProcessModel theDecomposed(Decomposed decomposed) {
        this.theDecomposed = decomposed;
        return this;
    }

    public void setTheDecomposed(Decomposed decomposed) {
        this.theDecomposed = decomposed;
    }

    public Process getTheProcess() {
        return theProcess;
    }

    public ProcessModel theProcess(Process process) {
        this.theProcess = process;
        return this;
    }

    public void setTheProcess(Process process) {
        this.theProcess = process;
    }

    public Template getTheOrigin() {
        return theOrigin;
    }

    public ProcessModel theOrigin(Template template) {
        this.theOrigin = template;
        return this;
    }

    public void setTheOrigin(Template template) {
        this.theOrigin = template;
    }

    public Set<Connection> getTheConnections() {
        return theConnections;
    }

    public ProcessModel theConnections(Set<Connection> connections) {
        this.theConnections = connections;
        return this;
    }

    public ProcessModel addTheConnection(Connection connection) {
        this.theConnections.add(connection);
        connection.setTheProcessModel(this);
        return this;
    }

    public ProcessModel removeTheConnection(Connection connection) {
        this.theConnections.remove(connection);
        connection.setTheProcessModel(null);
        return this;
    }

    public void setTheConnections(Set<Connection> connections) {
        this.theConnections = connections;
    }

    public Set<ProcessModelEvent> getTheProcessModelEvents() {
        return theProcessModelEvents;
    }

    public ProcessModel theProcessModelEvents(Set<ProcessModelEvent> processModelEvents) {
        this.theProcessModelEvents = processModelEvents;
        return this;
    }

    public ProcessModel addTheProcessModelEvent(ProcessModelEvent processModelEvent) {
        this.theProcessModelEvents.add(processModelEvent);
        processModelEvent.setTheProcessModel(this);
        return this;
    }

    public ProcessModel removeTheProcessModelEvent(ProcessModelEvent processModelEvent) {
        this.theProcessModelEvents.remove(processModelEvent);
        processModelEvent.setTheProcessModel(null);
        return this;
    }

    public void setTheProcessModelEvents(Set<ProcessModelEvent> processModelEvents) {
        this.theProcessModelEvents = processModelEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessModel)) {
            return false;
        }
        return id != null && id.equals(((ProcessModel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProcessModel{" +
            "id=" + getId() +
            ", requirements='" + getRequirements() + "'" +
            ", pmState='" + getPmState() + "'" +
            "}";
    }
}
