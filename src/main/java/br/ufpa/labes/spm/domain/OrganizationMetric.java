package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A OrganizationMetric.
 */
@Entity
@Table(name = "organization_metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganizationMetric extends Metric implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theOrganizationMetrics")
    private Organization theOrganization;

    @OneToOne(mappedBy = "theOrganizationMetricSub")
    @JsonIgnore
    private Metric theMetricSuper;

    @ManyToOne
    @JsonIgnoreProperties("organizationMetrics")
    private Company theCompany;

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

    public OrganizationMetric theOrganization(Organization organization) {
        this.theOrganization = organization;
        return this;
    }

    public void setTheOrganization(Organization organization) {
        this.theOrganization = organization;
    }

    public Metric getTheMetricSuper() {
        return theMetricSuper;
    }

    public OrganizationMetric theMetricSuper(Metric metric) {
        this.theMetricSuper = metric;
        return this;
    }

    public void setTheMetricSuper(Metric metric) {
        this.theMetricSuper = metric;
    }

    public Company getTheCompany() {
        return theCompany;
    }

    public OrganizationMetric theCompany(Company company) {
        this.theCompany = company;
        return this;
    }

    public void setTheCompany(Company company) {
        this.theCompany = company;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationMetric)) {
            return false;
        }
        return id != null && id.equals(((OrganizationMetric) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrganizationMetric{" +
            "id=" + getId() +
            "}";
    }
}
