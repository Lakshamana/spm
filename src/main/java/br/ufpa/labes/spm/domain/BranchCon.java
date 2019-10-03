package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

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
@Inheritance(strategy=InheritanceType.JOINED)
public class BranchCon extends MultipleCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("toBranchCons")
    private MultipleCon fromMultipleConnection;

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

    public MultipleCon getFromMultipleConnection() {
        return fromMultipleConnection;
    }

    public BranchCon fromMultipleConnection(MultipleCon multipleCon) {
        this.fromMultipleConnection = multipleCon;
        return this;
    }

    public void setFromMultipleConnection(MultipleCon multipleCon) {
        this.fromMultipleConnection = multipleCon;
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
