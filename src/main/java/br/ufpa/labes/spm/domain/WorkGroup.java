package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A WorkGroup.
 */
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
    private Set<ReqWorkGroup> theReqGroups = new HashSet<>();

    @OneToMany(mappedBy = "workGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WorkGroupMetric> theWorkGroupMetrics = new HashSet<>();

    @OneToMany(mappedBy = "workGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WorkGroupEstimation> theWorkGroupEstimations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("theWorkGroups")
    private WorkGroupType theGroupType;

    @ManyToOne
    @JsonIgnoreProperties("subGroups")
    private WorkGroup superGroup;

    @OneToMany(mappedBy = "superGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WorkGroup> subGroups = new HashSet<>();

    @OneToMany(mappedBy = "groupChosen")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WorkGroupInstSug> theWorkGroupInstSugs = new HashSet<>();

    @ManyToMany(mappedBy = "theWorkGroups")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Agent> theAgents = new HashSet<>();

    @ManyToMany(mappedBy = "sugWorkGroups")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<WorkGroupInstSug> theWorkGroupInstSugs = new HashSet<>();

    @ManyToMany(mappedBy = "groupSuggesteds")
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

    public Boolean isIsActive() {
        return isActive;
    }

    public WorkGroup isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<ReqWorkGroup> getTheReqGroups() {
        return theReqGroups;
    }

    public WorkGroup theReqGroups(Set<ReqWorkGroup> reqWorkGroups) {
        this.theReqGroups = reqWorkGroups;
        return this;
    }

    public WorkGroup addTheReqGroup(ReqWorkGroup reqWorkGroup) {
        this.theReqGroups.add(reqWorkGroup);
        reqWorkGroup.setTheWorkGroup(this);
        return this;
    }

    public WorkGroup removeTheReqGroup(ReqWorkGroup reqWorkGroup) {
        this.theReqGroups.remove(reqWorkGroup);
        reqWorkGroup.setTheWorkGroup(null);
        return this;
    }

    public void setTheReqGroups(Set<ReqWorkGroup> reqWorkGroups) {
        this.theReqGroups = reqWorkGroups;
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
        workGroupMetric.setWorkGroup(this);
        return this;
    }

    public WorkGroup removeTheWorkGroupMetric(WorkGroupMetric workGroupMetric) {
        this.theWorkGroupMetrics.remove(workGroupMetric);
        workGroupMetric.setWorkGroup(null);
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
        workGroupEstimation.setWorkGroup(this);
        return this;
    }

    public WorkGroup removeTheWorkGroupEstimation(WorkGroupEstimation workGroupEstimation) {
        this.theWorkGroupEstimations.remove(workGroupEstimation);
        workGroupEstimation.setWorkGroup(null);
        return this;
    }

    public void setTheWorkGroupEstimations(Set<WorkGroupEstimation> workGroupEstimations) {
        this.theWorkGroupEstimations = workGroupEstimations;
    }

    public WorkGroupType getTheGroupType() {
        return theGroupType;
    }

    public WorkGroup theGroupType(WorkGroupType workGroupType) {
        this.theGroupType = workGroupType;
        return this;
    }

    public void setTheGroupType(WorkGroupType workGroupType) {
        this.theGroupType = workGroupType;
    }

    public WorkGroup getSuperGroup() {
        return superGroup;
    }

    public WorkGroup superGroup(WorkGroup workGroup) {
        this.superGroup = workGroup;
        return this;
    }

    public void setSuperGroup(WorkGroup workGroup) {
        this.superGroup = workGroup;
    }

    public Set<WorkGroup> getSubGroups() {
        return subGroups;
    }

    public WorkGroup subGroups(Set<WorkGroup> workGroups) {
        this.subGroups = workGroups;
        return this;
    }

    public WorkGroup addSubGroups(WorkGroup workGroup) {
        this.subGroups.add(workGroup);
        workGroup.setSuperGroup(this);
        return this;
    }

    public WorkGroup removeSubGroups(WorkGroup workGroup) {
        this.subGroups.remove(workGroup);
        workGroup.setSuperGroup(null);
        return this;
    }

    public void setSubGroups(Set<WorkGroup> workGroups) {
        this.subGroups = workGroups;
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
        workGroupInstSug.setGroupChosen(this);
        return this;
    }

    public WorkGroup removeTheWorkGroupInstSug(WorkGroupInstSug workGroupInstSug) {
        this.theWorkGroupInstSugs.remove(workGroupInstSug);
        workGroupInstSug.setGroupChosen(null);
        return this;
    }

    public void setTheWorkGroupInstSugs(Set<WorkGroupInstSug> workGroupInstSugs) {
        this.theWorkGroupInstSugs = workGroupInstSugs;
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

    public Set<WorkGroupInstSug> getTheWorkGroupInstSugs() {
        return theWorkGroupInstSugs;
    }

    public WorkGroup theWorkGroupInstSugs(Set<WorkGroupInstSug> workGroupInstSugs) {
        this.theWorkGroupInstSugs = workGroupInstSugs;
        return this;
    }

    public WorkGroup addTheWorkGroupInstSug(WorkGroupInstSug workGroupInstSug) {
        this.theWorkGroupInstSugs.add(workGroupInstSug);
        workGroupInstSug.getGroupSuggesteds().add(this);
        return this;
    }

    public WorkGroup removeTheWorkGroupInstSug(WorkGroupInstSug workGroupInstSug) {
        this.theWorkGroupInstSugs.remove(workGroupInstSug);
        workGroupInstSug.getGroupSuggesteds().remove(this);
        return this;
    }

    public void setTheWorkGroupInstSugs(Set<WorkGroupInstSug> workGroupInstSugs) {
        this.theWorkGroupInstSugs = workGroupInstSugs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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
        return "WorkGroup{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
