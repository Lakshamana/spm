package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Structure} entity.
 */
public class StructureDTO implements Serializable {

    private Long id;


    private Long rootElementId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRootElementId() {
        return rootElementId;
    }

    public void setRootElementId(Long nodeId) {
        this.rootElementId = nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StructureDTO structureDTO = (StructureDTO) o;
        if (structureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), structureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StructureDTO{" +
            "id=" + getId() +
            ", rootElement=" + getRootElementId() +
            "}";
    }
}
