package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ReqWorkGroup.
 */
@Entity
@Table(name = "req_work_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReqWorkGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theReqWorkGroups")
    private WorkGroupType theWorkGroupType;

    @OneToOne(mappedBy = "theReqWorkGroupSub")
    @JsonIgnore
    private RequiredPeople theRequiredPeopleSuper;

    @ManyToOne
    @JsonIgnoreProperties("theReqWorkGroups")
    private WorkGroup theWorkGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkGroupType getTheWorkGroupType() {
        return theWorkGroupType;
    }

    public ReqWorkGroup theWorkGroupType(WorkGroupType workGroupType) {
        this.theWorkGroupType = workGroupType;
        return this;
    }

    public void setTheWorkGroupType(WorkGroupType workGroupType) {
        this.theWorkGroupType = workGroupType;
    }

    public RequiredPeople getTheRequiredPeopleSuper() {
        return theRequiredPeopleSuper;
    }

    public ReqWorkGroup theRequiredPeopleSuper(RequiredPeople requiredPeople) {
        this.theRequiredPeopleSuper = requiredPeople;
        return this;
    }

    public void setTheRequiredPeopleSuper(RequiredPeople requiredPeople) {
        this.theRequiredPeopleSuper = requiredPeople;
    }

    public WorkGroup getTheWorkGroup() {
        return theWorkGroup;
    }

    public ReqWorkGroup theWorkGroup(WorkGroup workGroup) {
        this.theWorkGroup = workGroup;
        return this;
    }

    public void setTheWorkGroup(WorkGroup workGroup) {
        this.theWorkGroup = workGroup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReqWorkGroup)) {
            return false;
        }
        return id != null && id.equals(((ReqWorkGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ReqWorkGroup{" +
            "id=" + getId() +
            "}";
    }
}
