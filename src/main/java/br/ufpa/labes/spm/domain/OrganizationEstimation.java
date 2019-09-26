package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A OrganizationEstimation.
 */
@Entity
@Table(name = "organization_estimation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganizationEstimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("organizationEstimations")
    private Organization theOrganization;

    @ManyToOne
    @JsonIgnoreProperties("theCompanyEstimations")
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getTheOrganization() {
        return theOrganization;
    }

    public OrganizationEstimation theOrganization(Organization organization) {
        this.theOrganization = organization;
        return this;
    }

    public void setTheOrganization(Organization organization) {
        this.theOrganization = organization;
    }

    public Company getCompany() {
        return company;
    }

    public OrganizationEstimation company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationEstimation)) {
            return false;
        }
        return id != null && id.equals(((OrganizationEstimation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrganizationEstimation{" +
            "id=" + getId() +
            "}";
    }
}
