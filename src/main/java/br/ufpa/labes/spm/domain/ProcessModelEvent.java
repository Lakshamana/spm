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
 * A ProcessModelEvent.
 */
@Entity
@Table(name = "process_model_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcessModelEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theProcessModelEvents")
    private ProcessModel theProcessModel;

    @OneToOne(mappedBy = "theProcessModelEventSub")
    @JsonIgnore
    private Event theEventSuper;

    @OneToMany(mappedBy = "theProcessModelEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CatalogEvent> theCatalogEventToProcessModels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessModel getTheProcessModel() {
        return theProcessModel;
    }

    public ProcessModelEvent theProcessModel(ProcessModel processModel) {
        this.theProcessModel = processModel;
        return this;
    }

    public void setTheProcessModel(ProcessModel processModel) {
        this.theProcessModel = processModel;
    }

    public Event getTheEventSuper() {
        return theEventSuper;
    }

    public ProcessModelEvent theEventSuper(Event event) {
        this.theEventSuper = event;
        return this;
    }

    public void setTheEventSuper(Event event) {
        this.theEventSuper = event;
    }

    public Set<CatalogEvent> getTheCatalogEventToProcessModels() {
        return theCatalogEventToProcessModels;
    }

    public ProcessModelEvent theCatalogEventToProcessModels(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToProcessModels = catalogEvents;
        return this;
    }

    public ProcessModelEvent addTheCatalogEventToProcessModel(CatalogEvent catalogEvent) {
        this.theCatalogEventToProcessModels.add(catalogEvent);
        catalogEvent.setTheProcessModelEvent(this);
        return this;
    }

    public ProcessModelEvent removeTheCatalogEventToProcessModel(CatalogEvent catalogEvent) {
        this.theCatalogEventToProcessModels.remove(catalogEvent);
        catalogEvent.setTheProcessModelEvent(null);
        return this;
    }

    public void setTheCatalogEventToProcessModels(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToProcessModels = catalogEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessModelEvent)) {
            return false;
        }
        return id != null && id.equals(((ProcessModelEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProcessModelEvent{" +
            "id=" + getId() +
            "}";
    }
}
