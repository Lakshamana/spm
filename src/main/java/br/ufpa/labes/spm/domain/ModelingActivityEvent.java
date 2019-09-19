package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ModelingActivityEvent.
 */
@Entity
@Table(name = "modeling_activity_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ModelingActivityEvent extends Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "theModelingActivityEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CatalogEvent> theCatalogEventToModelingActivities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<CatalogEvent> getTheCatalogEventToModelingActivities() {
        return theCatalogEventToModelingActivities;
    }

    public ModelingActivityEvent theCatalogEventToModelingActivities(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToModelingActivities = catalogEvents;
        return this;
    }

    public ModelingActivityEvent addTheCatalogEventToModelingActivity(CatalogEvent catalogEvent) {
        this.theCatalogEventToModelingActivities.add(catalogEvent);
        catalogEvent.setTheModelingActivityEvent(this);
        return this;
    }

    public ModelingActivityEvent removeTheCatalogEventToModelingActivity(CatalogEvent catalogEvent) {
        this.theCatalogEventToModelingActivities.remove(catalogEvent);
        catalogEvent.setTheModelingActivityEvent(null);
        return this;
    }

    public void setTheCatalogEventToModelingActivities(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToModelingActivities = catalogEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModelingActivityEvent)) {
            return false;
        }
        return id != null && id.equals(((ModelingActivityEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ModelingActivityEvent{" +
            "id=" + getId() +
            "}";
    }
}
