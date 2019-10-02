package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A Decomposed. */
@Entity
@Table(name = "decomposed")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Decomposed extends Activity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(unique = true)
  private ProcessModel theReferedProcessModel;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ProcessModel getTheReferedProcessModel() {
    return theReferedProcessModel;
  }

  public Decomposed theReferedProcessModel(ProcessModel processModel) {
    this.theReferedProcessModel = processModel;
    return this;
  }

  public void setTheReferedProcessModel(ProcessModel processModel) {
    this.theReferedProcessModel = processModel;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Decomposed)) {
      return false;
    }
    return id != null && id.equals(((Decomposed) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Decomposed{" + "id=" + getId() + "}";
  }

  public void removeFromTheTheReferedProcessModel() {
    if (this.theReferedProcessModel != null) {
      this.theReferedProcessModel.setTheDecomposed(null);
      this.setTheReferedProcessModel(null);
    }
  }
}
