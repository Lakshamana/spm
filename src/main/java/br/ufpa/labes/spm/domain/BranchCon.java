package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A BranchCon.
 */
@Entity
@Table(name = "branch_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BranchCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private BranchANDCon theBranchANDConSub;

    @ManyToOne
    @JsonIgnoreProperties("toBranchCons")
    private MultipleCon fromMultipleCon;

    @OneToOne(mappedBy = "theBranchConSub")
    @JsonIgnore
    private MultipleCon theMultipleConSuper;

    @ManyToOne
    @JsonIgnoreProperties("toBranchCons")
    private Activity fromActivity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BranchANDCon getTheBranchANDConSub() {
        return theBranchANDConSub;
    }

    public BranchCon theBranchANDConSub(BranchANDCon branchANDCon) {
        this.theBranchANDConSub = branchANDCon;
        return this;
    }

    public void setTheBranchANDConSub(BranchANDCon branchANDCon) {
        this.theBranchANDConSub = branchANDCon;
    }

    public MultipleCon getFromMultipleCon() {
        return fromMultipleCon;
    }

    public BranchCon fromMultipleCon(MultipleCon multipleCon) {
        this.fromMultipleCon = multipleCon;
        return this;
    }

    public void setFromMultipleCon(MultipleCon multipleCon) {
        this.fromMultipleCon = multipleCon;
    }

    public MultipleCon getTheMultipleConSuper() {
        return theMultipleConSuper;
    }

    public BranchCon theMultipleConSuper(MultipleCon multipleCon) {
        this.theMultipleConSuper = multipleCon;
        return this;
    }

    public void setTheMultipleConSuper(MultipleCon multipleCon) {
        this.theMultipleConSuper = multipleCon;
    }

    public Activity getFromActivity() {
        return fromActivity;
    }

    public BranchCon fromActivity(Activity activity) {
        this.fromActivity = activity;
        return this;
    }

    public void setFromActivity(Activity activity) {
        this.fromActivity = activity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchCon)) {
            return false;
        }
        return id != null && id.equals(((BranchCon) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BranchCon{" +
            "id=" + getId() +
            "}";
    }
}
