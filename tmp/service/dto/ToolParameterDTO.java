package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ToolParameter} entity.
 */
public class ToolParameterDTO implements Serializable {

    private Long id;

    private String label;

    private String separatorSymbol;


    private Long theArtifactTypeId;

    private Long theSubroutineId;

    private Long thePrimitiveTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSeparatorSymbol() {
        return separatorSymbol;
    }

    public void setSeparatorSymbol(String separatorSymbol) {
        this.separatorSymbol = separatorSymbol;
    }

    public Long getTheArtifactTypeId() {
        return theArtifactTypeId;
    }

    public void setTheArtifactTypeId(Long artifactTypeId) {
        this.theArtifactTypeId = artifactTypeId;
    }

    public Long getTheSubroutineId() {
        return theSubroutineId;
    }

    public void setTheSubroutineId(Long subroutineId) {
        this.theSubroutineId = subroutineId;
    }

    public Long getThePrimitiveTypeId() {
        return thePrimitiveTypeId;
    }

    public void setThePrimitiveTypeId(Long primitiveTypeId) {
        this.thePrimitiveTypeId = primitiveTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ToolParameterDTO toolParameterDTO = (ToolParameterDTO) o;
        if (toolParameterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), toolParameterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ToolParameterDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", separatorSymbol='" + getSeparatorSymbol() + "'" +
            ", theArtifactType=" + getTheArtifactTypeId() +
            ", theSubroutine=" + getTheSubroutineId() +
            ", thePrimitiveType=" + getThePrimitiveTypeId() +
            "}";
    }
}
