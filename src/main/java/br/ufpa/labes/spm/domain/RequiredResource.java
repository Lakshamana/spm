package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A RequiredResource. */
@Entity
@Table(name = "required_resource")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RequiredResource implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "amount_needed")
  private Float amountNeeded;

  @ManyToOne
  @JsonIgnoreProperties("theRequiredResources")
  private Normal theNormal;

  @ManyToOne
  @JsonIgnoreProperties("theRequiredResources")
  private ResourceType theResourceType;

  @ManyToOne
  @JsonIgnoreProperties("theRequiredResources")
  private Resource theResource;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getAmountNeeded() {
    return amountNeeded;
  }

  public RequiredResource amountNeeded(Float amountNeeded) {
    this.amountNeeded = amountNeeded;
    return this;
  }

  public void setAmountNeeded(Float amountNeeded) {
    this.amountNeeded = amountNeeded;
  }

  public Normal getTheNormal() {
    return theNormal;
  }

  public RequiredResource theNormal(Normal normal) {
    this.theNormal = normal;
    return this;
  }

  public void setTheNormal(Normal normal) {
    this.theNormal = normal;
  }

  public ResourceType getTheResourceType() {
    return theResourceType;
  }

  public RequiredResource theResourceType(ResourceType resourceType) {
    this.theResourceType = resourceType;
    return this;
  }

  public void setTheResourceType(ResourceType resourceType) {
    this.theResourceType = resourceType;
  }

  public Resource getTheResource() {
    return theResource;
  }

  public RequiredResource theResource(Resource resource) {
    this.theResource = resource;
    return this;
  }

  public void setTheResource(Resource resource) {
    this.theResource = resource;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RequiredResource)) {
      return false;
    }
    return id != null && id.equals(((RequiredResource) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "RequiredResource{" + "id=" + getId() + ", amountNeeded=" + getAmountNeeded() + "}";
  }
}
