package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A BranchConCond.
 */
@Entity
@Table(name = "branch_con_cond")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BranchConCond extends BranchCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kind_branch")
    private String kindBranch;

    @OneToMany(mappedBy = "theBranchConCond")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BranchConCondToActivity> theBranchConCondToActivities = new HashSet<>();

    @OneToMany(mappedBy = "theBranchConCond")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BranchConCondToMultipleCon> theBranchConCondToMultipleCons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKindBranch() {
        return kindBranch;
    }

    public BranchConCond kindBranch(String kindBranch) {
        this.kindBranch = kindBranch;
        return this;
    }

    public void setKindBranch(String kindBranch) {
        this.kindBranch = kindBranch;
    }

    public Set<BranchConCondToActivity> getTheBranchConCondToActivities() {
        return theBranchConCondToActivities;
    }

    public BranchConCond theBranchConCondToActivities(Set<BranchConCondToActivity> branchConCondToActivities) {
        this.theBranchConCondToActivities = branchConCondToActivities;
        return this;
    }

    public BranchConCond addTheBranchConCondToActivity(BranchConCondToActivity branchConCondToActivity) {
        this.theBranchConCondToActivities.add(branchConCondToActivity);
        branchConCondToActivity.setTheBranchConCond(this);
        return this;
    }

    public BranchConCond removeTheBranchConCondToActivity(BranchConCondToActivity branchConCondToActivity) {
        this.theBranchConCondToActivities.remove(branchConCondToActivity);
        branchConCondToActivity.setTheBranchConCond(null);
        return this;
    }

    public void setTheBranchConCondToActivities(Set<BranchConCondToActivity> branchConCondToActivities) {
        this.theBranchConCondToActivities = branchConCondToActivities;
    }

    public Set<BranchConCondToMultipleCon> getTheBranchConCondToMultipleCons() {
        return theBranchConCondToMultipleCons;
    }

    public BranchConCond theBranchConCondToMultipleCons(Set<BranchConCondToMultipleCon> branchConCondToMultipleCons) {
        this.theBranchConCondToMultipleCons = branchConCondToMultipleCons;
        return this;
    }

    public BranchConCond addTheBranchConCondToMultipleCon(BranchConCondToMultipleCon branchConCondToMultipleCon) {
        this.theBranchConCondToMultipleCons.add(branchConCondToMultipleCon);
        branchConCondToMultipleCon.setTheBranchConCond(this);
        return this;
    }

    public BranchConCond removeTheBranchConCondToMultipleCon(BranchConCondToMultipleCon branchConCondToMultipleCon) {
        this.theBranchConCondToMultipleCons.remove(branchConCondToMultipleCon);
        branchConCondToMultipleCon.setTheBranchConCond(null);
        return this;
    }

    public void setTheBranchConCondToMultipleCons(Set<BranchConCondToMultipleCon> branchConCondToMultipleCons) {
        this.theBranchConCondToMultipleCons = branchConCondToMultipleCons;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchConCond)) {
            return false;
        }
        return id != null && id.equals(((BranchConCond) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BranchConCond{" +
            "id=" + getId() +
            ", kindBranch='" + getKindBranch() + "'" +
            "}";
    }
}
