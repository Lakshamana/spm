package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ModelingActivityEvent} entity.
 */
public class ModelingActivityEventDTO implements Serializable {

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

        ModelingActivityEventDTO modelingActivityEventDTO = (ModelingActivityEventDTO) o;
        if (modelingActivityEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), modelingActivityEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ModelingActivityEventDTO{" +
            "id=" + getId() +
            "}";
    }
}
