package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A VCSRepository.
 */
@Entity
@Table(name = "vcs_repository")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VCSRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "control_version_system")
    private String controlVersionSystem;

    @Column(name = "server")
    private String server;

    @Column(name = "repository_path")
    private String repositoryPath;

    @ManyToOne
    @JsonIgnoreProperties("theVCSRepositories")
    private Structure theStructure;

    @ManyToOne
    @JsonIgnoreProperties("theVCSRepositories")
    private Artifact theArtifact;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public VCSRepository ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getControlVersionSystem() {
        return controlVersionSystem;
    }

    public VCSRepository controlVersionSystem(String controlVersionSystem) {
        this.controlVersionSystem = controlVersionSystem;
        return this;
    }

    public void setControlVersionSystem(String controlVersionSystem) {
        this.controlVersionSystem = controlVersionSystem;
    }

    public String getServer() {
        return server;
    }

    public VCSRepository server(String server) {
        this.server = server;
        return this;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public VCSRepository repositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
        return this;
    }

    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    public Structure getTheStructure() {
        return theStructure;
    }

    public VCSRepository theStructure(Structure structure) {
        this.theStructure = structure;
        return this;
    }

    public void setTheStructure(Structure structure) {
        this.theStructure = structure;
    }

    public Artifact getTheArtifact() {
        return theArtifact;
    }

    public VCSRepository theArtifact(Artifact artifact) {
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
        if (!(o instanceof VCSRepository)) {
            return false;
        }
        return id != null && id.equals(((VCSRepository) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "VCSRepository{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", controlVersionSystem='" + getControlVersionSystem() + "'" +
            ", server='" + getServer() + "'" +
            ", repositoryPath='" + getRepositoryPath() + "'" +
            "}";
    }
}
