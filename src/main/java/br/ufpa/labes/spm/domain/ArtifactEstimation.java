package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ArtifactEstimation.
 */
@Entity
@Table(name = "artifact_estimation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArtifactEstimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "theArtifactEstimationSub")
    @JsonIgnore
    private Estimation theEstimationSuper;

    @ManyToOne
    @JsonIgnoreProperties("theArtifactEstimations")
    private Artifact theArtifact;

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

    public ArtifactEstimation theEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
        return this;
    }

    public void setTheEstimationSuper(Estimation estimation) {
        this.theEstimationSuper = estimation;
    }

    public Artifact getTheArtifact() {
        return theArtifact;
    }

    public ArtifactEstimation theArtifact(Artifact artifact) {
        this.theArtifact = artifact;
        return this;
    }

    public void setTheArtifact(Artifact artifact) {
        this.theArtifact = artifact;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtifactEstimation)) {
            return false;
        }
        return id != null && id.equals(((ArtifactEstimation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ArtifactEstimation{" +
            "id=" + getId() +
            "}";
    }
}
