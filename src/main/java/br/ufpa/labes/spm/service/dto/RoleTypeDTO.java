package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.RoleType} entity.
 */
public class RoleTypeDTO implements Serializable {

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

        RoleTypeDTO roleTypeDTO = (RoleTypeDTO) o;
        if (roleTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roleTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoleTypeDTO{" +
            "id=" + getId() +
            "}";
    }
}
