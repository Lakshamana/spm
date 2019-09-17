package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.ufpa.labes.spm.domain.enumeration.PlainStatus;

/**
 * A Plain.
 */
@Entity
@Table(name = "plain")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.JOINED)
public class Plain extends Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "requirements")
    private String requirements;

    @Enumerated(EnumType.STRING)
    @Column(name = "plain_status")
    private PlainStatus plainStatus;

    @Column(name = "automatic")
    private Boolean automatic;

    @OneToOne
    @JoinColumn(unique = true)
    private EnactionDescription theEnactionDescription;

    @OneToMany(mappedBy = "thePlain")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> theGlobalActivityEvents = new HashSet<>();

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

    public PlainStatus getPlainStatus() {
        return plainStatus;
    }

    public Plain plainStatus(PlainStatus plainStatus) {
        this.plainStatus = plainStatus;
        return this;
    }

    public void setPlainStatus(PlainStatus plainStatus) {
        this.plainStatus = plainStatus;
    }

    public Boolean isAutomatic() {
        return automatic;
    }

    public Plain automatic(Boolean automatic) {
        this.automatic = automatic;
        return this;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
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

    public Set<Event> getTheGlobalActivityEvents() {
        return theGlobalActivityEvents;
    }

    public Plain theGlobalActivityEvents(Set<Event> events) {
        this.theGlobalActivityEvents = events;
        return this;
    }

    public Plain addTheGlobalActivityEvent(Event event) {
        this.theGlobalActivityEvents.add(event);
        event.setThePlain(this);
        return this;
    }

    public Plain removeTheGlobalActivityEvent(Event event) {
        this.theGlobalActivityEvents.remove(event);
        event.setThePlain(null);
        return this;
    }

    public void setTheGlobalActivityEvents(Set<Event> events) {
        this.theGlobalActivityEvents = events;
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
            ", plainStatus='" + getPlainStatus() + "'" +
            ", automatic='" + isAutomatic() + "'" +
            "}";
    }
}
