package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A GlobalActivityEvent.
 */
@Entity
@Table(name = "global_activity_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GlobalActivityEvent extends Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theGlobalActivityEvents")
    private CatalogEvent theCatalogEvent;

    @ManyToOne
    @JsonIgnoreProperties("theGlobalActivityEvents")
    private Plain thePlain;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogEvent getTheCatalogEvent() {
        return theCatalogEvent;
    }

    public GlobalActivityEvent theCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvent = catalogEvent;
        return this;
    }

    public void setTheCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvent = catalogEvent;
    }

    public Plain getThePlain() {
        return thePlain;
    }

    public GlobalActivityEvent thePlain(Plain plain) {
        this.thePlain = plain;
        return this;
    }

    public void setThePlain(Plain plain) {
        this.thePlain = plain;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalActivityEvent)) {
            return false;
        }
        return id != null && id.equals(((GlobalActivityEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GlobalActivityEvent{" +
            "id=" + getId() +
            "}";
    }
}
