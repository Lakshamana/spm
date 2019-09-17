package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A CatalogEvent. */
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
  @JsonIgnoreProperties("theCatalogEventToAgenda")
  private AgendaEvent theAgendaEvent;

  @ManyToOne
  @JsonIgnoreProperties("theCatalogEventToCatalogs")
  private CatalogEvent theCatalogEvent;

  @ManyToOne
  @JsonIgnoreProperties("theCatalogEventToConnections")
  private ConnectionEvent theConnectionEvent;

  @ManyToOne
  @JsonIgnoreProperties("theCatalogEventToGlobalActivities")
  private GlobalActivityEvent theGlobalActivityEvent;

  @ManyToOne
  @JsonIgnoreProperties("theCatalogEventToModelingActivities")
  private ModelingActivityEvent theModelingActivityEvent;

  @ManyToOne
  @JsonIgnoreProperties("theCatalogEventToProcesses")
  private ProcessEvent theProcessEvent;

  @ManyToOne
  @JsonIgnoreProperties("theCatalogEventToProcessModels")
  private ProcessModelEvent theProcessModelEvent;

  @ManyToOne
  @JsonIgnoreProperties("theCatalogEventToResources")
  private ResourceEvent theResourceEvent;

  @ManyToOne
  @JsonIgnoreProperties("theCatalogEvents")
  private Plain thePlain;

  @OneToMany(mappedBy = "theCatalogEvents")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Event> theAgendaEvents = new HashSet<>();

  @OneToMany(mappedBy = "theCatalogEvent")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<CatalogEvent> theCatalogEventToCatalogs = new HashSet<>();

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

  public Set<Event> getTheAgendaEvents() {
    return theAgendaEvents;
  }

  public CatalogEvent theAgendaEvents(Set<Event> events) {
    this.theAgendaEvents = events;
    return this;
  }

  public CatalogEvent addTheAgendaEvent(Event event) {
    this.theAgendaEvents.add(event);
    event.setTheCatalogEvents(this);
    return this;
  }

  public CatalogEvent removeTheAgendaEvent(Event event) {
    this.theAgendaEvents.remove(event);
    event.setTheCatalogEvents(null);
    return this;
  }

  public void setTheAgendaEvents(Set<Event> events) {
    this.theAgendaEvents = events;
  }

  public Set<CatalogEvent> getTheCatalogEventToCatalogs() {
    return theCatalogEventToCatalogs;
  }

  public CatalogEvent theCatalogEventToCatalogs(Set<CatalogEvent> catalogEvents) {
    this.theCatalogEventToCatalogs = catalogEvents;
    return this;
  }

  public CatalogEvent addTheCatalogEventToCatalog(CatalogEvent catalogEvent) {
    this.theCatalogEventToCatalogs.add(catalogEvent);
    catalogEvent.setTheCatalogEvent(this);
    return this;
  }

  public CatalogEvent removeTheCatalogEventToCatalog(CatalogEvent catalogEvent) {
    this.theCatalogEventToCatalogs.remove(catalogEvent);
    catalogEvent.setTheCatalogEvent(null);
    return this;
  }

  public void setTheCatalogEventToCatalogs(Set<CatalogEvent> catalogEvents) {
    this.theCatalogEventToCatalogs = catalogEvents;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

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
    return "CatalogEvent{" + "id=" + getId() + ", description='" + getDescription() + "'" + "}";
  }
}
