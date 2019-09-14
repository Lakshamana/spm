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
 * A ProcessEvent.
 */
@Entity
@Table(name = "process_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcessEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theProcessEvents")
    private Process theProcess;

    @OneToOne(mappedBy = "theProcessEventSub")
    @JsonIgnore
    private Event theEventSuper;

    @OneToMany(mappedBy = "theProcessEvent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CatalogEvent> theCatalogEventToProcesses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Process getTheProcess() {
        return theProcess;
    }

    public ProcessEvent theProcess(Process process) {
        this.theProcess = process;
        return this;
    }

    public void setTheProcess(Process process) {
        this.theProcess = process;
    }

    public Event getTheEventSuper() {
        return theEventSuper;
    }

    public ProcessEvent theEventSuper(Event event) {
        this.theEventSuper = event;
        return this;
    }

    public void setTheEventSuper(Event event) {
        this.theEventSuper = event;
    }

    public Set<CatalogEvent> getTheCatalogEventToProcesses() {
        return theCatalogEventToProcesses;
    }

    public ProcessEvent theCatalogEventToProcesses(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToProcesses = catalogEvents;
        return this;
    }

    public ProcessEvent addTheCatalogEventToProcess(CatalogEvent catalogEvent) {
        this.theCatalogEventToProcesses.add(catalogEvent);
        catalogEvent.setTheProcessEvent(this);
        return this;
    }

    public ProcessEvent removeTheCatalogEventToProcess(CatalogEvent catalogEvent) {
        this.theCatalogEventToProcesses.remove(catalogEvent);
        catalogEvent.setTheProcessEvent(null);
        return this;
    }

    public void setTheCatalogEventToProcesses(Set<CatalogEvent> catalogEvents) {
        this.theCatalogEventToProcesses = catalogEvents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcessEvent)) {
            return false;
        }
        return id != null && id.equals(((ProcessEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProcessEvent{" +
            "id=" + getId() +
            "}";
    }
}
