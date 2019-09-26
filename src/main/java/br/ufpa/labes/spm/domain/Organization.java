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
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "domain")
    private String domain;

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrganizationMetric> organizationMetrics = new HashSet<>();

    @OneToMany(mappedBy = "theOrganization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrganizationEstimation> organizationEstimations = new HashSet<>();

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

    public Set<OrganizationMetric> getOrganizationMetrics() {
        return organizationMetrics;
    }

    public Organization organizationMetrics(Set<OrganizationMetric> organizationMetrics) {
        this.organizationMetrics = organizationMetrics;
        return this;
    }

    public Organization addOrganizationMetric(OrganizationMetric organizationMetric) {
        this.organizationMetrics.add(organizationMetric);
        organizationMetric.setOrganization(this);
        return this;
    }

    public Organization removeOrganizationMetric(OrganizationMetric organizationMetric) {
        this.organizationMetrics.remove(organizationMetric);
        organizationMetric.setOrganization(null);
        return this;
    }

    public void setOrganizationMetrics(Set<OrganizationMetric> organizationMetrics) {
        this.organizationMetrics = organizationMetrics;
    }

    public Set<OrganizationEstimation> getOrganizationEstimations() {
        return organizationEstimations;
    }

    public Organization organizationEstimations(Set<OrganizationEstimation> organizationEstimations) {
        this.organizationEstimations = organizationEstimations;
        return this;
    }

    public Organization addOrganizationEstimation(OrganizationEstimation organizationEstimation) {
        this.organizationEstimations.add(organizationEstimation);
        organizationEstimation.setTheOrganization(this);
        return this;
    }

    public Organization removeOrganizationEstimation(OrganizationEstimation organizationEstimation) {
        this.organizationEstimations.remove(organizationEstimation);
        organizationEstimation.setTheOrganization(null);
        return this;
    }

    public void setOrganizationEstimations(Set<OrganizationEstimation> organizationEstimations) {
        this.organizationEstimations = organizationEstimations;
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
