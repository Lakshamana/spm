package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CatalogEvent.
 */
@Entity
@Table(name = "catalog_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CatalogEvent extends Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private Plain thePlain;

    @OneToMany(mappedBy = "theCatalogEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ResourceEvent> theResourceEvents = new HashSet<>();

    @OneToMany(mappedBy = "theCatalogEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProcessModelEvent> theProcessModelEvents = new HashSet<>();

    @OneToMany(mappedBy = "theCatalogEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgendaEvent> theAgendaEvents = new HashSet<>();

    @OneToMany(mappedBy = "theCatalogEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ConnectionEvent> theConnectionEvents = new HashSet<>();

    @OneToMany(mappedBy = "theCatalogEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GlobalActivityEvent> theGlobalActivityEvents = new HashSet<>();

    @OneToMany(mappedBy = "theCatalogEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ModelingActivityEvent> theModelingActivityEvents = new HashSet<>();

    @OneToMany(mappedBy = "theCatalogEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProcessEvent> theProcessEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public CatalogEvent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Plain getThePlain() {
        return thePlain;
    }

    public CatalogEvent thePlain(Plain plain) {
        this.thePlain = plain;
        return this;
    }

    public void setThePlain(Plain plain) {
        this.thePlain = plain;
    }

    public Set<ResourceEvent> getTheResourceEvents() {
        return theResourceEvents;
    }

    public CatalogEvent theResourceEvents(Set<ResourceEvent> resourceEvents) {
        this.theResourceEvents = resourceEvents;
        return this;
    }

    public CatalogEvent addTheResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvents.add(resourceEvent);
        resourceEvent.setTheCatalogEvent(this);
        return this;
    }

    public CatalogEvent removeTheResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvents.remove(resourceEvent);
        resourceEvent.setTheCatalogEvent(null);
        return this;
    }

    public void setTheResourceEvents(Set<ResourceEvent> resourceEvents) {
        this.theResourceEvents = resourceEvents;
    }

    public Set<ProcessModelEvent> getTheProcessModelEvents() {
        return theProcessModelEvents;
    }

    public CatalogEvent theProcessModelEvents(Set<ProcessModelEvent> processModelEvents) {
        this.theProcessModelEvents = processModelEvents;
        return this;
    }

    public CatalogEvent addTheProcessModelEvent(ProcessModelEvent processModelEvent) {
        this.theProcessModelEvents.add(processModelEvent);
        processModelEvent.setTheCatalogEvent(this);
        return this;
    }

    public CatalogEvent removeTheProcessModelEvent(ProcessModelEvent processModelEvent) {
        this.theProcessModelEvents.remove(processModelEvent);
        processModelEvent.setTheCatalogEvent(null);
        return this;
    }

    public void setTheProcessModelEvents(Set<ProcessModelEvent> processModelEvents) {
        this.theProcessModelEvents = processModelEvents;
    }

    public Set<AgendaEvent> getTheAgendaEvents() {
        return theAgendaEvents;
    }

    public CatalogEvent theAgendaEvents(Set<AgendaEvent> agendaEvents) {
        this.theAgendaEvents = agendaEvents;
        return this;
    }

    public CatalogEvent addTheAgendaEvent(AgendaEvent agendaEvent) {
        this.theAgendaEvents.add(agendaEvent);
        agendaEvent.setTheCatalogEvent(this);
        return this;
    }

    public CatalogEvent removeTheAgendaEvent(AgendaEvent agendaEvent) {
        this.theAgendaEvents.remove(agendaEvent);
        agendaEvent.setTheCatalogEvent(null);
        return this;
    }

    public void setTheAgendaEvents(Set<AgendaEvent> agendaEvents) {
        this.theAgendaEvents = agendaEvents;
    }

    public Set<ConnectionEvent> getTheConnectionEvents() {
        return theConnectionEvents;
    }

    public CatalogEvent theConnectionEvents(Set<ConnectionEvent> connectionEvents) {
        this.theConnectionEvents = connectionEvents;
        return this;
    }

    public CatalogEvent addTheConnectionEvent(ConnectionEvent connectionEvent) {
        this.theConnectionEvents.add(connectionEvent);
        connectionEvent.setTheCatalogEvent(this);
        return this;
    }

    public CatalogEvent removeTheConnectionEvent(ConnectionEvent connectionEvent) {
        this.theConnectionEvents.remove(connectionEvent);
        connectionEvent.setTheCatalogEvent(null);
        return this;
    }

    public void setTheConnectionEvents(Set<ConnectionEvent> connectionEvents) {
        this.theConnectionEvents = connectionEvents;
    }

    public Set<GlobalActivityEvent> getTheGlobalActivityEvents() {
        return theGlobalActivityEvents;
    }

    public CatalogEvent theGlobalActivityEvents(Set<GlobalActivityEvent> globalActivityEvents) {
        this.theGlobalActivityEvents = globalActivityEvents;
        return this;
    }

    public CatalogEvent addTheGlobalActivityEvent(GlobalActivityEvent globalActivityEvent) {
        this.theGlobalActivityEvents.add(globalActivityEvent);
        globalActivityEvent.setTheCatalogEvent(this);
        return this;
    }

    public CatalogEvent removeTheGlobalActivityEvent(GlobalActivityEvent globalActivityEvent) {
        this.theGlobalActivityEvents.remove(globalActivityEvent);
        globalActivityEvent.setTheCatalogEvent(null);
        return this;
    }

    public void setTheGlobalActivityEvents(Set<GlobalActivityEvent> globalActivityEvents) {
        this.theGlobalActivityEvents = globalActivityEvents;
    }

    public Set<ModelingActivityEvent> getTheModelingActivityEvents() {
        return theModelingActivityEvents;
    }

    public CatalogEvent theModelingActivityEvents(Set<ModelingActivityEvent> modelingActivityEvents) {
        this.theModelingActivityEvents = modelingActivityEvents;
        return this;
    }

    public CatalogEvent addTheModelingActivityEvent(ModelingActivityEvent modelingActivityEvent) {
        this.theModelingActivityEvents.add(modelingActivityEvent);
        modelingActivityEvent.setTheCatalogEvent(this);
        return this;
    }

    public CatalogEvent removeTheModelingActivityEvent(ModelingActivityEvent modelingActivityEvent) {
        this.theModelingActivityEvents.remove(modelingActivityEvent);
        modelingActivityEvent.setTheCatalogEvent(null);
        return this;
    }

    public void setTheModelingActivityEvents(Set<ModelingActivityEvent> modelingActivityEvents) {
        this.theModelingActivityEvents = modelingActivityEvents;
    }

    public Set<ProcessEvent> getTheProcessEvents() {
        return theProcessEvents;
    }

    public CatalogEvent theProcessEvents(Set<ProcessEvent> processEvents) {
        this.theProcessEvents = processEvents;
        return this;
    }

    public CatalogEvent addTheProcessEvent(ProcessEvent processEvent) {
        this.theProcessEvents.add(processEvent);
        processEvent.setTheCatalogEvent(this);
        return this;
    }

    public CatalogEvent removeTheProcessEvent(ProcessEvent processEvent) {
        this.theProcessEvents.remove(processEvent);
        processEvent.setTheCatalogEvent(null);
        return this;
    }

    public void setTheProcessEvents(Set<ProcessEvent> processEvents) {
        this.theProcessEvents = processEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogEvent)) {
            return false;
        }
        return id != null && id.equals(((CatalogEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CatalogEvent{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
