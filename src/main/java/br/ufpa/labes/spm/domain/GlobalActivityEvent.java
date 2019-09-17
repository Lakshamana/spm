package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A GlobalActivityEvent. */
@Entity
@Table(name = "global_activity_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GlobalActivityEvent extends Event implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "theGlobalActivityEvent")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<CatalogEvent> theCatalogEventToGlobalActivities = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<CatalogEvent> getTheCatalogEventToGlobalActivities() {
    return theCatalogEventToGlobalActivities;
  }

  public GlobalActivityEvent theCatalogEventToGlobalActivities(Set<CatalogEvent> catalogEvents) {
    this.theCatalogEventToGlobalActivities = catalogEvents;
    return this;
  }

  public GlobalActivityEvent addTheCatalogEventToGlobalActivity(CatalogEvent catalogEvent) {
    this.theCatalogEventToGlobalActivities.add(catalogEvent);
    catalogEvent.setTheGlobalActivityEvent(this);
    return this;
  }

  public GlobalActivityEvent removeTheCatalogEventToGlobalActivity(CatalogEvent catalogEvent) {
    this.theCatalogEventToGlobalActivities.remove(catalogEvent);
    catalogEvent.setTheGlobalActivityEvent(null);
    return this;
  }

  public void setTheCatalogEventToGlobalActivities(Set<CatalogEvent> catalogEvents) {
    this.theCatalogEventToGlobalActivities = catalogEvents;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

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
    return "GlobalActivityEvent{" + "id=" + getId() + "}";
  }
}
