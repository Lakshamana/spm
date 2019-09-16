package br.ufpa.labes.spm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/** A GraphicCoordinate. */
@Entity
@Table(name = "graphic_coordinate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GraphicCoordinate implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "x")
  private Integer x;

  @Column(name = "y")
  private Integer y;

  @Column(name = "visible")
  private Boolean visible;

  @Column(name = "the_process")
  private String theProcess;

  @OneToOne(mappedBy = "theGraphicCoordinate")
  @JsonIgnore
  private WebAPSEEObject theObjectReference;

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getX() {
    return x;
  }

  public GraphicCoordinate x(Integer x) {
    this.x = x;
    return this;
  }

  public void setX(Integer x) {
    this.x = x;
  }

  public Integer getY() {
    return y;
  }

  public GraphicCoordinate y(Integer y) {
    this.y = y;
    return this;
  }

  public void setY(Integer y) {
    this.y = y;
  }

  public Boolean isVisible() {
    return visible;
  }

  public GraphicCoordinate visible(Boolean visible) {
    this.visible = visible;
    return this;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }

  public String getTheProcess() {
    return theProcess;
  }

  public GraphicCoordinate theProcess(String theProcess) {
    this.theProcess = theProcess;
    return this;
  }

  public void setTheProcess(String theProcess) {
    this.theProcess = theProcess;
  }

  public WebAPSEEObject getTheObjectReference() {
    return theObjectReference;
  }

  public GraphicCoordinate theObjectReference(WebAPSEEObject webAPSEEObject) {
    this.theObjectReference = webAPSEEObject;
    return this;
  }

  public void setTheObjectReference(WebAPSEEObject webAPSEEObject) {
    this.theObjectReference = webAPSEEObject;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof GraphicCoordinate)) {
      return false;
    }
    return id != null && id.equals(((GraphicCoordinate) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "GraphicCoordinate{"
        + "id="
        + getId()
        + ", x="
        + getX()
        + ", y="
        + getY()
        + ", visible='"
        + isVisible()
        + "'"
        + ", theProcess='"
        + getTheProcess()
        + "'"
        + "}";
  }
}
