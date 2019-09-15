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
public class BranchConCond implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kind_branch")
    private String kindBranch;

    @OneToMany(mappedBy = "theBranchConCond")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BranchCondToActivity> theBranchCondToActivities = new HashSet<>();

    @OneToMany(mappedBy = "theBranchConCond")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BranchCondToMultipleCon> theBranchCondToMultipleCons = new HashSet<>();

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

    public Set<BranchCondToActivity> getTheBranchCondToActivities() {
        return theBranchCondToActivities;
    }

    public BranchConCond theBranchCondToActivities(Set<BranchCondToActivity> branchCondToActivities) {
        this.theBranchCondToActivities = branchCondToActivities;
        return this;
    }

    public BranchConCond addTheBranchCondToActivity(BranchCondToActivity branchCondToActivity) {
        this.theBranchCondToActivities.add(branchCondToActivity);
        branchCondToActivity.setTheBranchConCond(this);
        return this;
    }

    public BranchConCond removeTheBranchCondToActivity(BranchCondToActivity branchCondToActivity) {
        this.theBranchCondToActivities.remove(branchCondToActivity);
        branchCondToActivity.setTheBranchConCond(null);
        return this;
    }

    public void setTheBranchCondToActivities(Set<BranchCondToActivity> branchCondToActivities) {
        this.theBranchCondToActivities = branchCondToActivities;
    }

    public Set<BranchCondToMultipleCon> getTheBranchCondToMultipleCons() {
        return theBranchCondToMultipleCons;
    }

    public BranchConCond theBranchCondToMultipleCons(Set<BranchCondToMultipleCon> branchCondToMultipleCons) {
        this.theBranchCondToMultipleCons = branchCondToMultipleCons;
        return this;
    }

    public BranchConCond addTheBranchCondToMultipleCon(BranchCondToMultipleCon branchCondToMultipleCon) {
        this.theBranchCondToMultipleCons.add(branchCondToMultipleCon);
        branchCondToMultipleCon.setTheBranchConCond(this);
        return this;
    }

    public BranchConCond removeTheBranchCondToMultipleCon(BranchCondToMultipleCon branchCondToMultipleCon) {
        this.theBranchCondToMultipleCons.remove(branchCondToMultipleCon);
        branchCondToMultipleCon.setTheBranchConCond(null);
        return this;
    }

    public void setTheBranchCondToMultipleCons(Set<BranchCondToMultipleCon> branchCondToMultipleCons) {
        this.theBranchCondToMultipleCons = branchCondToMultipleCons;
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
