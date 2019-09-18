package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ConnectionEvent.
 */
@Entity
@Table(name = "connection_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConnectionEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "theConnectionEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CatalogEvent> theCatalogEventToConnections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CatalogEvent> getTheCatalogEventToConnections() {
        return theCatalogEventToConnections;
    }

    public ConnectionEvent theCatalogEventToConnections(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToConnections = catalogEvents;
        return this;
    }

    public ConnectionEvent addTheCatalogEventToConnection(CatalogEvent catalogEvent) {
        this.theCatalogEventToConnections.add(catalogEvent);
        catalogEvent.setTheConnectionEvent(this);
        return this;
    }

    public ConnectionEvent removeTheCatalogEventToConnection(CatalogEvent catalogEvent) {
        this.theCatalogEventToConnections.remove(catalogEvent);
        catalogEvent.setTheConnectionEvent(null);
        return this;
    }

    public void setTheCatalogEventToConnections(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToConnections = catalogEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConnectionEvent)) {
            return false;
        }
        return id != null && id.equals(((ConnectionEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConnectionEvent{" +
            "id=" + getId() +
            "}";
    }
}
