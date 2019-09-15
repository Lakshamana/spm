package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A BranchCondToActivity.
 */
@Entity
@Table(name = "branch_cond_to_activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BranchCondToActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theBranchCondToActivities")
    private Activity theActivity;

    @ManyToOne
    @JsonIgnoreProperties("theBranchCondToActivities")
    private BranchConCond theBranchConCond;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getTheActivity() {
        return theActivity;
    }

    public BranchCondToActivity theActivity(Activity activity) {
        this.theActivity = activity;
        return this;
    }

    public void setTheActivity(Activity activity) {
        this.theActivity = activity;
    }

    public BranchConCond getTheBranchConCond() {
        return theBranchConCond;
    }

    public BranchCondToActivity theBranchConCond(BranchConCond branchConCond) {
        this.theBranchConCond = branchConCond;
        return this;
    }

    public void setTheBranchConCond(BranchConCond branchConCond) {
        this.theBranchConCond = branchConCond;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchCondToActivity)) {
            return false;
        }
        return id != null && id.equals(((BranchCondToActivity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BranchCondToActivity{" +
            "id=" + getId() +
            "}";
    }
}
