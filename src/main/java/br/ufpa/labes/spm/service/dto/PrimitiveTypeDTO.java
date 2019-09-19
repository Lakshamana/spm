package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.PrimitiveType} entity.
 */
public class PrimitiveTypeDTO implements Serializable {

    private Long id;

    private String ident;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrimitiveTypeDTO primitiveTypeDTO = (PrimitiveTypeDTO) o;
        if (primitiveTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), primitiveTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrimitiveTypeDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            "}";
    }
}
