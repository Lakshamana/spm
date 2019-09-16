package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A ClassMethodCall. */
@Entity
@Table(name = "class_method_call")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassMethodCall extends Subroutine implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "class_name")
  private String className;

  @Column(name = "method_name")
  private String methodName;

  @Lob
  @Column(name = "description")
  private String description;

  @OneToOne(mappedBy = "theClassMethodCallSub")
  @JsonIgnore
  private Subroutine theSubroutineSuper;

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

  public ClassMethodCall className(String className) {
    this.className = className;
    return this;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getMethodName() {
    return methodName;
  }

  public ClassMethodCall methodName(String methodName) {
    this.methodName = methodName;
    return this;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getDescription() {
    return description;
  }

  public ClassMethodCall description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Subroutine getTheSubroutineSuper() {
    return theSubroutineSuper;
  }

  public ClassMethodCall theSubroutineSuper(Subroutine subroutine) {
    this.theSubroutineSuper = subroutine;
    return this;
  }

  public void setTheSubroutineSuper(Subroutine subroutine) {
    this.theSubroutineSuper = subroutine;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ClassMethodCall)) {
      return false;
    }
    return id != null && id.equals(((ClassMethodCall) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ClassMethodCall{"
        + "id="
        + getId()
        + ", className='"
        + getClassName()
        + "'"
        + ", methodName='"
        + getMethodName()
        + "'"
        + ", description='"
        + getDescription()
        + "'"
        + "}";
  }
}
