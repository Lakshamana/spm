package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import br.ufpa.labes.spm.domain.enumeration.TemplateStatus;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Template} entity.
 */
public class TemplateDTO implements Serializable {

    private Long id;

    private TemplateStatus tStatus;


    private Long theOriginalVersionDescriptionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TemplateStatus gettStatus() {
        return tStatus;
    }

    public void settStatus(TemplateStatus tStatus) {
        this.tStatus = tStatus;
    }

    public Long getTheOriginalVersionDescriptionId() {
        return theOriginalVersionDescriptionId;
    }

    public void setTheOriginalVersionDescriptionId(Long descriptionId) {
        this.theOriginalVersionDescriptionId = descriptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TemplateDTO templateDTO = (TemplateDTO) o;
        if (templateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), templateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TemplateDTO{" +
            "id=" + getId() +
            ", tStatus='" + gettStatus() + "'" +
            ", theOriginalVersionDescription=" + getTheOriginalVersionDescriptionId() +
            "}";
    }
}
