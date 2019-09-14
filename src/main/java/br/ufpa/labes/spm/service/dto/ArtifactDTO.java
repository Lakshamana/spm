package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Artifact} entity.
 */
public class ArtifactDTO implements Serializable {

    private Long id;

    private String ident;

    private String name;

    private String description;

    private String pathName;

    private String fileName;

    private String latestVersion;

    private Boolean isTemplate;

    private Boolean isActive;


    private Long theArtifactTypeId;

    private Long derivedFromId;

    private Long theProjectId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public Boolean isIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getTheArtifactTypeId() {
        return theArtifactTypeId;
    }

    public void setTheArtifactTypeId(Long artifactTypeId) {
        this.theArtifactTypeId = artifactTypeId;
    }

    public Long getDerivedFromId() {
        return derivedFromId;
    }

    public void setDerivedFromId(Long artifactId) {
        this.derivedFromId = artifactId;
    }

    public Long getTheProjectId() {
        return theProjectId;
    }

    public void setTheProjectId(Long projectId) {
        this.theProjectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArtifactDTO artifactDTO = (ArtifactDTO) o;
        if (artifactDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), artifactDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ArtifactDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", pathName='" + getPathName() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", latestVersion='" + getLatestVersion() + "'" +
            ", isTemplate='" + isIsTemplate() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", theArtifactType=" + getTheArtifactTypeId() +
            ", derivedFrom=" + getDerivedFromId() +
            ", theProject=" + getTheProjectId() +
            "}";
    }
}
