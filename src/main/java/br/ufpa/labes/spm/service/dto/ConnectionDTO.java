package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Connection} entity.
 */
public class ConnectionDTO implements Serializable {

    private Long id;

    private String ident;


    private Long theProcessModelId;

    private Long theConnectionTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Long getTheProcessModelId() {
        return theProcessModelId;
    }

    public void setTheProcessModelId(Long processModelId) {
        this.theProcessModelId = processModelId;
    }

    public Long getTheConnectionTypeId() {
        return theConnectionTypeId;
    }

    public void setTheConnectionTypeId(Long connectionTypeId) {
        this.theConnectionTypeId = connectionTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConnectionDTO connectionDTO = (ConnectionDTO) o;
        if (connectionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), connectionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConnectionDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", theProcessModel=" + getTheProcessModelId() +
            ", theConnectionType=" + getTheConnectionTypeId() +
            "}";
    }
}
