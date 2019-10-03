package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Exclusive} entity.
 */
public class ExclusiveDTO implements Serializable {

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

        ExclusiveDTO exclusiveDTO = (ExclusiveDTO) o;
        if (exclusiveDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exclusiveDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExclusiveDTO{" +
            "id=" + getId() +
            ", state='" + getState() + "'" +
            ", unitOfCost='" + getUnitOfCost() + "'" +
            "}";
    }
}
