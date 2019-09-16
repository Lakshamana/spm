package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A RoleNeedsAbility. */
@Entity
@Table(name = "role_needs_ability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RoleNeedsAbility implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "degree")
  private Integer degree;

  @ManyToOne
  @JsonIgnoreProperties("theRoleNeedsAbilities")
  private Role theRole;

  @ManyToOne
  @JsonIgnoreProperties("theRoleNeedsAbilities")
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

  public RoleNeedsAbility degree(Integer degree) {
    this.degree = degree;
    return this;
  }

  public void setDegree(Integer degree) {
    this.degree = degree;
  }

  public Role getTheRole() {
    return theRole;
  }

  public RoleNeedsAbility theRole(Role role) {
    this.theRole = role;
    return this;
  }

  public void setTheRole(Role role) {
    this.theRole = role;
  }

  public Ability getTheAbility() {
    return theAbility;
  }

  public RoleNeedsAbility theAbility(Ability ability) {
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
    if (!(o instanceof RoleNeedsAbility)) {
      return false;
    }
    return id != null && id.equals(((RoleNeedsAbility) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "RoleNeedsAbility{" + "id=" + getId() + ", degree=" + getDegree() + "}";
  }
}
