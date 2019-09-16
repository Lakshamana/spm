package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A RoleType. */
@Entity
@Table(name = "role_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RoleType extends Type implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy = "theRoleTypeSub")
  @JsonIgnore
  private Type theTypeSuper;

  @OneToMany(mappedBy = "theRoleType")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Role> theRoles = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Type getTheTypeSuper() {
    return theTypeSuper;
  }

  public RoleType theTypeSuper(Type type) {
    this.theTypeSuper = type;
    return this;
  }

  public void setTheTypeSuper(Type type) {
    this.theTypeSuper = type;
  }

  public Set<Role> getTheRoles() {
    return theRoles;
  }

  public RoleType theRoles(Set<Role> roles) {
    this.theRoles = roles;
    return this;
  }

  public RoleType addTheRole(Role role) {
    this.theRoles.add(role);
    role.setTheRoleType(this);
    return this;
  }

  public RoleType removeTheRole(Role role) {
    this.theRoles.remove(role);
    role.setTheRoleType(null);
    return this;
  }

  public void setTheRoles(Set<Role> roles) {
    this.theRoles = roles;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RoleType)) {
      return false;
    }
    return id != null && id.equals(((RoleType) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "RoleType{" + "id=" + getId() + "}";
  }
}
