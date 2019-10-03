package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ProcessEvent} entity.
 */
public class ProcessEventDTO implements Serializable {

    private Long id;


    private Long theCatalogEventId;

    private Long theProcessId;

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

    public Long getTheProcessId() {
        return theProcessId;
    }

    public void setTheProcessId(Long processId) {
        this.theProcessId = processId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProcessEventDTO processEventDTO = (ProcessEventDTO) o;
        if (processEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), processEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcessEventDTO{" +
            "id=" + getId() +
            ", theCatalogEvent=" + getTheCatalogEventId() +
            ", theProcess=" + getTheProcessId() +
            "}";
    }
}
