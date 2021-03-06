package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.AgendaEvent} entity.
 */
public class AgendaEventDTO implements Serializable {

    private Long id;


    private Long theCatalogEventId;

    private Long theTaskId;

    private Long theNormalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheCatalogEventId() {
        return theCatalogEventId;
    }

    public void setTheCatalogEventId(Long catalogEventId) {
        this.theCatalogEventId = catalogEventId;
    }

    public Long getTheTaskId() {
        return theTaskId;
    }

    public void setTheTaskId(Long taskId) {
        this.theTaskId = taskId;
    }

    public Long getTheNormalId() {
        return theNormalId;
    }

    public void setTheNormalId(Long normalId) {
        this.theNormalId = normalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AgendaEventDTO agendaEventDTO = (AgendaEventDTO) o;
        if (agendaEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agendaEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AgendaEventDTO{" +
            "id=" + getId() +
            ", theCatalogEvent=" + getTheCatalogEventId() +
            ", theTask=" + getTheTaskId() +
            ", theNormal=" + getTheNormalId() +
            "}";
    }
}
