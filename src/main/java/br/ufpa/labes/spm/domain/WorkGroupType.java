package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A WorkGroupType.
 */
@Entity
@Table(name = "work_group_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WorkGroupType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "theGroupType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WorkGroup> theWorkGroups = new HashSet<>();

    @OneToMany(mappedBy = "theWorkGroupType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReqWorkGroup> theReqGroups = new HashSet<>();

    @OneToMany(mappedBy = "groupTypeRequired")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WorkGroupInstSug> theWorkGroupInstSugs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<WorkGroup> getTheWorkGroups() {
        return theWorkGroups;
    }

    public WorkGroupType theWorkGroups(Set<WorkGroup> workGroups) {
        this.theWorkGroups = workGroups;
        return this;
    }

    public WorkGroupType addTheWorkGroup(WorkGroup workGroup) {
        this.theWorkGroups.add(workGroup);
        workGroup.setTheGroupType(this);
        return this;
    }

    public WorkGroupType removeTheWorkGroup(WorkGroup workGroup) {
        this.theWorkGroups.remove(workGroup);
        workGroup.setTheGroupType(null);
        return this;
    }

    public void setTheWorkGroups(Set<WorkGroup> workGroups) {
        this.theWorkGroups = workGroups;
    }

    public Set<ReqWorkGroup> getTheReqGroups() {
        return theReqGroups;
    }

    public WorkGroupType theReqGroups(Set<ReqWorkGroup> reqWorkGroups) {
        this.theReqGroups = reqWorkGroups;
        return this;
    }

    public WorkGroupType addTheReqGroup(ReqWorkGroup reqWorkGroup) {
        this.theReqGroups.add(reqWorkGroup);
        reqWorkGroup.setTheWorkGroupType(this);
        return this;
    }

    public WorkGroupType removeTheReqGroup(ReqWorkGroup reqWorkGroup) {
        this.theReqGroups.remove(reqWorkGroup);
        reqWorkGroup.setTheWorkGroupType(null);
        return this;
    }

    public void setTheReqGroups(Set<ReqWorkGroup> reqWorkGroups) {
        this.theReqGroups = reqWorkGroups;
    }

    public Set<WorkGroupInstSug> getTheWorkGroupInstSugs() {
        return theWorkGroupInstSugs;
    }

    public WorkGroupType theWorkGroupInstSugs(Set<WorkGroupInstSug> workGroupInstSugs) {
        this.theWorkGroupInstSugs = workGroupInstSugs;
        return this;
    }

    public WorkGroupType addTheWorkGroupInstSug(WorkGroupInstSug workGroupInstSug) {
        this.theWorkGroupInstSugs.add(workGroupInstSug);
        workGroupInstSug.setGroupTypeRequired(this);
        return this;
    }

    public WorkGroupType removeTheWorkGroupInstSug(WorkGroupInstSug workGroupInstSug) {
        this.theWorkGroupInstSugs.remove(workGroupInstSug);
        workGroupInstSug.setGroupTypeRequired(null);
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
        if (!(o instanceof WorkGroupType)) {
            return false;
        }
        return id != null && id.equals(((WorkGroupType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WorkGroupType{" +
            "id=" + getId() +
            "}";
    }
}
