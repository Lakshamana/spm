package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A BranchConCondToMultipleCon.
 */
@Entity
@Table(name = "branch_cond_to_multiple_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BranchConCondToMultipleCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theBranchConCondToMultipleCons")
    private MultipleCon theMultipleCon;

    @ManyToOne
    @JsonIgnoreProperties("theBranchConCondToMultipleCons")
    private BranchConCond theBranchConCond;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MultipleCon getTheMultipleCon() {
        return theMultipleCon;
    }

    public BranchConCondToMultipleCon theMultipleCon(MultipleCon multipleCon) {
        this.theMultipleCon = multipleCon;
        return this;
    }

    public void setTheMultipleCon(MultipleCon multipleCon) {
        this.theMultipleCon = multipleCon;
    }

    public BranchConCond getTheBranchConCond() {
        return theBranchConCond;
    }

    public BranchConCondToMultipleCon theBranchConCond(BranchConCond branchConCond) {
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
        if (!(o instanceof BranchConCondToMultipleCon)) {
            return false;
        }
        return id != null && id.equals(((BranchConCondToMultipleCon) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BranchConCondToMultipleCon{" +
            "id=" + getId() +
            "}";
    }
}
