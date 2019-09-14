package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ResourceEstimation.
 */
@Entity
@Table(name = "resource_estimation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ResourceEstimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theResourceEstimations")
    private Resource theResource;

    @OneToOne(mappedBy = "theResourceEstimationSub")
    @JsonIgnore
    private Estimation theEstimationSuper;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getTheResource() {
        return theResource;
    }

    public ResourceEstimation theResource(Resource resource) {
        this.theResource = resource;
        return this;
    }

    public void setTheResource(Resource resource) {
        this.theResource = resource;
    }

    public Estimation getTheEstimationSuper() {
        return theEstimationSuper;
    }

    public ResourceEstimation theEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
        return this;
    }

    public void setTheEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceEstimation)) {
            return false;
        }
        return id != null && id.equals(((ResourceEstimation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ResourceEstimation{" +
            "id=" + getId() +
            "}";
    }
}
