package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A ResourceInstSug. */
@Entity
@Table(name = "resource_inst_sug")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResourceInstSug implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ResourceInstSug)) {
      return false;
    }
    return id != null && id.equals(((ResourceInstSug) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ResourceInstSug{" + "id=" + getId() + "}";
  }
}
