package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Shareable} entity.
 */
public class ShareableDTO implements Serializable {

    private Long id;

    private String state;

    private String unitOfCost;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUnitOfCost() {
        return unitOfCost;
    }

    public void setUnitOfCost(String unitOfCost) {
        this.unitOfCost = unitOfCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShareableDTO shareableDTO = (ShareableDTO) o;
        if (shareableDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shareableDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShareableDTO{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", unitOfCost='" + getUnitOfCost() + "'" +
            "}";
    }
}
