package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.GlobalActivityEvent} entity.
 */
public class GlobalActivityEventDTO implements Serializable {

    private Long id;


    private Long theCatalogEventId;

    private Long thePlainId;

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

        GlobalActivityEventDTO globalActivityEventDTO = (GlobalActivityEventDTO) o;
        if (globalActivityEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), globalActivityEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GlobalActivityEventDTO{" +
            "id=" + getId() +
            ", theCatalogEvent=" + getTheCatalogEventId() +
            ", thePlain=" + getThePlainId() +
            "}";
    }
}
