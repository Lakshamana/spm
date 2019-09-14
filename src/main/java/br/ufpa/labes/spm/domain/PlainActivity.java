package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.ufpa.labes.spm.domain.enumeration.PlainActivityStatus;

/**
 * A PlainActivity.
 */
@Entity
@Table(name = "plain_activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PlainActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "requirements")
    private String requirements;

    @Enumerated(EnumType.STRING)
    @Column(name = "plain_activity_status")
    private PlainActivityStatus plainActivityStatus;

    @Column(name = "automatic")
    private Boolean automatic;

    @OneToOne
    @JoinColumn(unique = true)
    private EnactionDescription theEnactionDescription;

    @OneToOne
    @JoinColumn(unique = true)
    private AutomaticActivity theAutomaticActivitySub;

    @OneToMany(mappedBy = "thePlainActivity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Event> theGlobalActivityEvents = new HashSet<>();

    @OneToOne(mappedBy = "thePlainActivitySub")
    @JsonIgnore
    private Activity theActivitySuper;

    @OneToMany(mappedBy = "thePlainActivity")
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

    public PlainActivity requirements(String requirements) {
        this.requirements = requirements;
        return this;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public PlainActivityStatus getPlainActivityStatus() {
        return plainActivityStatus;
    }

    public PlainActivity plainActivityStatus(PlainActivityStatus plainActivityStatus) {
        this.plainActivityStatus = plainActivityStatus;
        return this;
    }

    public void setPlainActivityStatus(PlainActivityStatus plainActivityStatus) {
        this.plainActivityStatus = plainActivityStatus;
    }

    public Boolean isAutomatic() {
        return automatic;
    }

    public PlainActivity automatic(Boolean automatic) {
        this.automatic = automatic;
        return this;
    }

    public void setAutomatic(Boolean automatic) {
        this.automatic = automatic;
    }

    public EnactionDescription getTheEnactionDescription() {
        return theEnactionDescription;
    }

    public PlainActivity theEnactionDescription(EnactionDescription enactionDescription) {
        this.theEnactionDescription = enactionDescription;
        return this;
    }

    public void setTheEnactionDescription(EnactionDescription enactionDescription) {
        this.theEnactionDescription = enactionDescription;
    }

    public AutomaticActivity getTheAutomaticActivitySub() {
        return theAutomaticActivitySub;
    }

    public PlainActivity theAutomaticActivitySub(AutomaticActivity automaticActivity) {
        this.theAutomaticActivitySub = automaticActivity;
        return this;
    }

    public void setTheAutomaticActivitySub(AutomaticActivity automaticActivity) {
        this.theAutomaticActivitySub = automaticActivity;
    }

    public Set<Event> getTheGlobalActivityEvents() {
        return theGlobalActivityEvents;
    }

    public PlainActivity theGlobalActivityEvents(Set<Event> events) {
        this.theGlobalActivityEvents = events;
        return this;
    }

    public PlainActivity addTheGlobalActivityEvent(Event event) {
        this.theGlobalActivityEvents.add(event);
        event.setThePlainActivity(this);
        return this;
    }

    public PlainActivity removeTheGlobalActivityEvent(Event event) {
        this.theGlobalActivityEvents.remove(event);
        event.setThePlainActivity(null);
        return this;
    }

    public void setTheGlobalActivityEvents(Set<Event> events) {
        this.theGlobalActivityEvents = events;
    }

    public Activity getTheActivitySuper() {
        return theActivitySuper;
    }

    public PlainActivity theActivitySuper(Activity activity) {
        this.theActivitySuper = activity;
        return this;
    }

    public void setTheActivitySuper(Activity activity) {
        this.theActivitySuper = activity;
    }

    public Set<CatalogEvent> getTheCatalogEvents() {
        return theCatalogEvents;
    }

    public PlainActivity theCatalogEvents(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEvents = catalogEvents;
        return this;
    }

    public PlainActivity addTheCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvents.add(catalogEvent);
        catalogEvent.setThePlainActivity(this);
        return this;
    }

    public PlainActivity removeTheCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvents.remove(catalogEvent);
        catalogEvent.setThePlainActivity(null);
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
        if (!(o instanceof PlainActivity)) {
            return false;
        }
        return id != null && id.equals(((PlainActivity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PlainActivity{" +
            "id=" + getId() +
            ", requirements='" + getRequirements() + "'" +
            ", plainActivityStatus='" + getPlainActivityStatus() + "'" +
            ", automatic='" + isAutomatic() + "'" +
            "}";
    }
}
