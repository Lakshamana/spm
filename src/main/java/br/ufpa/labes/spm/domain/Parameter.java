package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Parameter.
 */
@Entity
@Table(name = "parameter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Parameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private ArtifactParam theArtifactParameterSub;

    @OneToOne
    @JoinColumn(unique = true)
    private PrimitiveParam thePrimitiveParamSub;

    @ManyToOne
    @JsonIgnoreProperties("theParameters")
    private AutomaticActivity theAutomatic;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Parameter description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArtifactParam getTheArtifactParameterSub() {
        return theArtifactParameterSub;
    }

    public Parameter theArtifactParameterSub(ArtifactParam artifactParam) {
        this.theArtifactParameterSub = artifactParam;
        return this;
    }

    public void setTheArtifactParameterSub(ArtifactParam artifactParam) {
        this.theArtifactParameterSub = artifactParam;
    }

    public PrimitiveParam getThePrimitiveParamSub() {
        return thePrimitiveParamSub;
    }

    public Parameter thePrimitiveParamSub(PrimitiveParam primitiveParam) {
        this.thePrimitiveParamSub = primitiveParam;
        return this;
    }

    public void setThePrimitiveParamSub(PrimitiveParam primitiveParam) {
        this.thePrimitiveParamSub = primitiveParam;
    }

    public AutomaticActivity getTheAutomatic() {
        return theAutomatic;
    }

    public Parameter theAutomatic(AutomaticActivity automaticActivity) {
        this.theAutomatic = automaticActivity;
        return this;
    }

    public void setTheAutomatic(AutomaticActivity automaticActivity) {
        this.theAutomatic = automaticActivity;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parameter)) {
            return false;
        }
        return id != null && id.equals(((Parameter) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Parameter{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
