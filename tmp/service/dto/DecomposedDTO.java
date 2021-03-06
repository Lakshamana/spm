package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Decomposed} entity.
 */
public class DecomposedDTO implements Serializable {

    private Long id;


    private Long theReferedProcessModelId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheReferedProcessModelId() {
        return theReferedProcessModelId;
    }

    public void setTheReferedProcessModelId(Long processModelId) {
        this.theReferedProcessModelId = processModelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DecomposedDTO decomposedDTO = (DecomposedDTO) o;
        if (decomposedDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), decomposedDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DecomposedDTO{" +
            "id=" + getId() +
            ", theReferedProcessModel=" + getTheReferedProcessModelId() +
            "}";
    }
}
