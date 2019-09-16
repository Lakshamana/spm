package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import br.ufpa.labes.spm.domain.enumeration.TemplateStatus;

/** A Template. */
@Entity
@Table(name = "template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Template extends Process implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "t_status")
  private TemplateStatus tStatus;

  @OneToMany(mappedBy = "theOrigin")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ProcessModel> theProcessModels = new HashSet<>();

  @ManyToOne
  @JsonIgnoreProperties("descTemplateOriginalVersions")
  private Description theOriginalVersionDescription;

  @OneToOne(mappedBy = "theTemplateSub")
  @JsonIgnore
  private Process theProcessSuper;

  @OneToMany(mappedBy = "theTemplateOldVersion")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Description> theDerivedVersionDescriptions = new HashSet<>();

  @OneToMany(mappedBy = "theTemplateNewVersion")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Description> theTemplateNewDescriptions = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public TemplateStatus gettStatus() {
    return tStatus;
  }

  public Template tStatus(TemplateStatus tStatus) {
    this.tStatus = tStatus;
    return this;
  }

  public void settStatus(TemplateStatus tStatus) {
    this.tStatus = tStatus;
  }

  public Set<ProcessModel> getTheProcessModels() {
    return theProcessModels;
  }

  public Template theProcessModels(Set<ProcessModel> processModels) {
    this.theProcessModels = processModels;
    return this;
  }

  public Template addTheProcessModel(ProcessModel processModel) {
    this.theProcessModels.add(processModel);
    processModel.setTheOrigin(this);
    return this;
  }

  public Template removeTheProcessModel(ProcessModel processModel) {
    this.theProcessModels.remove(processModel);
    processModel.setTheOrigin(null);
    return this;
  }

  public void setTheProcessModels(Set<ProcessModel> processModels) {
    this.theProcessModels = processModels;
  }

  public Description getTheOriginalVersionDescription() {
    return theOriginalVersionDescription;
  }

  public Template theOriginalVersionDescription(Description description) {
    this.theOriginalVersionDescription = description;
    return this;
  }

  public void setTheOriginalVersionDescription(Description description) {
    this.theOriginalVersionDescription = description;
  }

  public Process getTheProcessSuper() {
    return theProcessSuper;
  }

  public Template theProcessSuper(Process process) {
    this.theProcessSuper = process;
    return this;
  }

  public void setTheProcessSuper(Process process) {
    this.theProcessSuper = process;
  }

  public Set<Description> getTheDerivedVersionDescriptions() {
    return theDerivedVersionDescriptions;
  }

  public Template theDerivedVersionDescriptions(Set<Description> descriptions) {
    this.theDerivedVersionDescriptions = descriptions;
    return this;
  }

  public Template addTheDerivedVersionDescription(Description description) {
    this.theDerivedVersionDescriptions.add(description);
    description.setTheTemplateOldVersion(this);
    return this;
  }

  public Template removeTheDerivedVersionDescription(Description description) {
    this.theDerivedVersionDescriptions.remove(description);
    description.setTheTemplateOldVersion(null);
    return this;
  }

  public void setTheDerivedVersionDescriptions(Set<Description> descriptions) {
    this.theDerivedVersionDescriptions = descriptions;
  }

  public Set<Description> getTheTemplateNewDescriptions() {
    return theTemplateNewDescriptions;
  }

  public Template theTemplateNewDescriptions(Set<Description> descriptions) {
    this.theTemplateNewDescriptions = descriptions;
    return this;
  }

  public Template addTheTemplateNewDescription(Description description) {
    this.theTemplateNewDescriptions.add(description);
    description.setTheTemplateNewVersion(this);
    return this;
  }

  public Template removeTheTemplateNewDescription(Description description) {
    this.theTemplateNewDescriptions.remove(description);
    description.setTheTemplateNewVersion(null);
    return this;
  }

  public void setTheTemplateNewDescriptions(Set<Description> descriptions) {
    this.theTemplateNewDescriptions = descriptions;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Template)) {
      return false;
    }
    return id != null && id.equals(((Template) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "Template{" + "id=" + getId() + ", tStatus='" + gettStatus() + "'" + "}";
  }
}
