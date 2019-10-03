package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.RelationshipKind} entity.
 */
public class RelationshipKindDTO implements Serializable {

    private Long id;

    private String typeIdent;

    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeIdent() {
        return typeIdent;
    }

    public void setTypeIdent(String typeIdent) {
        this.typeIdent = typeIdent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RelationshipKindDTO relationshipKindDTO = (RelationshipKindDTO) o;
        if (relationshipKindDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), relationshipKindDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RelationshipKindDTO{" +
            "id=" + getId() +
            ", typeIdent='" + getTypeIdent() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
