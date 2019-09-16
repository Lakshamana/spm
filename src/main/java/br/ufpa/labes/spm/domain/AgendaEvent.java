package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AgendaEvent.
 */
@Entity
@Table(name = "agenda_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AgendaEvent extends Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "theAgendaEventSub")
    @JsonIgnore
    private Event theEventSuper;

    @OneToMany(mappedBy = "theAgendaEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CatalogEvent> theCatalogEventToAgenda = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getTheEventSuper() {
        return theEventSuper;
    }

    public AgendaEvent theEventSuper(Event event) {
        this.theEventSuper = event;
        return this;
    }

    public void setTheEventSuper(Event event) {
        this.theEventSuper = event;
    }

    public Set<CatalogEvent> getTheCatalogEventToAgenda() {
        return theCatalogEventToAgenda;
    }

    public AgendaEvent theCatalogEventToAgenda(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToAgenda = catalogEvents;
        return this;
    }

    public AgendaEvent addTheCatalogEventToAgenda(CatalogEvent catalogEvent) {
        this.theCatalogEventToAgenda.add(catalogEvent);
        catalogEvent.setTheAgendaEvent(this);
        return this;
    }

    public AgendaEvent removeTheCatalogEventToAgenda(CatalogEvent catalogEvent) {
        this.theCatalogEventToAgenda.remove(catalogEvent);
        catalogEvent.setTheAgendaEvent(null);
        return this;
    }

    public void setTheCatalogEventToAgenda(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToAgenda = catalogEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgendaEvent)) {
            return false;
        }
        return id != null && id.equals(((AgendaEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AgendaEvent{" +
            "id=" + getId() +
            "}";
    }
}
