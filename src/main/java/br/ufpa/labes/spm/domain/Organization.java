package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organization extends Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain")
    private String domain;

    @OneToMany(mappedBy = "theOrganization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrganizationMetric> theOrganizationMetrics = new HashSet<>();

    @OneToMany(mappedBy = "theOrganization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrganizationEstimation> theOrganizationEstimations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public Organization domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Set<OrganizationMetric> getTheOrganizationMetrics() {
        return theOrganizationMetrics;
    }

    public Organization theOrganizationMetrics(Set<OrganizationMetric> organizationMetrics) {
        this.theOrganizationMetrics = organizationMetrics;
        return this;
    }

    public Organization addTheOrganizationMetric(OrganizationMetric organizationMetric) {
        this.theOrganizationMetrics.add(organizationMetric);
        organizationMetric.setTheOrganization(this);
        return this;
    }

    public Organization removeTheOrganizationMetric(OrganizationMetric organizationMetric) {
        this.theOrganizationMetrics.remove(organizationMetric);
        organizationMetric.setTheOrganization(null);
        return this;
    }

    public void setTheOrganizationMetrics(Set<OrganizationMetric> organizationMetrics) {
        this.theOrganizationMetrics = organizationMetrics;
    }

    public Set<OrganizationEstimation> getTheOrganizationEstimations() {
        return theOrganizationEstimations;
    }

    public Organization theOrganizationEstimations(Set<OrganizationEstimation> organizationEstimations) {
        this.theOrganizationEstimations = organizationEstimations;
        return this;
    }

    public Organization addTheOrganizationEstimation(OrganizationEstimation organizationEstimation) {
        this.theOrganizationEstimations.add(organizationEstimation);
        organizationEstimation.setTheOrganization(this);
        return this;
    }

    public Organization removeTheOrganizationEstimation(OrganizationEstimation organizationEstimation) {
        this.theOrganizationEstimations.remove(organizationEstimation);
        organizationEstimation.setTheOrganization(null);
        return this;
    }

    public void setTheOrganizationEstimations(Set<OrganizationEstimation> organizationEstimations) {
        this.theOrganizationEstimations = organizationEstimations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", domain='" + getDomain() + "'" +
            "}";
    }
}
