package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Automatic.
 */
@Entity
@Table(name = "automatic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Automatic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Subroutine theSubroutine;

    @OneToOne(mappedBy = "theAutomaticSub")
    @JsonIgnore
    private Plain thePlainSuper;

    @ManyToOne
    @JsonIgnoreProperties("theAutomatics")
    private Artifact theArtifact;

    @OneToMany(mappedBy = "theAutomatic")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Parameter> theParameters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subroutine getTheSubroutine() {
        return theSubroutine;
    }

    public Automatic theSubroutine(Subroutine subroutine) {
        this.theSubroutine = subroutine;
        return this;
    }

    public void setTheSubroutine(Subroutine subroutine) {
        this.theSubroutine = subroutine;
    }

    public Plain getThePlainSuper() {
        return thePlainSuper;
    }

    public Automatic thePlainSuper(Plain plain) {
        this.thePlainSuper = plain;
        return this;
    }

    public void setThePlainSuper(Plain plain) {
        this.thePlainSuper = plain;
    }

    public Artifact getTheArtifact() {
        return theArtifact;
    }

    public Automatic theArtifact(Artifact artifact) {
        this.theArtifact = artifact;
        return this;
    }

    public void setTheArtifact(Artifact artifact) {
        this.theArtifact = artifact;
    }

    public Set<Parameter> getTheParameters() {
        return theParameters;
    }

    public Automatic theParameters(Set<Parameter> parameters) {
        this.theParameters = parameters;
        return this;
    }

    public Automatic addTheParameters(Parameter parameter) {
        this.theParameters.add(parameter);
        parameter.setTheAutomatic(this);
        return this;
    }

    public Automatic removeTheParameters(Parameter parameter) {
        this.theParameters.remove(parameter);
        parameter.setTheAutomatic(null);
        return this;
    }

    public void setTheParameters(Set<Parameter> parameters) {
        this.theParameters = parameters;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Automatic)) {
            return false;
        }
        return id != null && id.equals(((Automatic) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Automatic{" +
            "id=" + getId() +
            "}";
    }
}
