package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A InvolvedArtifact. */
@Entity
@Table(name = "involved_artifact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class InvolvedArtifact implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JsonIgnoreProperties("involvedArtifactToNormals")
  private Normal inInvolvedArtifacts;

  @ManyToOne
  @JsonIgnoreProperties("involvedArtifactFromNormals")
  private Normal outInvolvedArtifacts;

  @ManyToOne
  @JsonIgnoreProperties("theInvolvedArtifacts")
  private ArtifactType theArtifactType;

  @ManyToOne
  @JsonIgnoreProperties("theInvolvedArtifacts")
  private Artifact theArtifact;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Normal getInInvolvedArtifacts() {
    return inInvolvedArtifacts;
  }

  public InvolvedArtifact inInvolvedArtifacts(Normal normal) {
    this.inInvolvedArtifacts = normal;
    return this;
  }

  public void setInInvolvedArtifacts(Normal normal) {
    this.inInvolvedArtifacts = normal;
  }

  public Normal getOutInvolvedArtifacts() {
    return outInvolvedArtifacts;
  }

  public InvolvedArtifact outInvolvedArtifacts(Normal normal) {
    this.outInvolvedArtifacts = normal;
    return this;
  }

  public void setOutInvolvedArtifacts(Normal normal) {
    this.outInvolvedArtifacts = normal;
  }

  public ArtifactType getTheArtifactType() {
    return theArtifactType;
  }

  public InvolvedArtifact theArtifactType(ArtifactType artifactType) {
    this.theArtifactType = artifactType;
    return this;
  }

  public void setTheArtifactType(ArtifactType artifactType) {
    this.theArtifactType = artifactType;
  }

  public Artifact getTheArtifact() {
    return theArtifact;
  }

  public InvolvedArtifact theArtifact(Artifact artifact) {
    this.theArtifact = artifact;
    return this;
  }

  public void setTheArtifact(Artifact artifact) {
    this.theArtifact = artifact;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof InvolvedArtifact)) {
      return false;
    }
    return id != null && id.equals(((InvolvedArtifact) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "InvolvedArtifact{" + "id=" + getId() + "}";
  }

  public void removeFromTheArtifacts() {
    if (this.theArtifact != null) {
      this.theArtifact.removeTheInvolvedArtifacts(this);
    }
  }

  public void insertIntoTheArtifacts(Artifact theArtifact) {
    theArtifact.addTheInvolvedArtifacts(this);
  }
}
