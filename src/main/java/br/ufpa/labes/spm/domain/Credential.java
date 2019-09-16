package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import br.ufpa.labes.spm.domain.enumeration.OperationEnum;

/** A Credential. */
@Entity
@Table(name = "credential")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Credential implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "class_name")
  private String className;

  @Column(name = "uid")
  private String uid;

  @Enumerated(EnumType.STRING)
  @Column(name = "operation_enum")
  private OperationEnum operationEnum;

  @ManyToOne
  @JsonIgnoreProperties("theCredentials")
  private User user;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getClassName() {
    return className;
  }

  public Credential className(String className) {
    this.className = className;
    return this;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getUid() {
    return uid;
  }

  public Credential uid(String uid) {
    this.uid = uid;
    return this;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public OperationEnum getOperationEnum() {
    return operationEnum;
  }

  public Credential operationEnum(OperationEnum operationEnum) {
    this.operationEnum = operationEnum;
    return this;
  }

  public void setOperationEnum(OperationEnum operationEnum) {
    this.operationEnum = operationEnum;
  }

  public User getUser() {
    return user;
  }

  public Credential user(User user) {
    this.user = user;
    return this;
  }

  public void setUser(User user) {
    this.user = user;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Credential)) {
      return false;
    }
    return id != null && id.equals(((Credential) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Credential{"
        + "id="
        + getId()
        + ", className='"
        + getClassName()
        + "'"
        + ", uid='"
        + getUid()
        + "'"
        + ", operationEnum='"
        + getOperationEnum()
        + "'"
        + "}";
  }
}
