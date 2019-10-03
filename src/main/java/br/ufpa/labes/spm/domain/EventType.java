package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A EventType.
 */
@Entity
@Table(name = "event_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EventType extends Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "theEventType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> theEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Event> getTheEvents() {
        return theEvents;
    }

    public EventType theEvents(Set<Event> events) {
        this.theEvents = events;
        return this;
    }

    public EventType addTheEvent(Event event) {
        this.theEvents.add(event);
        event.setTheEventType(this);
        return this;
    }

    public EventType removeTheEvent(Event event) {
        this.theEvents.remove(event);
        event.setTheEventType(null);
        return this;
    }

    public void setTheEvents(Set<Event> events) {
        this.theEvents = events;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventType)) {
            return false;
        }
        return id != null && id.equals(((EventType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EventType{" +
            "id=" + getId() +
            "}";
    }
}
