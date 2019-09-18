package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CompanyUnit.
 */
@Entity
@Table(name = "company_unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CompanyUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("theCompanyUnits")
    private Company theOrganization;

    @ManyToOne
    @JsonIgnoreProperties("theCompanyUnits")
    private CompanyUnit theCommand;

    @ManyToOne
    @JsonIgnoreProperties("theManagedOrgUnits")
    private Agent theAgent;

    @OneToMany(mappedBy = "theCommand")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CompanyUnit> theCompanyUnits = new HashSet<>();

    @ManyToMany(mappedBy = "theOrgUnits")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Agent> theAgents = new HashSet<>();

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

    public CompanyUnit ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public CompanyUnit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public CompanyUnit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Company getTheOrganization() {
        return theOrganization;
    }

    public CompanyUnit theOrganization(Company company) {
        this.theOrganization = company;
        return this;
    }

    public void setTheOrganization(Company company) {
        this.theOrganization = company;
    }

    public CompanyUnit getTheCommand() {
        return theCommand;
    }

    public CompanyUnit theCommand(CompanyUnit companyUnit) {
        this.theCommand = companyUnit;
        return this;
    }

    public void setTheCommand(CompanyUnit companyUnit) {
        this.theCommand = companyUnit;
    }

    public Agent getTheAgent() {
        return theAgent;
    }

    public CompanyUnit theAgent(Agent agent) {
        this.theAgent = agent;
        return this;
    }

    public void setTheAgent(Agent agent) {
        this.theAgent = agent;
    }

    public Set<CompanyUnit> getTheCompanyUnits() {
        return theCompanyUnits;
    }

    public CompanyUnit theCompanyUnits(Set<CompanyUnit> companyUnits) {
        this.theCompanyUnits = companyUnits;
        return this;
    }

    public CompanyUnit addTheCompanyUnit(CompanyUnit companyUnit) {
        this.theCompanyUnits.add(companyUnit);
        companyUnit.setTheCommand(this);
        return this;
    }

    public CompanyUnit removeTheCompanyUnit(CompanyUnit companyUnit) {
        this.theCompanyUnits.remove(companyUnit);
        companyUnit.setTheCommand(null);
        return this;
    }

    public void setTheCompanyUnits(Set<CompanyUnit> companyUnits) {
        this.theCompanyUnits = companyUnits;
    }

    public Set<Agent> getTheAgents() {
        return theAgents;
    }

    public CompanyUnit theAgents(Set<Agent> agents) {
        this.theAgents = agents;
        return this;
    }

    public CompanyUnit addTheAgent(Agent agent) {
        this.theAgents.add(agent);
        agent.getTheOrgUnits().add(this);
        return this;
    }

    public CompanyUnit removeTheAgent(Agent agent) {
        this.theAgents.remove(agent);
        agent.getTheOrgUnits().remove(this);
        return this;
    }

    public void setTheAgents(Set<Agent> agents) {
        this.theAgents = agents;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyUnit)) {
            return false;
        }
        return id != null && id.equals(((CompanyUnit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CompanyUnit{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
