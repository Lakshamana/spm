package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.OrganizationEstimation} entity.
 */
public class OrganizationEstimationDTO implements Serializable {

    private Long id;


    private Long theOrganizationId;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheOrganizationId() {
        return theOrganizationId;
    }

    public void setTheOrganizationId(Long organizationId) {
        this.theOrganizationId = organizationId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationEstimationDTO organizationEstimationDTO = (OrganizationEstimationDTO) o;
        if (organizationEstimationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationEstimationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationEstimationDTO{" +
            "id=" + getId() +
            ", theOrganization=" + getTheOrganizationId() +
            ", company=" + getCompanyId() +
            "}";
    }
}
