package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @OneToOne(mappedBy = "theDecomposedSub")
  @JsonIgnore
  private Activity theActivitySuper;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Activity getTheActivitySuper() {
    return theActivitySuper;
  }

  public Decomposed theActivitySuper(Activity activity) {
    this.theActivitySuper = activity;
    return this;
  }

  public void setTheActivitySuper(Activity activity) {
    this.theActivitySuper = activity;
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
}
