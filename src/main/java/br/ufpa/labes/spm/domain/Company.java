package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "fantasy_name")
    private String fantasyName;

    @Column(name = "social_reason")
    private String socialReason;

    @Column(name = "acronym")
    private String acronym;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageURL;

    @Column(name = "url")
    private String url;

    @Column(name = "automatic_instantiation")
    private Boolean automaticInstantiation;

    @OneToMany(mappedBy = "theCompany")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrganizationMetric> organizationMetrics = new HashSet<>();

    @OneToMany(mappedBy = "theCompany")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrganizationEstimation> theCompanyEstimations = new HashSet<>();

    @OneToMany(mappedBy = "theOrganization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CompanyUnit> theCompanyUnits = new HashSet<>();

    @OneToMany(mappedBy = "theOrganization")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DevelopingSystem> theDevelopingSystems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public Company ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Company cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public Company fantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
        return this;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
    }

    public String getSocialReason() {
        return socialReason;
    }

    public Company socialReason(String socialReason) {
        this.socialReason = socialReason;
        return this;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public String getAcronym() {
        return acronym;
    }

    public Company acronym(String acronym) {
        this.acronym = acronym;
        return this;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getAddress() {
        return address;
    }

    public Company address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public Company phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public Company description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Company imageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUrl() {
        return url;
    }

    public Company url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isAutomaticInstantiation() {
        return automaticInstantiation;
    }

    public Company automaticInstantiation(Boolean automaticInstantiation) {
        this.automaticInstantiation = automaticInstantiation;
        return this;
    }

    public void setAutomaticInstantiation(Boolean automaticInstantiation) {
        this.automaticInstantiation = automaticInstantiation;
    }

    public Set<OrganizationMetric> getOrganizationMetrics() {
        return organizationMetrics;
    }

    public Company organizationMetrics(Set<OrganizationMetric> organizationMetrics) {
        this.organizationMetrics = organizationMetrics;
        return this;
    }

    public Company addOrganizationMetric(OrganizationMetric organizationMetric) {
        this.organizationMetrics.add(organizationMetric);
        organizationMetric.setTheCompany(this);
        return this;
    }

    public Company removeOrganizationMetric(OrganizationMetric organizationMetric) {
        this.organizationMetrics.remove(organizationMetric);
        organizationMetric.setTheCompany(null);
        return this;
    }

    public void setOrganizationMetrics(Set<OrganizationMetric> organizationMetrics) {
        this.organizationMetrics = organizationMetrics;
    }

    public Set<OrganizationEstimation> getTheCompanyEstimations() {
        return theCompanyEstimations;
    }

    public Company theCompanyEstimations(Set<OrganizationEstimation> organizationEstimations) {
        this.theCompanyEstimations = organizationEstimations;
        return this;
    }

    public Company addTheCompanyEstimation(OrganizationEstimation organizationEstimation) {
        this.theCompanyEstimations.add(organizationEstimation);
        organizationEstimation.setTheCompany(this);
        return this;
    }

    public Company removeTheCompanyEstimation(OrganizationEstimation organizationEstimation) {
        this.theCompanyEstimations.remove(organizationEstimation);
        organizationEstimation.setTheCompany(null);
        return this;
    }

    public void setTheCompanyEstimations(Set<OrganizationEstimation> organizationEstimations) {
        this.theCompanyEstimations = organizationEstimations;
    }

    public Set<CompanyUnit> getTheCompanyUnits() {
        return theCompanyUnits;
    }

    public Company theCompanyUnits(Set<CompanyUnit> companyUnits) {
        this.theCompanyUnits = companyUnits;
        return this;
    }

    public Company addTheCompanyUnit(CompanyUnit companyUnit) {
        this.theCompanyUnits.add(companyUnit);
        companyUnit.setTheOrganization(this);
        return this;
    }

    public Company removeTheCompanyUnit(CompanyUnit companyUnit) {
        this.theCompanyUnits.remove(companyUnit);
        companyUnit.setTheOrganization(null);
        return this;
    }

    public void setTheCompanyUnits(Set<CompanyUnit> companyUnits) {
        this.theCompanyUnits = companyUnits;
    }

    public Set<DevelopingSystem> getTheDevelopingSystems() {
        return theDevelopingSystems;
    }

    public Company theDevelopingSystems(Set<DevelopingSystem> developingSystems) {
        this.theDevelopingSystems = developingSystems;
        return this;
    }

    public Company addTheDevelopingSystem(DevelopingSystem developingSystem) {
        this.theDevelopingSystems.add(developingSystem);
        developingSystem.setTheOrganization(this);
        return this;
    }

    public Company removeTheDevelopingSystem(DevelopingSystem developingSystem) {
        this.theDevelopingSystems.remove(developingSystem);
        developingSystem.setTheOrganization(null);
        return this;
    }

    public void setTheDevelopingSystems(Set<DevelopingSystem> developingSystems) {
        this.theDevelopingSystems = developingSystems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", fantasyName='" + getFantasyName() + "'" +
            ", socialReason='" + getSocialReason() + "'" +
            ", acronym='" + getAcronym() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", description='" + getDescription() + "'" +
            ", imageURL='" + getImageURL() + "'" +
            ", url='" + getUrl() + "'" +
            ", automaticInstantiation='" + isAutomaticInstantiation() + "'" +
            "}";
    }
}
