package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Subroutine.
 */
@Entity
@Table(name = "subroutine")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.JOINED)
public class Subroutine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theSubroutines")
    private ArtifactType theArtifactType;

    @OneToOne(mappedBy = "theSubroutine")
    @JsonIgnore
    private Automatic theAutomatic;

    @OneToMany(mappedBy = "theSubroutine")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ToolParameter> theToolParameters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArtifactType getTheArtifactType() {
        return theArtifactType;
    }

    public Subroutine theArtifactType(ArtifactType artifactType) {
        this.theArtifactType = artifactType;
        return this;
    }

    public void setTheArtifactType(ArtifactType artifactType) {
        this.theArtifactType = artifactType;
    }

    public Automatic getTheAutomatic() {
        return theAutomatic;
    }

    public Subroutine theAutomatic(Automatic automatic) {
        this.theAutomatic = automatic;
        return this;
    }

    public void setTheAutomatic(Automatic automatic) {
        this.theAutomatic = automatic;
    }

    public Set<ToolParameter> getTheToolParameters() {
        return theToolParameters;
    }

    public Subroutine theToolParameters(Set<ToolParameter> toolParameters) {
        this.theToolParameters = toolParameters;
        return this;
    }

    public Subroutine addTheToolParameters(ToolParameter toolParameter) {
        this.theToolParameters.add(toolParameter);
        toolParameter.setTheSubroutine(this);
        return this;
    }

    public Subroutine removeTheToolParameters(ToolParameter toolParameter) {
        this.theToolParameters.remove(toolParameter);
        toolParameter.setTheSubroutine(null);
        return this;
    }

    public void setTheToolParameters(Set<ToolParameter> toolParameters) {
        this.theToolParameters = toolParameters;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subroutine)) {
            return false;
        }
        return id != null && id.equals(((Subroutine) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Subroutine{" +
            "id=" + getId() +
            "}";
    }
}
