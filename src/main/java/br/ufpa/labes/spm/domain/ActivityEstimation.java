package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ActivityEstimation.
 */
@Entity
@Table(name = "activity_estimation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityEstimation extends Estimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "theActivityEstimationSub")
    @JsonIgnore
    private Estimation theEstimationSuper;

    @ManyToOne
    @JsonIgnoreProperties("theActivityEstimations")
    private Activity theActivity;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estimation getTheEstimationSuper() {
        return theEstimationSuper;
    }

    public ActivityEstimation theEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
        return this;
    }

    public void setTheEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
    }

    public Activity getTheActivity() {
        return theActivity;
    }

    public ActivityEstimation theActivity(Activity activity) {
        this.theActivity = activity;
        return this;
    }

    public void setTheActivity(Activity activity) {
        this.theActivity = activity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityEstimation)) {
            return false;
        }
        return id != null && id.equals(((ActivityEstimation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ActivityEstimation{" +
            "id=" + getId() +
            "}";
    }
}
