package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A ProcessAgenda. */
@Entity
@Table(name = "process_agenda")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcessAgenda implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "theProcessAgenda")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Task> theTasks = new HashSet<>();

  @ManyToOne
  @JsonIgnoreProperties("theProcessAgenda")
  private TaskAgenda theTaskAgenda;

  @ManyToOne
  @JsonIgnoreProperties("theProcessAgenda")
  private Process theProcess;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<Task> getTheTasks() {
    return theTasks;
  }

  public ProcessAgenda theTasks(Set<Task> tasks) {
    this.theTasks = tasks;
    return this;
  }

  public ProcessAgenda addTheTask(Task task) {
    this.theTasks.add(task);
    task.setTheProcessAgenda(this);
    return this;
  }

  public ProcessAgenda removeTheTask(Task task) {
    this.theTasks.remove(task);
    task.setTheProcessAgenda(null);
    return this;
  }

  public void setTheTasks(Set<Task> tasks) {
    this.theTasks = tasks;
  }

  public TaskAgenda getTheTaskAgenda() {
    return theTaskAgenda;
  }

  public ProcessAgenda theTaskAgenda(TaskAgenda taskAgenda) {
    this.theTaskAgenda = taskAgenda;
    return this;
  }

  public void setTheTaskAgenda(TaskAgenda taskAgenda) {
    this.theTaskAgenda = taskAgenda;
  }

  public Process getTheProcess() {
    return theProcess;
  }

  public ProcessAgenda theProcess(Process process) {
    this.theProcess = process;
    return this;
  }

  public void setTheProcess(Process process) {
    this.theProcess = process;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProcessAgenda)) {
      return false;
    }
    return id != null && id.equals(((ProcessAgenda) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ProcessAgenda{" + "id=" + getId() + "}";
  }
}
