package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.CatalogEvent} entity.
 */
public class CatalogEventDTO implements Serializable {

    private Long id;

    @Lob
    private String description;


    private Long theResourceEventId;

    private Long theProcessModelEventId;

    private Long theAgendaEventId;

    private Long theCatalogEventId;

    private Long theConnectionEventId;

    private Long theGlobalActivityEventId;

    private Long theModelingActivityEventId;

    private Long theProcessEventId;

    private Long thePlainId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTheResourceEventId() {
        return theResourceEventId;
    }

    public void setTheResourceEventId(Long resourceEventId) {
        this.theResourceEventId = resourceEventId;
    }

    public Long getTheProcessModelEventId() {
        return theProcessModelEventId;
    }

    public void setTheProcessModelEventId(Long processModelEventId) {
        this.theProcessModelEventId = processModelEventId;
    }

    public Long getTheAgendaEventId() {
        return theAgendaEventId;
    }

    public void setTheAgendaEventId(Long agendaEventId) {
        this.theAgendaEventId = agendaEventId;
    }

    public Long getTheCatalogEventId() {
        return theCatalogEventId;
    }

    public void setTheCatalogEventId(Long catalogEventId) {
        this.theCatalogEventId = catalogEventId;
    }

    public Long getTheConnectionEventId() {
        return theConnectionEventId;
    }

    public void setTheConnectionEventId(Long connectionEventId) {
        this.theConnectionEventId = connectionEventId;
    }

    public Long getTheGlobalActivityEventId() {
        return theGlobalActivityEventId;
    }

    public void setTheGlobalActivityEventId(Long globalActivityEventId) {
        this.theGlobalActivityEventId = globalActivityEventId;
    }

    public Long getTheModelingActivityEventId() {
        return theModelingActivityEventId;
    }

    public void setTheModelingActivityEventId(Long modelingActivityEventId) {
        this.theModelingActivityEventId = modelingActivityEventId;
    }

    public Long getTheProcessEventId() {
        return theProcessEventId;
    }

    public void setTheProcessEventId(Long processEventId) {
        this.theProcessEventId = processEventId;
    }

    public Long getThePlainId() {
        return thePlainId;
    }

    public void setThePlainId(Long plainId) {
        this.thePlainId = plainId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CatalogEventDTO catalogEventDTO = (CatalogEventDTO) o;
        if (catalogEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), catalogEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CatalogEventDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", theResourceEvent=" + getTheResourceEventId() +
            ", theProcessModelEvent=" + getTheProcessModelEventId() +
            ", theAgendaEvent=" + getTheAgendaEventId() +
            ", theCatalogEvent=" + getTheCatalogEventId() +
            ", theConnectionEvent=" + getTheConnectionEventId() +
            ", theGlobalActivityEvent=" + getTheGlobalActivityEventId() +
            ", theModelingActivityEvent=" + getTheModelingActivityEventId() +
            ", theProcessEvent=" + getTheProcessEventId() +
            ", thePlain=" + getThePlainId() +
            "}";
    }
}
