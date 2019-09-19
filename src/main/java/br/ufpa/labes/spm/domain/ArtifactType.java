package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ArtifactType.
 */
@Entity
@Table(name = "artifact_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArtifactType extends Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "theArtifactType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Artifact> theArtifacts = new HashSet<>();

    @OneToMany(mappedBy = "theArtifactType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArtifactCon> theArtifactCons = new HashSet<>();

    @OneToMany(mappedBy = "theArtifactType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvolvedArtifact> theInvolvedArtifacts = new HashSet<>();

    @OneToMany(mappedBy = "theArtifactType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Subroutine> theSubroutines = new HashSet<>();

    @OneToMany(mappedBy = "theArtifactType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ToolParameter> theToolParameters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Artifact> getTheArtifacts() {
        return theArtifacts;
    }

    public ArtifactType theArtifacts(Set<Artifact> artifacts) {
        this.theArtifacts = artifacts;
        return this;
    }

    public ArtifactType addTheArtifact(Artifact artifact) {
        this.theArtifacts.add(artifact);
        artifact.setTheArtifactType(this);
        return this;
    }

    public ArtifactType removeTheArtifact(Artifact artifact) {
        this.theArtifacts.remove(artifact);
        artifact.setTheArtifactType(null);
        return this;
    }

    public void setTheArtifacts(Set<Artifact> artifacts) {
        this.theArtifacts = artifacts;
    }

    public Set<ArtifactCon> getTheArtifactCons() {
        return theArtifactCons;
    }

    public ArtifactType theArtifactCons(Set<ArtifactCon> artifactCons) {
        this.theArtifactCons = artifactCons;
        return this;
    }

    public ArtifactType addTheArtifactCon(ArtifactCon artifactCon) {
        this.theArtifactCons.add(artifactCon);
        artifactCon.setTheArtifactType(this);
        return this;
    }

    public ArtifactType removeTheArtifactCon(ArtifactCon artifactCon) {
        this.theArtifactCons.remove(artifactCon);
        artifactCon.setTheArtifactType(null);
        return this;
    }

    public void setTheArtifactCons(Set<ArtifactCon> artifactCons) {
        this.theArtifactCons = artifactCons;
    }

    public Set<InvolvedArtifact> getTheInvolvedArtifacts() {
        return theInvolvedArtifacts;
    }

    public ArtifactType theInvolvedArtifacts(Set<InvolvedArtifact> involvedArtifacts) {
        this.theInvolvedArtifacts = involvedArtifacts;
        return this;
    }

    public ArtifactType addTheInvolvedArtifact(InvolvedArtifact involvedArtifact) {
        this.theInvolvedArtifacts.add(involvedArtifact);
        involvedArtifact.setTheArtifactType(this);
        return this;
    }

    public ArtifactType removeTheInvolvedArtifact(InvolvedArtifact involvedArtifact) {
        this.theInvolvedArtifacts.remove(involvedArtifact);
        involvedArtifact.setTheArtifactType(null);
        return this;
    }

    public void setTheInvolvedArtifacts(Set<InvolvedArtifact> involvedArtifacts) {
        this.theInvolvedArtifacts = involvedArtifacts;
    }

    public Set<Subroutine> getTheSubroutines() {
        return theSubroutines;
    }

    public ArtifactType theSubroutines(Set<Subroutine> subroutines) {
        this.theSubroutines = subroutines;
        return this;
    }

    public ArtifactType addTheSubroutine(Subroutine subroutine) {
        this.theSubroutines.add(subroutine);
        subroutine.setTheArtifactType(this);
        return this;
    }

    public ArtifactType removeTheSubroutine(Subroutine subroutine) {
        this.theSubroutines.remove(subroutine);
        subroutine.setTheArtifactType(null);
        return this;
    }

    public void setTheSubroutines(Set<Subroutine> subroutines) {
        this.theSubroutines = subroutines;
    }

    public Set<ToolParameter> getTheToolParameters() {
        return theToolParameters;
    }

    public ArtifactType theToolParameters(Set<ToolParameter> toolParameters) {
        this.theToolParameters = toolParameters;
        return this;
    }

    public ArtifactType addTheToolParameter(ToolParameter toolParameter) {
        this.theToolParameters.add(toolParameter);
        toolParameter.setTheArtifactType(this);
        return this;
    }

    public ArtifactType removeTheToolParameter(ToolParameter toolParameter) {
        this.theToolParameters.remove(toolParameter);
        toolParameter.setTheArtifactType(null);
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
        if (!(o instanceof ArtifactType)) {
            return false;
        }
        return id != null && id.equals(((ArtifactType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ArtifactType{" +
            "id=" + getId() +
            "}";
    }
}
