package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/** A EnactionDescription. */
@Entity
@Table(name = "enaction_description")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EnactionDescription implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "actual_begin")
  private LocalDate actualBegin;

  @Column(name = "actual_end")
  private LocalDate actualEnd;

  @Column(name = "state")
  private String state;

  @OneToOne(mappedBy = "theEnactionDescription")
  @JsonIgnore
  private Plain thePlain;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getActualBegin() {
    return actualBegin;
  }

  public EnactionDescription actualBegin(LocalDate actualBegin) {
    this.actualBegin = actualBegin;
    return this;
  }

  public void setActualBegin(LocalDate actualBegin) {
    this.actualBegin = actualBegin;
  }

  public LocalDate getActualEnd() {
    return actualEnd;
  }

  public EnactionDescription actualEnd(LocalDate actualEnd) {
    this.actualEnd = actualEnd;
    return this;
  }

  public void setActualEnd(LocalDate actualEnd) {
    this.actualEnd = actualEnd;
  }

  public String getState() {
    return state;
  }

  public EnactionDescription state(String state) {
    this.state = state;
    return this;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Plain getThePlain() {
    return thePlain;
  }

  public EnactionDescription thePlain(Plain plain) {
    this.thePlain = plain;
    return this;
  }

  public void setThePlain(Plain plain) {
    this.thePlain = plain;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EnactionDescription)) {
      return false;
    }
    return id != null && id.equals(((EnactionDescription) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "EnactionDescription{"
        + "id="
        + getId()
        + ", actualBegin='"
        + getActualBegin()
        + "'"
        + ", actualEnd='"
        + getActualEnd()
        + "'"
        + ", state='"
        + getState()
        + "'"
        + "}";
  }
}
