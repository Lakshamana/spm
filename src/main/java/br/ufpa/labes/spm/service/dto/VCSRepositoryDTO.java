package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.VCSRepository} entity.
 */
public class VCSRepositoryDTO implements Serializable {

    private Long id;

    private String ident;

    private String controlVersionSystem;

    private String server;

    private String repositoryPath;


    private Long theStructureId;

    private Long theArtifactId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getControlVersionSystem() {
        return controlVersionSystem;
    }

    public void setControlVersionSystem(String controlVersionSystem) {
        this.controlVersionSystem = controlVersionSystem;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    public Long getTheStructureId() {
        return theStructureId;
    }

    public void setTheStructureId(Long structureId) {
        this.theStructureId = structureId;
    }

    public Long getTheArtifactId() {
        return theArtifactId;
    }

    public void setTheArtifactId(Long artifactId) {
        this.theArtifactId = artifactId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VCSRepositoryDTO vCSRepositoryDTO = (VCSRepositoryDTO) o;
        if (vCSRepositoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), vCSRepositoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VCSRepositoryDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", controlVersionSystem='" + getControlVersionSystem() + "'" +
            ", server='" + getServer() + "'" +
            ", repositoryPath='" + getRepositoryPath() + "'" +
            ", theStructure=" + getTheStructureId() +
            ", theArtifact=" + getTheArtifactId() +
            "}";
    }
}
