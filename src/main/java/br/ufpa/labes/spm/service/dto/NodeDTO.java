package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Node} entity.
 */
public class NodeDTO implements Serializable {

    private Long id;

    private String ident;

    private String data;

    private String serviceFileId;


    private Long theNodeId;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getServiceFileId() {
        return serviceFileId;
    }

    public void setServiceFileId(String serviceFileId) {
        this.serviceFileId = serviceFileId;
    }

    public Long getTheNodeId() {
        return theNodeId;
    }

    public void setTheNodeId(Long nodeId) {
        this.theNodeId = nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NodeDTO nodeDTO = (NodeDTO) o;
        if (nodeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), nodeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NodeDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", data='" + getData() + "'" +
            ", serviceFileId='" + getServiceFileId() + "'" +
            ", theNode=" + getTheNodeId() +
            "}";
    }
}
