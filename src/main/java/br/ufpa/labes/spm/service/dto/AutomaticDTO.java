package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.AutomaticActivity} entity.
 */
public class AutomaticActivityDTO implements Serializable {

    private Long id;


    private Long theSubroutineId;

    private Long theArtifactId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheSubroutineId() {
        return theSubroutineId;
    }

    public void setTheSubroutineId(Long subroutineId) {
        this.theSubroutineId = subroutineId;
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

        AutomaticActivityDTO automaticActivityDTO = (AutomaticActivityDTO) o;
        if (automaticActivityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), automaticActivityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AutomaticActivityDTO{" +
            "id=" + getId() +
            ", theSubroutine=" + getTheSubroutineId() +
            ", theArtifact=" + getTheArtifactId() +
            "}";
    }
}
