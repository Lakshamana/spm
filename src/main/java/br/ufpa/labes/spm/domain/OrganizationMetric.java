package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A OrganizationMetric. */
@Entity
@Table(name = "organization_metric")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganizationMetric extends Metric implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JsonIgnoreProperties("organizationMetrics")
  private Organization organization;

  @ManyToOne
  @JsonIgnoreProperties("organizationMetrics")
  private Company company;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Organization getOrganization() {
    return organization;
  }

  public OrganizationMetric organization(Organization organization) {
    this.organization = organization;
    return this;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public Company getCompany() {
    return company;
  }

  public OrganizationMetric company(Company company) {
    this.company = company;
    return this;
  }

  public void setCompany(Company company) {
    this.company = company;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

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
    return "OrganizationMetric{" + "id=" + getId() + "}";
  }
}
