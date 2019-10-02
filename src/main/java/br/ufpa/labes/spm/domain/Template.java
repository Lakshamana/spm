package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/** A Template. */
@Entity
@Table(name = "template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Template extends Process implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "template_state")
  private String templateState;

  @OneToMany(mappedBy = "theOrigin")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<ProcessModel> theInstances = new HashSet<>();

  @ManyToOne
  @JsonIgnoreProperties("descTemplateOriginalVersions")
  private Description theOriginalVersionDescription;

  @OneToMany(mappedBy = "theOldVersion")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Description> theDerivedVersionDescriptions = new HashSet<>();

  @OneToMany(mappedBy = "theNewVersion")
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  private Set<Description> theTemplateNewDescriptions = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTemplateState() {
    return templateState;
  }

  public Template templateState(String templateState) {
    this.templateState = templateState;
    return this;
  }

  public void setTemplateState(String templateState) {
    this.templateState = templateState;
  }

  public Set<ProcessModel> getTheInstances() {
    return theInstances;
  }

  public Template theInstances(Set<ProcessModel> processModels) {
    this.theInstances = processModels;
    return this;
  }

  public Template addTheInstances(ProcessModel processModel) {
    this.theInstances.add(processModel);
    processModel.setTheOrigin(this);
    return this;
  }

  public Template removeTheInstances(ProcessModel processModel) {
    this.theInstances.remove(processModel);
    processModel.setTheOrigin(null);
    return this;
  }

  public void setTheInstances(Set<ProcessModel> processModels) {
    this.theInstances = processModels;
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

  public Set<Description> getTheDerivedVersionDescriptions() {
    return theDerivedVersionDescriptions;
  }

  public Template theDerivedVersionDescriptions(Set<Description> descriptions) {
    this.theDerivedVersionDescriptions = descriptions;
    return this;
  }

  public Template addTheDerivedVersionDescriptions(Description description) {
    this.theDerivedVersionDescriptions.add(description);
    description.setTheOldVersion(this);
    return this;
  }

  public Template removeTheDerivedVersionDescriptions(Description description) {
    this.theDerivedVersionDescriptions.remove(description);
    description.setTheOldVersion(null);
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

  public Template addTheTemplateNewDescriptions(Description description) {
    this.theTemplateNewDescriptions.add(description);
    description.setTheNewVersion(this);
    return this;
  }

  public Template removeTheTemplateNewDescriptions(Description description) {
    this.theTemplateNewDescriptions.remove(description);
    description.setTheNewVersion(null);
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
    return "Template{" + "id=" + getId() + ", templateState='" + getTemplateState() + "'" + "}";
  }
}
