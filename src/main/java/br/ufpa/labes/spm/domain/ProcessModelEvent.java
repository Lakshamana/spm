package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A ProcessModelEvent. */
@Entity
@Table(name = "process_model_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcessModelEvent extends Event implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JsonIgnoreProperties("theProcessModelEvents")
  private CatalogEvent theCatalogEvent;

  @ManyToOne
  @JsonIgnoreProperties("theProcessModelEvents")
  private ProcessModel theProcessModel;

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

  public ProcessModelEvent theCatalogEvent(CatalogEvent catalogEvent) {
    this.theCatalogEvent = catalogEvent;
    return this;
  }

  public void setTheCatalogEvent(CatalogEvent catalogEvent) {
    this.theCatalogEvent = catalogEvent;
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
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

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
    return "ProcessModelEvent{" + "id=" + getId() + "}";
  }
}
