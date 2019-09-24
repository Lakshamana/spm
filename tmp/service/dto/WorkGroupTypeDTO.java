package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.WorkGroupType} entity.
 */
public class WorkGroupTypeDTO implements Serializable {

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkGroupTypeDTO workGroupTypeDTO = (WorkGroupTypeDTO) o;
        if (workGroupTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workGroupTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkGroupTypeDTO{" +
            "id=" + getId() +
            "}";
    }
}
