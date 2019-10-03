package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.CompanyUnit} entity.
 */
public class CompanyUnitDTO implements Serializable {

    private Long id;

    private String ident;

    private String name;

    @Lob
    private String description;


    private Long theOrganizationId;

    private Long theCommandId;

    private Long theAgentId;

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

    public Long getTheCommandId() {
        return theCommandId;
    }

    public void setTheCommandId(Long companyUnitId) {
        this.theCommandId = companyUnitId;
    }

    public Long getTheAgentId() {
        return theAgentId;
    }

    public void setTheAgentId(Long agentId) {
        this.theAgentId = agentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CompanyUnitDTO companyUnitDTO = (CompanyUnitDTO) o;
        if (companyUnitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), companyUnitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CompanyUnitDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", theOrganization=" + getTheOrganizationId() +
            ", theCommand=" + getTheCommandId() +
            ", theAgent=" + getTheAgentId() +
            "}";
    }
}
