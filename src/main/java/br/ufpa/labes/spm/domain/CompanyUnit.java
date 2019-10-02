package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A CompanyUnit. */
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
  @JsonIgnoreProperties("theOrganizationalUnits")
  private Company theOrganization;

  @ManyToOne
  @JsonIgnoreProperties("theSubordinates")
  private CompanyUnit theCommand;

  @ManyToOne
  @JsonIgnoreProperties("theManagedOrgUnits")
  private Agent theAgent;

  @OneToMany(mappedBy = "theCommand")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<CompanyUnit> theSubordinates = new HashSet<>();

  @ManyToMany(mappedBy = "theOrgUnits")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JsonIgnore
  private Set<Agent> theUnitAgents = new HashSet<>();

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

  public Set<CompanyUnit> getTheSubordinates() {
    return theSubordinates;
  }

  public CompanyUnit theSubordinates(Set<CompanyUnit> companyUnits) {
    this.theSubordinates = companyUnits;
    return this;
  }

  public CompanyUnit addTheSubordinates(CompanyUnit companyUnit) {
    this.theSubordinates.add(companyUnit);
    companyUnit.setTheCommand(this);
    return this;
  }

  public CompanyUnit removeTheSubordinates(CompanyUnit companyUnit) {
    this.theSubordinates.remove(companyUnit);
    companyUnit.setTheCommand(null);
    return this;
  }

  public void setTheSubordinates(Set<CompanyUnit> companyUnits) {
    this.theSubordinates = companyUnits;
  }

  public Set<Agent> getTheUnitAgents() {
    return theUnitAgents;
  }

  public CompanyUnit theUnitAgents(Set<Agent> agents) {
    this.theUnitAgents = agents;
    return this;
  }

  public CompanyUnit addTheUnitAgents(Agent agent) {
    this.theUnitAgents.add(agent);
    agent.getTheOrgUnits().add(this);
    return this;
  }

  public CompanyUnit removeTheUnitAgents(Agent agent) {
    this.theUnitAgents.remove(agent);
    agent.getTheOrgUnits().remove(this);
    return this;
  }

  public void setTheUnitAgents(Set<Agent> agents) {
    this.theUnitAgents = agents;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

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
    return "CompanyUnit{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", name='"
        + getName()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + "}";
  }
}
