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
public class CatalogEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private ResourceEvent theResourceEvent;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private ProcessModelEvent theProcessModelEvent;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private AgendaEvent theAgendaEvent;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private CatalogEvent theCatalogEvent;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private ConnectionEvent theConnectionEvent;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private GlobalActivityEvent theGlobalActivityEvent;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private ModelingActivityEvent theModelingActivityEvent;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private ProcessEvent theProcessEvent;

    @ManyToOne
    @JsonIgnoreProperties("theCatalogEvents")
    private Plain thePlain;

    @OneToMany(mappedBy = "theCatalogEvents")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> theEvents = new HashSet<>();

    @OneToMany(mappedBy = "theCatalogEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CatalogEvent> theCatalogEvents = new HashSet<>();

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

    public ResourceEvent getTheResourceEvent() {
        return theResourceEvent;
    }

    public CatalogEvent theResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvent = resourceEvent;
        return this;
    }

    public void setTheResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvent = resourceEvent;
    }

    public ProcessModelEvent getTheProcessModelEvent() {
        return theProcessModelEvent;
    }

    public CatalogEvent theProcessModelEvent(ProcessModelEvent processModelEvent) {
        this.theProcessModelEvent = processModelEvent;
        return this;
    }

    public void setTheProcessModelEvent(ProcessModelEvent processModelEvent) {
        this.theProcessModelEvent = processModelEvent;
    }

    public AgendaEvent getTheAgendaEvent() {
        return theAgendaEvent;
    }

    public CatalogEvent theAgendaEvent(AgendaEvent agendaEvent) {
        this.theAgendaEvent = agendaEvent;
        return this;
    }

    public void setTheAgendaEvent(AgendaEvent agendaEvent) {
        this.theAgendaEvent = agendaEvent;
    }

    public CatalogEvent getTheCatalogEvent() {
        return theCatalogEvent;
    }

    public CatalogEvent theCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvent = catalogEvent;
        return this;
    }

    public void setTheCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvent = catalogEvent;
    }

    public ConnectionEvent getTheConnectionEvent() {
        return theConnectionEvent;
    }

    public CatalogEvent theConnectionEvent(ConnectionEvent connectionEvent) {
        this.theConnectionEvent = connectionEvent;
        return this;
    }

    public void setTheConnectionEvent(ConnectionEvent connectionEvent) {
        this.theConnectionEvent = connectionEvent;
    }

    public GlobalActivityEvent getTheGlobalActivityEvent() {
        return theGlobalActivityEvent;
    }

    public CatalogEvent theGlobalActivityEvent(GlobalActivityEvent globalActivityEvent) {
        this.theGlobalActivityEvent = globalActivityEvent;
        return this;
    }

    public void setTheGlobalActivityEvent(GlobalActivityEvent globalActivityEvent) {
        this.theGlobalActivityEvent = globalActivityEvent;
    }

    public ModelingActivityEvent getTheModelingActivityEvent() {
        return theModelingActivityEvent;
    }

    public CatalogEvent theModelingActivityEvent(ModelingActivityEvent modelingActivityEvent) {
        this.theModelingActivityEvent = modelingActivityEvent;
        return this;
    }

    public void setTheModelingActivityEvent(ModelingActivityEvent modelingActivityEvent) {
        this.theModelingActivityEvent = modelingActivityEvent;
    }

    public ProcessEvent getTheProcessEvent() {
        return theProcessEvent;
    }

    public CatalogEvent theProcessEvent(ProcessEvent processEvent) {
        this.theProcessEvent = processEvent;
        return this;
    }

    public void setTheProcessEvent(ProcessEvent processEvent) {
        this.theProcessEvent = processEvent;
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

    public Set<Event> getTheEvents() {
        return theEvents;
    }

    public CatalogEvent theEvents(Set<Event> events) {
        this.theEvents = events;
        return this;
    }

    public CatalogEvent addTheEvent(Event event) {
        this.theEvents.add(event);
        event.setTheCatalogEvents(this);
        return this;
    }

    public CatalogEvent removeTheEvent(Event event) {
        this.theEvents.remove(event);
        event.setTheCatalogEvents(null);
        return this;
    }

    public void setTheEvents(Set<Event> events) {
        this.theEvents = events;
    }

    public Set<CatalogEvent> getTheCatalogEvents() {
        return theCatalogEvents;
    }

    public CatalogEvent theCatalogEvents(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEvents = catalogEvents;
        return this;
    }

    public CatalogEvent addTheCatalogEvents(CatalogEvent catalogEvent) {
        this.theCatalogEvents.add(catalogEvent);
        catalogEvent.setTheCatalogEvent(this);
        return this;
    }

    public CatalogEvent removeTheCatalogEvents(CatalogEvent catalogEvent) {
        this.theCatalogEvents.remove(catalogEvent);
        catalogEvent.setTheCatalogEvent(null);
        return this;
    }

    public void setTheCatalogEvents(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEvents = catalogEvents;
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
