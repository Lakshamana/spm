package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A BranchANDCon.
 */
@Entity
@Table(name = "branch_and_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BranchANDCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "branch_and_con_to_multiple_con",
               joinColumns = @JoinColumn(name = "branchandcon_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "to_multiple_con_id", referencedColumnName = "id"))
    private Set<MultipleCon> toMultipleCons = new HashSet<>();

    @OneToOne(mappedBy = "theBranchANDConSub")
    @JsonIgnore
    private BranchCon theBranchConSuper;

    @ManyToMany(mappedBy = "toBranchANDCons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Activity> fromActivities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<MultipleCon> getToMultipleCons() {
        return toMultipleCons;
    }

    public BranchANDCon toMultipleCons(Set<MultipleCon> multipleCons) {
        this.toMultipleCons = multipleCons;
        return this;
    }

    public BranchANDCon addToMultipleCon(MultipleCon multipleCon) {
        this.toMultipleCons.add(multipleCon);
        multipleCon.getFromMultipleCons().add(this);
        return this;
    }

    public BranchANDCon removeToMultipleCon(MultipleCon multipleCon) {
        this.toMultipleCons.remove(multipleCon);
        multipleCon.getFromMultipleCons().remove(this);
        return this;
    }

    public void setToMultipleCons(Set<MultipleCon> multipleCons) {
        this.toMultipleCons = multipleCons;
    }

    public BranchCon getTheBranchConSuper() {
        return theBranchConSuper;
    }

    public BranchANDCon theBranchConSuper(BranchCon branchCon) {
        this.theBranchConSuper = branchCon;
        return this;
    }

    public void setTheBranchConSuper(BranchCon branchCon) {
        this.theBranchConSuper = branchCon;
    }

    public Set<Activity> getFromActivities() {
        return fromActivities;
    }

    public BranchANDCon fromActivities(Set<Activity> activities) {
        this.fromActivities = activities;
        return this;
    }

    public BranchANDCon addFromActivity(Activity activity) {
        this.fromActivities.add(activity);
        activity.getToBranchANDCons().add(this);
        return this;
    }

    public BranchANDCon removeFromActivity(Activity activity) {
        this.fromActivities.remove(activity);
        activity.getToBranchANDCons().remove(this);
        return this;
    }

    public void setFromActivities(Set<Activity> activities) {
        this.fromActivities = activities;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchANDCon)) {
            return false;
        }
        return id != null && id.equals(((BranchANDCon) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BranchANDCon{" +
            "id=" + getId() +
            "}";
    }
}
