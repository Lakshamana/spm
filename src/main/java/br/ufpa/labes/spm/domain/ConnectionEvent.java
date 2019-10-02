package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A ConnectionEvent. */
@Entity
@Table(name = "connection_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConnectionEvent extends Event implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JsonIgnoreProperties("theConnectionEvents")
  private CatalogEvent theCatalogEvent;

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

  public ConnectionEvent theCatalogEvent(CatalogEvent catalogEvent) {
    this.theCatalogEvent = catalogEvent;
    return this;
  }

  public void setTheCatalogEvent(CatalogEvent catalogEvent) {
    this.theCatalogEvent = catalogEvent;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ConnectionEvent)) {
      return false;
    }
    return id != null && id.equals(((ConnectionEvent) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ConnectionEvent{" + "id=" + getId() + "}";
  }
}
