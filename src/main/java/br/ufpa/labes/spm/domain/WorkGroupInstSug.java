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
 * A WorkGroupInstSug.
 */
@Entity
@Table(name = "work_group_inst_sug")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WorkGroupInstSug implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("sugToChosenWorkGroups")
    private WorkGroup chosenWorkGroup;

    @ManyToOne
    @JsonIgnoreProperties("sugToReqWorkGroups")
    private Type workGroupTypeRequired;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "work_group_inst_sug_sug_work_group",
               joinColumns = @JoinColumn(name = "work_group_inst_sug_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "sug_work_group_id", referencedColumnName = "id"))
    private Set<WorkGroup> sugWorkGroups = new HashSet<>();

    @OneToOne(mappedBy = "theWorkGroupInstSug")
    @JsonIgnore
    private PeopleInstSug thePeopleInstSugSuper;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkGroup getChosenWorkGroup() {
        return chosenWorkGroup;
    }

    public WorkGroupInstSug chosenWorkGroup(WorkGroup workGroup) {
        this.chosenWorkGroup = workGroup;
        return this;
    }

    public void setChosenWorkGroup(WorkGroup workGroup) {
        this.chosenWorkGroup = workGroup;
    }

    public Type getWorkGroupTypeRequired() {
        return workGroupTypeRequired;
    }

    public WorkGroupInstSug workGroupTypeRequired(Type type) {
        this.workGroupTypeRequired = type;
        return this;
    }

    public void setWorkGroupTypeRequired(Type type) {
        this.workGroupTypeRequired = type;
    }

    public Set<WorkGroup> getSugWorkGroups() {
        return sugWorkGroups;
    }

    public WorkGroupInstSug sugWorkGroups(Set<WorkGroup> workGroups) {
        this.sugWorkGroups = workGroups;
        return this;
    }

    public WorkGroupInstSug addSugWorkGroup(WorkGroup workGroup) {
        this.sugWorkGroups.add(workGroup);
        workGroup.getTheWorkGroupInstSugs().add(this);
        return this;
    }

    public WorkGroupInstSug removeSugWorkGroup(WorkGroup workGroup) {
        this.sugWorkGroups.remove(workGroup);
        workGroup.getTheWorkGroupInstSugs().remove(this);
        return this;
    }

    public void setSugWorkGroups(Set<WorkGroup> workGroups) {
        this.sugWorkGroups = workGroups;
    }

    public PeopleInstSug getThePeopleInstSugSuper() {
        return thePeopleInstSugSuper;
    }

    public WorkGroupInstSug thePeopleInstSugSuper(PeopleInstSug peopleInstSug) {
        this.thePeopleInstSugSuper = peopleInstSug;
        return this;
    }

    public void setThePeopleInstSugSuper(PeopleInstSug peopleInstSug) {
        this.thePeopleInstSugSuper = peopleInstSug;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WorkGroupInstSug)) {
            return false;
        }
        return id != null && id.equals(((WorkGroupInstSug) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WorkGroupInstSug{" +
            "id=" + getId() +
            "}";
    }
}
