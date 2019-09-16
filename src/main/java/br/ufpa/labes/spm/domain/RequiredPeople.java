package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A RequiredPeople. */
@Entity
@Table(name = "required_people")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.JOINED)
public class RequiredPeople implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(unique = true)
  private ReqWorkGroup theReqWorkGroupSub;

  @OneToOne
  @JoinColumn(unique = true)
  private ReqAgent theReqAgentSub;

  @ManyToOne
  @JsonIgnoreProperties("theRequiredPeople")
  private Normal theNormal;

  @OneToMany(mappedBy = "theReqAgent")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ReqAgentRequiresAbility> theReqAgentRequiresAbilities = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ReqWorkGroup getTheReqWorkGroupSub() {
    return theReqWorkGroupSub;
  }

  public RequiredPeople theReqWorkGroupSub(ReqWorkGroup reqWorkGroup) {
    this.theReqWorkGroupSub = reqWorkGroup;
    return this;
  }

  public void setTheReqWorkGroupSub(ReqWorkGroup reqWorkGroup) {
    this.theReqWorkGroupSub = reqWorkGroup;
  }

  public ReqAgent getTheReqAgentSub() {
    return theReqAgentSub;
  }

  public RequiredPeople theReqAgentSub(ReqAgent reqAgent) {
    this.theReqAgentSub = reqAgent;
    return this;
  }

  public void setTheReqAgentSub(ReqAgent reqAgent) {
    this.theReqAgentSub = reqAgent;
  }

  public Normal getTheNormal() {
    return theNormal;
  }

  public RequiredPeople theNormal(Normal normal) {
    this.theNormal = normal;
    return this;
  }

  public void setTheNormal(Normal normal) {
    this.theNormal = normal;
  }

  public Set<ReqAgentRequiresAbility> getTheReqAgentRequiresAbilities() {
    return theReqAgentRequiresAbilities;
  }

  public RequiredPeople theReqAgentRequiresAbilities(
      Set<ReqAgentRequiresAbility> reqAgentRequiresAbilities) {
    this.theReqAgentRequiresAbilities = reqAgentRequiresAbilities;
    return this;
  }

  public RequiredPeople addTheReqAgentRequiresAbility(
      ReqAgentRequiresAbility reqAgentRequiresAbility) {
    this.theReqAgentRequiresAbilities.add(reqAgentRequiresAbility);
    reqAgentRequiresAbility.setTheReqAgent(this);
    return this;
  }

  public RequiredPeople removeTheReqAgentRequiresAbility(
      ReqAgentRequiresAbility reqAgentRequiresAbility) {
    this.theReqAgentRequiresAbilities.remove(reqAgentRequiresAbility);
    reqAgentRequiresAbility.setTheReqAgent(null);
    return this;
  }

  public void setTheReqAgentRequiresAbilities(
      Set<ReqAgentRequiresAbility> reqAgentRequiresAbilities) {
    this.theReqAgentRequiresAbilities = reqAgentRequiresAbilities;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RequiredPeople)) {
      return false;
    }
    return id != null && id.equals(((RequiredPeople) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "RequiredPeople{" + "id=" + getId() + "}";
  }
}
