package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ResourceEvent.
 */
@Entity
@Table(name = "resource_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResourceEvent extends Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Normal theNormal;

    @ManyToOne
    @JsonIgnoreProperties("theResourceEvents")
    private Resource theResource;

    @OneToOne(mappedBy = "theResourceEventSub")
    @JsonIgnore
    private Event theEventSuper;

    @OneToMany(mappedBy = "theResourceEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CatalogEvent> theCatalogEventToResources = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Normal getTheNormal() {
        return theNormal;
    }

    public ResourceEvent theNormal(Normal normal) {
        this.theNormal = normal;
        return this;
    }

    public void setTheNormal(Normal normal) {
        this.theNormal = normal;
    }

    public Resource getTheResource() {
        return theResource;
    }

    public ResourceEvent theResource(Resource resource) {
        this.theResource = resource;
        return this;
    }

    public void setTheResource(Resource resource) {
        this.theResource = resource;
    }

    public Event getTheEventSuper() {
        return theEventSuper;
    }

    public ResourceEvent theEventSuper(Event event) {
        this.theEventSuper = event;
        return this;
    }

    public void setTheEventSuper(Event event) {
        this.theEventSuper = event;
    }

    public Set<CatalogEvent> getTheCatalogEventToResources() {
        return theCatalogEventToResources;
    }

    public ResourceEvent theCatalogEventToResources(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToResources = catalogEvents;
        return this;
    }

    public ResourceEvent addTheCatalogEventToResource(CatalogEvent catalogEvent) {
        this.theCatalogEventToResources.add(catalogEvent);
        catalogEvent.setTheResourceEvent(this);
        return this;
    }

    public ResourceEvent removeTheCatalogEventToResource(CatalogEvent catalogEvent) {
        this.theCatalogEventToResources.remove(catalogEvent);
        catalogEvent.setTheResourceEvent(null);
        return this;
    }

    public void setTheCatalogEventToResources(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToResources = catalogEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceEvent)) {
            return false;
        }
        return id != null && id.equals(((ResourceEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ResourceEvent{" +
            "id=" + getId() +
            "}";
    }
}
