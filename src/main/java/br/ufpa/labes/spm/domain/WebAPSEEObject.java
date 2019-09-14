package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import br.ufpa.labes.spm.domain.enumeration.WebAPSEEObjectType;

/**
 * A WebAPSEEObject.
 */
@Entity
@Table(name = "web_apsee_object")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WebAPSEEObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "the_referred_oid", nullable = false)
    private Long theReferredOid;

    @NotNull
    @Column(name = "class_name", nullable = false)
    private String className;

    @Enumerated(EnumType.STRING)
    @Column(name = "web_apsee_object_type")
    private WebAPSEEObjectType webAPSEEObjectType;

    @OneToOne
    @JoinColumn(unique = true)
    private GraphicCoordinate theGraphicCoordinate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheReferredOid() {
        return theReferredOid;
    }

    public WebAPSEEObject theReferredOid(Long theReferredOid) {
        this.theReferredOid = theReferredOid;
        return this;
    }

    public void setTheReferredOid(Long theReferredOid) {
        this.theReferredOid = theReferredOid;
    }

    public String getClassName() {
        return className;
    }

    public WebAPSEEObject className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public WebAPSEEObjectType getWebAPSEEObjectType() {
        return webAPSEEObjectType;
    }

    public WebAPSEEObject webAPSEEObjectType(WebAPSEEObjectType webAPSEEObjectType) {
        this.webAPSEEObjectType = webAPSEEObjectType;
        return this;
    }

    public void setWebAPSEEObjectType(WebAPSEEObjectType webAPSEEObjectType) {
        this.webAPSEEObjectType = webAPSEEObjectType;
    }

    public GraphicCoordinate getTheGraphicCoordinate() {
        return theGraphicCoordinate;
    }

    public WebAPSEEObject theGraphicCoordinate(GraphicCoordinate graphicCoordinate) {
        this.theGraphicCoordinate = graphicCoordinate;
        return this;
    }

    public void setTheGraphicCoordinate(GraphicCoordinate graphicCoordinate) {
        this.theGraphicCoordinate = graphicCoordinate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WebAPSEEObject)) {
            return false;
        }
        return id != null && id.equals(((WebAPSEEObject) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WebAPSEEObject{" +
            "id=" + getId() +
            ", theReferredOid=" + getTheReferredOid() +
            ", className='" + getClassName() + "'" +
            ", webAPSEEObjectType='" + getWebAPSEEObjectType() + "'" +
            "}";
    }
}
