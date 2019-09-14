package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.OrganizationMetric} entity.
 */
public class OrganizationMetricDTO implements Serializable {

    private Long id;


    private Long theOrganizationId;

    private Long theCompanyId;

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

    public Long getTheCompanyId() {
        return theCompanyId;
    }

    public void setTheCompanyId(Long companyId) {
        this.theCompanyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationMetricDTO organizationMetricDTO = (OrganizationMetricDTO) o;
        if (organizationMetricDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationMetricDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationMetricDTO{" +
            "id=" + getId() +
            ", theOrganization=" + getTheOrganizationId() +
            ", theCompany=" + getTheCompanyId() +
            "}";
    }
}
