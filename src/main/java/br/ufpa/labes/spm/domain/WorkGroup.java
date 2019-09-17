package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A WorkGroup. */
@Entity
@Table(name = "work_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WorkGroup implements Serializable {

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

  @Column(name = "is_active")
  private Boolean isActive;

  @OneToMany(mappedBy = "theWorkGroup")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ReqWorkGroup> theReqWorkGroups = new HashSet<>();

  @OneToMany(mappedBy = "theWorkGroup")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<WorkGroupMetric> theWorkGroupMetrics = new HashSet<>();

  @OneToMany(mappedBy = "theWorkGroup")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<WorkGroupEstimation> theWorkGroupEstimations = new HashSet<>();

  @ManyToOne
  @JsonIgnoreProperties("theWorkGroups")
  private WorkGroupType theWorkGroupType;

  @ManyToOne
  @JsonIgnoreProperties("subWorkGroups")
  private WorkGroup superWorkGroup;

  @OneToMany(mappedBy = "superWorkGroup")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<WorkGroup> subWorkGroups = new HashSet<>();

  @OneToMany(mappedBy = "chosenWorkGroup")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<WorkGroupInstSug> sugToChosenWorkGroups = new HashSet<>();

  @ManyToMany(mappedBy = "theWorkGroups")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<Agent> theAgents = new HashSet<>();

  @ManyToMany(mappedBy = "sugWorkGroups")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<WorkGroupInstSug> theWorkGroupInstSugs = new HashSet<>();

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

  public WorkGroup ident(String ident) {
    this.ident = ident;
    return this;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getName() {
    return name;
  }

  public WorkGroup name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public WorkGroup description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean isActive() {
    return isActive;
  }

  public WorkGroup isActive(Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Set<ReqWorkGroup> getTheReqWorkGroups() {
    return theReqWorkGroups;
  }

  public WorkGroup theReqWorkGroups(Set<ReqWorkGroup> reqWorkGroups) {
    this.theReqWorkGroups = reqWorkGroups;
    return this;
  }

  public WorkGroup addTheReqWorkGroup(ReqWorkGroup reqWorkGroup) {
    this.theReqWorkGroups.add(reqWorkGroup);
    reqWorkGroup.setTheWorkGroup(this);
    return this;
  }

  public WorkGroup removeTheReqWorkGroup(ReqWorkGroup reqWorkGroup) {
    this.theReqWorkGroups.remove(reqWorkGroup);
    reqWorkGroup.setTheWorkGroup(null);
    return this;
  }

  public void setTheReqWorkGroups(Set<ReqWorkGroup> reqWorkGroups) {
    this.theReqWorkGroups = reqWorkGroups;
  }

  public Set<WorkGroupMetric> getTheWorkGroupMetrics() {
    return theWorkGroupMetrics;
  }

  public WorkGroup theWorkGroupMetrics(Set<WorkGroupMetric> workGroupMetrics) {
    this.theWorkGroupMetrics = workGroupMetrics;
    return this;
  }

  public WorkGroup addTheWorkGroupMetric(WorkGroupMetric workGroupMetric) {
    this.theWorkGroupMetrics.add(workGroupMetric);
    workGroupMetric.setTheWorkGroup(this);
    return this;
  }

  public WorkGroup removeTheWorkGroupMetric(WorkGroupMetric workGroupMetric) {
    this.theWorkGroupMetrics.remove(workGroupMetric);
    workGroupMetric.setTheWorkGroup(null);
    return this;
  }

  public void setTheWorkGroupMetrics(Set<WorkGroupMetric> workGroupMetrics) {
    this.theWorkGroupMetrics = workGroupMetrics;
  }

  public Set<WorkGroupEstimation> getTheWorkGroupEstimations() {
    return theWorkGroupEstimations;
  }

  public WorkGroup theWorkGroupEstimations(Set<WorkGroupEstimation> workGroupEstimations) {
    this.theWorkGroupEstimations = workGroupEstimations;
    return this;
  }

  public WorkGroup addTheWorkGroupEstimation(WorkGroupEstimation workGroupEstimation) {
    this.theWorkGroupEstimations.add(workGroupEstimation);
    workGroupEstimation.setTheWorkGroup(this);
    return this;
  }

  public WorkGroup removeTheWorkGroupEstimation(WorkGroupEstimation workGroupEstimation) {
    this.theWorkGroupEstimations.remove(workGroupEstimation);
    workGroupEstimation.setTheWorkGroup(null);
    return this;
  }

  public void setTheWorkGroupEstimations(Set<WorkGroupEstimation> workGroupEstimations) {
    this.theWorkGroupEstimations = workGroupEstimations;
  }

  public WorkGroupType getTheWorkGroupType() {
    return theWorkGroupType;
  }

  public WorkGroup theWorkGroupType(WorkGroupType workGroupType) {
    this.theWorkGroupType = workGroupType;
    return this;
  }

  public void setTheWorkGroupType(WorkGroupType workGroupType) {
    this.theWorkGroupType = workGroupType;
  }

  public WorkGroup getSuperWorkGroup() {
    return superWorkGroup;
  }

  public WorkGroup superWorkGroup(WorkGroup workGroup) {
    this.superWorkGroup = workGroup;
    return this;
  }

  public void setSuperWorkGroup(WorkGroup workGroup) {
    this.superWorkGroup = workGroup;
  }

  public Set<WorkGroup> getSubWorkGroups() {
    return subWorkGroups;
  }

  public WorkGroup subWorkGroups(Set<WorkGroup> workGroups) {
    this.subWorkGroups = workGroups;
    return this;
  }

  public WorkGroup addSubWorkGroups(WorkGroup workGroup) {
    this.subWorkGroups.add(workGroup);
    workGroup.setSuperWorkGroup(this);
    return this;
  }

  public WorkGroup removeSubWorkGroups(WorkGroup workGroup) {
    this.subWorkGroups.remove(workGroup);
    workGroup.setSuperWorkGroup(null);
    return this;
  }

  public void setSubWorkGroups(Set<WorkGroup> workGroups) {
    this.subWorkGroups = workGroups;
  }

  public Set<WorkGroupInstSug> getSugToChosenWorkGroups() {
    return sugToChosenWorkGroups;
  }

  public WorkGroup sugToChosenWorkGroups(Set<WorkGroupInstSug> workGroupInstSugs) {
    this.sugToChosenWorkGroups = workGroupInstSugs;
    return this;
  }

  public WorkGroup addSugToChosenWorkGroup(WorkGroupInstSug workGroupInstSug) {
    this.sugToChosenWorkGroups.add(workGroupInstSug);
    workGroupInstSug.setChosenWorkGroup(this);
    return this;
  }

  public WorkGroup removeSugToChosenWorkGroup(WorkGroupInstSug workGroupInstSug) {
    this.sugToChosenWorkGroups.remove(workGroupInstSug);
    workGroupInstSug.setChosenWorkGroup(null);
    return this;
  }

  public void setSugToChosenWorkGroups(Set<WorkGroupInstSug> workGroupInstSugs) {
    this.sugToChosenWorkGroups = workGroupInstSugs;
  }

  public Set<Agent> getTheAgents() {
    return theAgents;
  }

  public WorkGroup theAgents(Set<Agent> agents) {
    this.theAgents = agents;
    return this;
  }

  public WorkGroup addTheAgent(Agent agent) {
    this.theAgents.add(agent);
    agent.getTheWorkGroups().add(this);
    return this;
  }

  public WorkGroup removeTheAgent(Agent agent) {
    this.theAgents.remove(agent);
    agent.getTheWorkGroups().remove(this);
    return this;
  }

  public void setTheAgents(Set<Agent> agents) {
    this.theAgents = agents;
  }

  public Set<WorkGroupInstSug> getTheWorkGroupInstSugs() {
    return theWorkGroupInstSugs;
  }

  public WorkGroup theWorkGroupInstSugs(Set<WorkGroupInstSug> workGroupInstSugs) {
    this.theWorkGroupInstSugs = workGroupInstSugs;
    return this;
  }

  public WorkGroup addTheWorkGroupInstSug(WorkGroupInstSug workGroupInstSug) {
    this.theWorkGroupInstSugs.add(workGroupInstSug);
    workGroupInstSug.getSugWorkGroups().add(this);
    return this;
  }

  public WorkGroup removeTheWorkGroupInstSug(WorkGroupInstSug workGroupInstSug) {
    this.theWorkGroupInstSugs.remove(workGroupInstSug);
    workGroupInstSug.getSugWorkGroups().remove(this);
    return this;
  }

  public void setTheWorkGroupInstSugs(Set<WorkGroupInstSug> workGroupInstSugs) {
    this.theWorkGroupInstSugs = workGroupInstSugs;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof WorkGroup)) {
      return false;
    }
    return id != null && id.equals(((WorkGroup) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "WorkGroup{"
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
        + ", isActive='"
        + isActive()
        + "'"
        + "}";
  }
}
