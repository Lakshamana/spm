package br.ufpa.labes.spm.domain;
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
public class WorkGroupInstSug extends PeopleInstSug implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theWorkGroupInstSugs")
    private WorkGroup groupChosen;

    @ManyToOne
    @JsonIgnoreProperties("theWorkGroupInstSugs")
    private WorkGroupType groupTypeRequired;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "work_group_inst_sug_group_suggested",
               joinColumns = @JoinColumn(name = "work_group_inst_sug_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "group_suggested_id", referencedColumnName = "id"))
    private Set<WorkGroup> groupSuggesteds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkGroup getGroupChosen() {
        return groupChosen;
    }

    public WorkGroupInstSug groupChosen(WorkGroup workGroup) {
        this.groupChosen = workGroup;
        return this;
    }

    public void setGroupChosen(WorkGroup workGroup) {
        this.groupChosen = workGroup;
    }

    public WorkGroupType getGroupTypeRequired() {
        return groupTypeRequired;
    }

    public WorkGroupInstSug groupTypeRequired(WorkGroupType workGroupType) {
        this.groupTypeRequired = workGroupType;
        return this;
    }

    public void setGroupTypeRequired(WorkGroupType workGroupType) {
        this.groupTypeRequired = workGroupType;
    }

    public Set<WorkGroup> getGroupSuggesteds() {
        return groupSuggesteds;
    }

    public WorkGroupInstSug groupSuggesteds(Set<WorkGroup> workGroups) {
        this.groupSuggesteds = workGroups;
        return this;
    }

    public WorkGroupInstSug addGroupSuggested(WorkGroup workGroup) {
        this.groupSuggesteds.add(workGroup);
        workGroup.getTheSuggestedGroups().add(this);
        return this;
    }

    public WorkGroupInstSug removeGroupSuggested(WorkGroup workGroup) {
        this.groupSuggesteds.remove(workGroup);
        workGroup.getTheSuggestedGroups().remove(this);
        return this;
    }

    public void setGroupSuggesteds(Set<WorkGroup> workGroups) {
        this.groupSuggesteds = workGroups;
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
