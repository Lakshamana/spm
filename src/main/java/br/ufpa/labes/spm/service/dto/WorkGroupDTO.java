package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.WorkGroup} entity.
 */
public class WorkGroupDTO implements Serializable {

    private Long id;

    private String ident;

    private String name;

    @Lob
    private String description;

    private Boolean isActive;


    private Long theWorkGroupTypeId;

    private Long superWorkGroupId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getTheWorkGroupTypeId() {
        return theWorkGroupTypeId;
    }

    public void setTheWorkGroupTypeId(Long workGroupTypeId) {
        this.theWorkGroupTypeId = workGroupTypeId;
    }

    public Long getSuperWorkGroupId() {
        return superWorkGroupId;
    }

    public void setSuperWorkGroupId(Long workGroupId) {
        this.superWorkGroupId = workGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WorkGroupDTO workGroupDTO = (WorkGroupDTO) o;
        if (workGroupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), workGroupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WorkGroupDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", theWorkGroupType=" + getTheWorkGroupTypeId() +
            ", superWorkGroup=" + getSuperWorkGroupId() +
            "}";
    }
}
