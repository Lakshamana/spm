package br.ufpa.labes.spm.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import br.ufpa.labes.spm.domain.enumeration.WebAPSEEObjectType;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.WebAPSEEObject} entity.
 */
public class WebAPSEEObjectDTO implements Serializable {

    private Long id;

    @NotNull
    private Long theReferredOid;

    @NotNull
    private String className;

    private WebAPSEEObjectType webAPSEEObjectType;


    private Long theGraphicCoordinateId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheReferredOid() {
        return theReferredOid;
    }

    public void setTheReferredOid(Long theReferredOid) {
        this.theReferredOid = theReferredOid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public WebAPSEEObjectType getWebAPSEEObjectType() {
        return webAPSEEObjectType;
    }

    public void setWebAPSEEObjectType(WebAPSEEObjectType webAPSEEObjectType) {
        this.webAPSEEObjectType = webAPSEEObjectType;
    }

    public Long getTheGraphicCoordinateId() {
        return theGraphicCoordinateId;
    }

    public void setTheGraphicCoordinateId(Long graphicCoordinateId) {
        this.theGraphicCoordinateId = graphicCoordinateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WebAPSEEObjectDTO webAPSEEObjectDTO = (WebAPSEEObjectDTO) o;
        if (webAPSEEObjectDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), webAPSEEObjectDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WebAPSEEObjectDTO{" +
            "id=" + getId() +
            ", theReferredOid=" + getTheReferredOid() +
            ", className='" + getClassName() + "'" +
            ", webAPSEEObjectType='" + getWebAPSEEObjectType() + "'" +
            ", theGraphicCoordinate=" + getTheGraphicCoordinateId() +
            "}";
    }
}
