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
 * A AutomaticActivity.
 */
@Entity
@Table(name = "automatic_activity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AutomaticActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private Subroutine theSubroutine;

    @OneToOne(mappedBy = "theAutomaticActivitySub")
    @JsonIgnore
    private Plain theAutomatic;

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

    public AutomaticActivity theSubroutine(Subroutine subroutine) {
        this.theSubroutine = subroutine;
        return this;
    }

    public void setTheSubroutine(Subroutine subroutine) {
        this.theSubroutine = subroutine;
    }

    public Plain getTheAutomatic() {
        return theAutomatic;
    }

    public AutomaticActivity theAutomatic(Plain plain) {
        this.theAutomatic = plain;
        return this;
    }

    public void setTheAutomatic(Plain plain) {
        this.theAutomatic = plain;
    }

    public Artifact getTheArtifact() {
        return theArtifact;
    }

    public AutomaticActivity theArtifact(Artifact artifact) {
        this.theArtifact = artifact;
        return this;
    }

    public void setTheArtifact(Artifact artifact) {
        this.theArtifact = artifact;
    }

    public Set<Parameter> getTheParameters() {
        return theParameters;
    }

    public AutomaticActivity theParameters(Set<Parameter> parameters) {
        this.theParameters = parameters;
        return this;
    }

    public AutomaticActivity addTheParameters(Parameter parameter) {
        this.theParameters.add(parameter);
        parameter.setTheAutomatic(this);
        return this;
    }

    public AutomaticActivity removeTheParameters(Parameter parameter) {
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
        if (!(o instanceof AutomaticActivity)) {
            return false;
        }
        return id != null && id.equals(((AutomaticActivity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AutomaticActivity{" +
            "id=" + getId() +
            "}";
    }
}
