package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/** A Event. */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.JOINED)
public class Event implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  @Column(name = "why")
  private String why;

  @Column(name = "jhi_when")
  private LocalDate when;

  @Column(name = "is_created_by_apsee")
  private Boolean isCreatedByApsee;

  @OneToOne
  @JoinColumn(unique = true)
  private AgendaEvent theAgendaEventSub;

  @OneToOne
  @JoinColumn(unique = true)
  private CatalogEvent theCatalogEventSub;

  @OneToOne
  @JoinColumn(unique = true)
  private ConnectionEvent theConnectionEventSub;

  @OneToOne
  @JoinColumn(unique = true)
  private GlobalActivityEvent theGlobalActivityEventSub;

  @OneToOne
  @JoinColumn(unique = true)
  private ModelingActivityEvent theModelingActivityEventSub;

  @OneToOne
  @JoinColumn(unique = true)
  private ProcessEvent theProcessEventSub;

  @OneToOne
  @JoinColumn(unique = true)
  private ProcessModelEvent theProcessModelEventSub;

  @OneToOne
  @JoinColumn(unique = true)
  private ResourceEvent theResourceEventSub;

  @ManyToOne
  @JsonIgnoreProperties("theAgendaEvents")
  private CatalogEvent theCatalogEvents;

  @ManyToOne
  @JsonIgnoreProperties("theAgendaEvents")
  private Task theTask;

  @ManyToOne
  @JsonIgnoreProperties("theEvents")
  private SpmLog theLog;

  @ManyToOne
  @JsonIgnoreProperties("theEvents")
  private EventType theEventType;

  @ManyToOne
  @JsonIgnoreProperties("theModelingActivityEvents")
  private Activity theActivity;

  @ManyToOne
  @JsonIgnoreProperties("theGlobalActivityEvents")
  private Plain thePlain;

  @ManyToOne
  @JsonIgnoreProperties("theModelingActivityEvents")
  private Agent theAgent;

  @OneToMany(mappedBy = "theResourceEvent")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Agent> theRequestorAgents = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getWhy() {
    return why;
  }

  public Event why(String why) {
    this.why = why;
    return this;
  }

  public void setWhy(String why) {
    this.why = why;
  }

  public LocalDate getWhen() {
    return when;
  }

  public Event when(LocalDate when) {
    this.when = when;
    return this;
  }

  public void setWhen(LocalDate when) {
    this.when = when;
  }

  public Boolean isIsCreatedByApsee() {
    return isCreatedByApsee;
  }

  public Event isCreatedByApsee(Boolean isCreatedByApsee) {
    this.isCreatedByApsee = isCreatedByApsee;
    return this;
  }

  public void setIsCreatedByApsee(Boolean isCreatedByApsee) {
    this.isCreatedByApsee = isCreatedByApsee;
  }

  public AgendaEvent getTheAgendaEventSub() {
    return theAgendaEventSub;
  }

  public Event theAgendaEventSub(AgendaEvent agendaEvent) {
    this.theAgendaEventSub = agendaEvent;
    return this;
  }

  public void setTheAgendaEventSub(AgendaEvent agendaEvent) {
    this.theAgendaEventSub = agendaEvent;
  }

  public CatalogEvent getTheCatalogEventSub() {
    return theCatalogEventSub;
  }

  public Event theCatalogEventSub(CatalogEvent catalogEvent) {
    this.theCatalogEventSub = catalogEvent;
    return this;
  }

  public void setTheCatalogEventSub(CatalogEvent catalogEvent) {
    this.theCatalogEventSub = catalogEvent;
  }

  public ConnectionEvent getTheConnectionEventSub() {
    return theConnectionEventSub;
  }

  public Event theConnectionEventSub(ConnectionEvent connectionEvent) {
    this.theConnectionEventSub = connectionEvent;
    return this;
  }

  public void setTheConnectionEventSub(ConnectionEvent connectionEvent) {
    this.theConnectionEventSub = connectionEvent;
  }

  public GlobalActivityEvent getTheGlobalActivityEventSub() {
    return theGlobalActivityEventSub;
  }

  public Event theGlobalActivityEventSub(GlobalActivityEvent globalActivityEvent) {
    this.theGlobalActivityEventSub = globalActivityEvent;
    return this;
  }

  public void setTheGlobalActivityEventSub(GlobalActivityEvent globalActivityEvent) {
    this.theGlobalActivityEventSub = globalActivityEvent;
  }

  public ModelingActivityEvent getTheModelingActivityEventSub() {
    return theModelingActivityEventSub;
  }

  public Event theModelingActivityEventSub(ModelingActivityEvent modelingActivityEvent) {
    this.theModelingActivityEventSub = modelingActivityEvent;
    return this;
  }

  public void setTheModelingActivityEventSub(ModelingActivityEvent modelingActivityEvent) {
    this.theModelingActivityEventSub = modelingActivityEvent;
  }

  public ProcessEvent getTheProcessEventSub() {
    return theProcessEventSub;
  }

  public Event theProcessEventSub(ProcessEvent processEvent) {
    this.theProcessEventSub = processEvent;
    return this;
  }

  public void setTheProcessEventSub(ProcessEvent processEvent) {
    this.theProcessEventSub = processEvent;
  }

  public ProcessModelEvent getTheProcessModelEventSub() {
    return theProcessModelEventSub;
  }

  public Event theProcessModelEventSub(ProcessModelEvent processModelEvent) {
    this.theProcessModelEventSub = processModelEvent;
    return this;
  }

  public void setTheProcessModelEventSub(ProcessModelEvent processModelEvent) {
    this.theProcessModelEventSub = processModelEvent;
  }

  public ResourceEvent getTheResourceEventSub() {
    return theResourceEventSub;
  }

  public Event theResourceEventSub(ResourceEvent resourceEvent) {
    this.theResourceEventSub = resourceEvent;
    return this;
  }

  public void setTheResourceEventSub(ResourceEvent resourceEvent) {
    this.theResourceEventSub = resourceEvent;
  }

  public CatalogEvent getTheCatalogEvents() {
    return theCatalogEvents;
  }

  public Event theCatalogEvents(CatalogEvent catalogEvent) {
    this.theCatalogEvents = catalogEvent;
    return this;
  }

  public void setTheCatalogEvents(CatalogEvent catalogEvent) {
    this.theCatalogEvents = catalogEvent;
  }

  public Task getTheTask() {
    return theTask;
  }

  public Event theTask(Task task) {
    this.theTask = task;
    return this;
  }

  public void setTheTask(Task task) {
    this.theTask = task;
  }

  public SpmLog getTheLog() {
    return theLog;
  }

  public Event theLog(SpmLog spmLog) {
    this.theLog = spmLog;
    return this;
  }

  public void setTheLog(SpmLog spmLog) {
    this.theLog = spmLog;
  }

  public EventType getTheEventType() {
    return theEventType;
  }

  public Event theEventType(EventType eventType) {
    this.theEventType = eventType;
    return this;
  }

  public void setTheEventType(EventType eventType) {
    this.theEventType = eventType;
  }

  public Activity getTheActivity() {
    return theActivity;
  }

  public Event theActivity(Activity activity) {
    this.theActivity = activity;
    return this;
  }

  public void setTheActivity(Activity activity) {
    this.theActivity = activity;
  }

  public Plain getThePlain() {
    return thePlain;
  }

  public Event thePlain(Plain plain) {
    this.thePlain = plain;
    return this;
  }

  public void setThePlain(Plain plain) {
    this.thePlain = plain;
  }

  public Agent getTheAgent() {
    return theAgent;
  }

  public Event theAgent(Agent agent) {
    this.theAgent = agent;
    return this;
  }

  public void setTheAgent(Agent agent) {
    this.theAgent = agent;
  }

  public Set<Agent> getTheRequestorAgents() {
    return theRequestorAgents;
  }

  public Event theRequestorAgents(Set<Agent> agents) {
    this.theRequestorAgents = agents;
    return this;
  }

  public Event addTheRequestorAgent(Agent agent) {
    this.theRequestorAgents.add(agent);
    agent.setTheResourceEvent(this);
    return this;
  }

  public Event removeTheRequestorAgent(Agent agent) {
    this.theRequestorAgents.remove(agent);
    agent.setTheResourceEvent(null);
    return this;
  }

  public void setTheRequestorAgents(Set<Agent> agents) {
    this.theRequestorAgents = agents;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Event)) {
      return false;
    }
    return id != null && id.equals(((Event) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Event{"
        + "id="
        + getId()
        + ", why='"
        + getWhy()
        + "'"
        + ", when='"
        + getWhen()
        + "'"
        + ", isCreatedByApsee='"
        + isIsCreatedByApsee()
        + "'"
        + "}";
  }
}
