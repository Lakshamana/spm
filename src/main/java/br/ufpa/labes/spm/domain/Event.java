package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return "Event{" +
            "id=" + getId() +
            ", why='" + getWhy() + "'" +
            ", when='" + getWhen() + "'" +
            ", isCreatedByApsee='" + isIsCreatedByApsee() + "'" +
            "}";
    }
}
