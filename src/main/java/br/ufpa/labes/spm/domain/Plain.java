package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Plain.
 */
@Entity
@Table(name = "plain")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.JOINED)
public class Plain extends Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final transient String WAITING = "Waiting", READY = "Ready",
    FAILED = "Failed", CANCELED = "Canceled", ACTIVE = "Active",
    PAUSED = "Paused", FINISHED = "Finished";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "requirements")
    private String requirements;

    @OneToOne
    @JoinColumn(unique = true)
    private EnactionDescription theEnactionDescription;

    @OneToMany(mappedBy = "thePlain")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GlobalActivityEvent> theGlobalActivityEvents = new HashSet<>();

    @OneToMany(mappedBy = "thePlain")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CatalogEvent> theCatalogEvents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequirements() {
        return requirements;
    }

    public Plain requirements(String requirements) {
        this.requirements = requirements;
        return this;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public EnactionDescription getTheEnactionDescription() {
        return theEnactionDescription;
    }

    public Plain theEnactionDescription(EnactionDescription enactionDescription) {
        this.theEnactionDescription = enactionDescription;
        return this;
    }

    public void setTheEnactionDescription(EnactionDescription enactionDescription) {
        this.theEnactionDescription = enactionDescription;
    }

    public Set<GlobalActivityEvent> getTheGlobalActivityEvents() {
        return theGlobalActivityEvents;
    }

    public Plain theGlobalActivityEvents(Set<GlobalActivityEvent> globalActivityEvents) {
        this.theGlobalActivityEvents = globalActivityEvents;
        return this;
    }

    public Plain addTheGlobalActivityEvent(GlobalActivityEvent globalActivityEvent) {
        this.theGlobalActivityEvents.add(globalActivityEvent);
        globalActivityEvent.setThePlain(this);
        return this;
    }

    public Plain removeTheGlobalActivityEvent(GlobalActivityEvent globalActivityEvent) {
        this.theGlobalActivityEvents.remove(globalActivityEvent);
        globalActivityEvent.setThePlain(null);
        return this;
    }

    public void setTheGlobalActivityEvents(Set<GlobalActivityEvent> globalActivityEvents) {
        this.theGlobalActivityEvents = globalActivityEvents;
    }

    public Set<CatalogEvent> getTheCatalogEvents() {
        return theCatalogEvents;
    }

    public Plain theCatalogEvents(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEvents = catalogEvents;
        return this;
    }

    public Plain addTheCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvents.add(catalogEvent);
        catalogEvent.setThePlain(this);
        return this;
    }

    public Plain removeTheCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvents.remove(catalogEvent);
        catalogEvent.setThePlain(null);
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
        if (!(o instanceof Plain)) {
            return false;
        }
        return id != null && id.equals(((Plain) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Plain{" +
            "id=" + getId() +
            ", requirements='" + getRequirements() + "'" +
            "}";
    }
}
