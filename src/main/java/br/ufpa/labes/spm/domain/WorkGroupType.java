package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(mappedBy = "theWorkGroupTypeSub")
    @JsonIgnore
    private Type theTypeSuper;

    @OneToMany(mappedBy = "theWorkGroupType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<WorkGroup> theWorkGroups = new HashSet<>();

    @OneToMany(mappedBy = "theWorkGroupType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReqWorkGroup> theReqWorkGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getTheTypeSuper() {
        return theTypeSuper;
    }

    public WorkGroupType theTypeSuper(Type type) {
        this.theTypeSuper = type;
        return this;
    }

    public void setTheTypeSuper(Type type) {
        this.theTypeSuper = type;
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
        workGroup.setTheWorkGroupType(this);
        return this;
    }

    public WorkGroupType removeTheWorkGroup(WorkGroup workGroup) {
        this.theWorkGroups.remove(workGroup);
        workGroup.setTheWorkGroupType(null);
        return this;
    }

    public void setTheWorkGroups(Set<WorkGroup> workGroups) {
        this.theWorkGroups = workGroups;
    }

    public Set<ReqWorkGroup> getTheReqWorkGroups() {
        return theReqWorkGroups;
    }

    public WorkGroupType theReqWorkGroups(Set<ReqWorkGroup> reqWorkGroups) {
        this.theReqWorkGroups = reqWorkGroups;
        return this;
    }

    public WorkGroupType addTheReqWorkGroup(ReqWorkGroup reqWorkGroup) {
        this.theReqWorkGroups.add(reqWorkGroup);
        reqWorkGroup.setTheWorkGroupType(this);
        return this;
    }

    public WorkGroupType removeTheReqWorkGroup(ReqWorkGroup reqWorkGroup) {
        this.theReqWorkGroups.remove(reqWorkGroup);
        reqWorkGroup.setTheWorkGroupType(null);
        return this;
    }

    public void setTheReqWorkGroups(Set<ReqWorkGroup> reqWorkGroups) {
        this.theReqWorkGroups = reqWorkGroups;
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
