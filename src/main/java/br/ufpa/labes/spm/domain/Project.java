package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/** A Project. */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ident")
  private String ident;

  @Column(name = "name")
  private String name;

  @Lob
  @Column(name = "description")
  private String description;

  @Column(name = "begin_date")
  private LocalDate beginDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @OneToMany(mappedBy = "theProject")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Artifact> finalArtifacts = new HashSet<>();

  @ManyToOne
  @JsonIgnoreProperties("theProjects")
  private Process processRefered;

  @ManyToOne
  @JsonIgnoreProperties("theProjects")
  private DevelopingSystem theSystem;

  @ManyToOne
  @JsonIgnoreProperties("projects")
  private Calendar theCalendar;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdent() {
    return ident;
  }

  public Project ident(String ident) {
    this.ident = ident;
    return this;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getName() {
    return name;
  }

  public Project name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public Project description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getBeginDate() {
    return beginDate;
  }

  public Project beginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
    return this;
  }

  public void setBeginDate(LocalDate beginDate) {
    this.beginDate = beginDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public Project endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Set<Artifact> getFinalArtifacts() {
    return finalArtifacts;
  }

  public Project finalArtifacts(Set<Artifact> artifacts) {
    this.finalArtifacts = artifacts;
    return this;
  }

  public Project addFinalArtifacts(Artifact artifact) {
    this.finalArtifacts.add(artifact);
    artifact.setTheProject(this);
    return this;
  }

  public Project removeFinalArtifacts(Artifact artifact) {
    this.finalArtifacts.remove(artifact);
    artifact.setTheProject(null);
    return this;
  }

  public void setFinalArtifacts(Set<Artifact> artifacts) {
    this.finalArtifacts = artifacts;
  }

  public Process getProcessRefered() {
    return processRefered;
  }

  public Project processRefered(Process process) {
    this.processRefered = process;
    return this;
  }

  public void setProcessRefered(Process process) {
    this.processRefered = process;
  }

  public DevelopingSystem getTheSystem() {
    return theSystem;
  }

  public Project theSystem(DevelopingSystem developingSystem) {
    this.theSystem = developingSystem;
    return this;
  }

  public void setTheSystem(DevelopingSystem developingSystem) {
    this.theSystem = developingSystem;
  }

  public Calendar getTheCalendar() {
    return theCalendar;
  }

  public Project theCalendar(Calendar calendar) {
    this.theCalendar = calendar;
    return this;
  }

  public void setTheCalendar(Calendar calendar) {
    this.theCalendar = calendar;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Project)) {
      return false;
    }
    return id != null && id.equals(((Project) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Project{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", name='"
        + getName()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + ", beginDate='"
        + getBeginDate()
        + "'"
        + ", endDate='"
        + getEndDate()
        + "'"
        + "}";
  }
}
