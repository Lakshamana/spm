package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A ReqAgentRequiresAbility. */
@Entity
@Table(name = "req_agent_requires_ability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReqAgentRequiresAbility implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "degree")
  private Integer degree;

  @ManyToOne
  @JsonIgnoreProperties("theReqAgentRequiresAbilities")
  private RequiredPeople theReqAgent;

  @ManyToOne
  @JsonIgnoreProperties("theReqAgentRequiresAbilities")
  private Ability theAbility;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getDegree() {
    return degree;
  }

  public ReqAgentRequiresAbility degree(Integer degree) {
    this.degree = degree;
    return this;
  }

  public void setDegree(Integer degree) {
    this.degree = degree;
  }

  public RequiredPeople getTheReqAgent() {
    return theReqAgent;
  }

  public ReqAgentRequiresAbility theReqAgent(RequiredPeople requiredPeople) {
    this.theReqAgent = requiredPeople;
    return this;
  }

  public void setTheReqAgent(RequiredPeople requiredPeople) {
    this.theReqAgent = requiredPeople;
  }

  public Ability getTheAbility() {
    return theAbility;
  }

  public ReqAgentRequiresAbility theAbility(Ability ability) {
    this.theAbility = ability;
    return this;
  }

  public void setTheAbility(Ability ability) {
    this.theAbility = ability;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ReqAgentRequiresAbility)) {
      return false;
    }
    return id != null && id.equals(((ReqAgentRequiresAbility) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ReqAgentRequiresAbility{" + "id=" + getId() + ", degree=" + getDegree() + "}";
  }
}
