package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.DevelopingSystem} entity.
 */
public class DevelopingSystemDTO implements Serializable {

    private Long id;

    private String ident;

    private String name;

    @Lob
    private String description;


    private Long theOrganizationId;

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

    public Long getTheOrganizationId() {
        return theOrganizationId;
    }

    public void setTheOrganizationId(Long companyId) {
        this.theOrganizationId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DevelopingSystemDTO developingSystemDTO = (DevelopingSystemDTO) o;
        if (developingSystemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), developingSystemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DevelopingSystemDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", theOrganization=" + getTheOrganizationId() +
            "}";
    }
}
