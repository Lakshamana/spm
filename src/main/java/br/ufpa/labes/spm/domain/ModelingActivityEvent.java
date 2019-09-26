package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne
    @JsonIgnoreProperties("theModelingActivityEvents")
    private Activity theActivity;

    @ManyToOne
    @JsonIgnoreProperties("theModelingActivityEvents")
    private Agent theAgent;

    @OneToMany(mappedBy = "theModelingActivityEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CatalogEvent> theCatalogEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getTheActivity() {
        return theActivity;
    }

    public ModelingActivityEvent theActivity(Activity activity) {
        this.theActivity = activity;
        return this;
    }

    public void setTheActivity(Activity activity) {
        this.theActivity = activity;
    }

    public Agent getTheAgent() {
        return theAgent;
    }

    public ModelingActivityEvent theAgent(Agent agent) {
        this.theAgent = agent;
        return this;
    }

    public void setTheAgent(Agent agent) {
        this.theAgent = agent;
    }

    public Set<CatalogEvent> getTheCatalogEvents() {
        return theCatalogEvents;
    }

    public ModelingActivityEvent theCatalogEvents(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEvents = catalogEvents;
        return this;
    }

    public ModelingActivityEvent addTheCatalogEvents(CatalogEvent catalogEvent) {
        this.theCatalogEvents.add(catalogEvent);
        catalogEvent.setTheModelingActivityEvent(this);
        return this;
    }

    public ModelingActivityEvent removeTheCatalogEvents(CatalogEvent catalogEvent) {
        this.theCatalogEvents.remove(catalogEvent);
        catalogEvent.setTheModelingActivityEvent(null);
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
