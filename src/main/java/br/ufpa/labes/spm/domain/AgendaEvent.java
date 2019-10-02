package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

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

    @ManyToOne
    @JsonIgnoreProperties("theAgendaEvents")
    private CatalogEvent theCatalogEvent;

    @ManyToOne
    @JsonIgnoreProperties("theAgendaEvents")
    private Task theTask;

    @ManyToOne
    @JsonIgnoreProperties("theAgendaEvents")
    private Normal theNormal;

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

    public AgendaEvent theCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvent = catalogEvent;
        return this;
    }

    public void setTheCatalogEvent(CatalogEvent catalogEvent) {
        this.theCatalogEvent = catalogEvent;
    }

    public Task getTheTask() {
        return theTask;
    }

    public AgendaEvent theTask(Task task) {
        this.theTask = task;
        return this;
    }

    public void setTheTask(Task task) {
        this.theTask = task;
    }

    public Normal getTheNormal() {
        return theNormal;
    }

    public AgendaEvent theNormal(Normal normal) {
        this.theNormal = normal;
        return this;
    }

    public void setTheNormal(Normal normal) {
        this.theNormal = normal;
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
