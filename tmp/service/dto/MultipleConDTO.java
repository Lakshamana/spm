package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.MultipleCon} entity.
 */
public class MultipleConDTO implements Serializable {

    private Long id;

    private Boolean fired;


    private Long theDependencyId;

    private Long theDependencyToMultipleConsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isFired() {
        return fired;
    }

    public void setFired(Boolean fired) {
        this.fired = fired;
    }

    public Long getTheDependencyId() {
        return theDependencyId;
    }

    public void setTheDependencyId(Long dependencyId) {
        this.theDependencyId = dependencyId;
    }

    public Long getTheDependencyToMultipleConsId() {
        return theDependencyToMultipleConsId;
    }

    public void setTheDependencyToMultipleConsId(Long dependencyId) {
        this.theDependencyToMultipleConsId = dependencyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MultipleConDTO multipleConDTO = (MultipleConDTO) o;
        if (multipleConDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), multipleConDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MultipleConDTO{" +
            "id=" + getId() +
            ", fired='" + isFired() + "'" +
            ", theDependency=" + getTheDependencyId() +
            ", theDependencyToMultipleCons=" + getTheDependencyToMultipleConsId() +
            "}";
    }
}
