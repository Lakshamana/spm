package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Dependency} entity.
 */
public class DependencyDTO implements Serializable {

    private Long id;

    private String kindDep;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKindDep() {
        return kindDep;
    }

    public void setKindDep(String kindDep) {
        this.kindDep = kindDep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DependencyDTO dependencyDTO = (DependencyDTO) o;
        if (dependencyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dependencyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DependencyDTO{" +
            "id=" + getId() +
            ", kindDep='" + getKindDep() + "'" +
            "}";
    }
}
