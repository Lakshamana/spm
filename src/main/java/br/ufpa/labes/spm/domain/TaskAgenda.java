package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A TaskAgenda. */
@Entity
@Table(name = "task_agenda")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TaskAgenda implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy = "theTaskAgenda")
  @JsonIgnore
  private Agent theAgent;

  @OneToMany(mappedBy = "theTaskAgenda")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ProcessAgenda> theProcessAgenda = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Agent getTheAgent() {
    return theAgent;
  }

  public TaskAgenda theAgent(Agent agent) {
    this.theAgent = agent;
    return this;
  }

  public void setTheAgent(Agent agent) {
    this.theAgent = agent;
  }

  public Set<ProcessAgenda> getTheProcessAgenda() {
    return theProcessAgenda;
  }

  public TaskAgenda theProcessAgenda(Set<ProcessAgenda> processAgenda) {
    this.theProcessAgenda = processAgenda;
    return this;
  }

  public TaskAgenda addTheProcessAgenda(ProcessAgenda processAgenda) {
    this.theProcessAgenda.add(processAgenda);
    processAgenda.setTheTaskAgenda(this);
    return this;
  }

  public TaskAgenda removeTheProcessAgenda(ProcessAgenda processAgenda) {
    this.theProcessAgenda.remove(processAgenda);
    processAgenda.setTheTaskAgenda(null);
    return this;
  }

  public void setTheProcessAgenda(Set<ProcessAgenda> processAgenda) {
    this.theProcessAgenda = processAgenda;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TaskAgenda)) {
      return false;
    }
    return id != null && id.equals(((TaskAgenda) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "TaskAgenda{" + "id=" + getId() + "}";
  }
}
