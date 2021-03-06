package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ConnectionEvent} entity.
 */
public class ConnectionEventDTO implements Serializable {

    private Long id;


    private Long theCatalogEventId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConnectionEventDTO connectionEventDTO = (ConnectionEventDTO) o;
        if (connectionEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), connectionEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConnectionEventDTO{" +
            "id=" + getId() +
            ", theCatalogEvent=" + getTheCatalogEventId() +
            "}";
    }
}
