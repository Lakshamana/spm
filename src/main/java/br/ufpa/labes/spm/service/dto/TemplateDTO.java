package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Template} entity.
 */
public class TemplateDTO implements Serializable {

    private Long id;

    private String templateState;


    private Long theOriginalVersionDescriptionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateState() {
        return templateState;
    }

    public void setTemplateState(String templateState) {
        this.templateState = templateState;
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
            ", templateState='" + getTemplateState() + "'" +
            ", theOriginalVersionDescription=" + getTheOriginalVersionDescriptionId() +
            "}";
    }
}
