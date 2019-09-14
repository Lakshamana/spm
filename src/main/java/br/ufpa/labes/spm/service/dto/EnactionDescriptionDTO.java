package br.ufpa.labes.spm.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.EnactionDescription} entity.
 */
public class EnactionDescriptionDTO implements Serializable {

    private Long id;

    private LocalDate actualBegin;

    private LocalDate actualEnd;

    private String state;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getActualBegin() {
        return actualBegin;
    }

    public void setActualBegin(LocalDate actualBegin) {
        this.actualBegin = actualBegin;
    }

    public LocalDate getActualEnd() {
        return actualEnd;
    }

    public void setActualEnd(LocalDate actualEnd) {
        this.actualEnd = actualEnd;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EnactionDescriptionDTO enactionDescriptionDTO = (EnactionDescriptionDTO) o;
        if (enactionDescriptionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), enactionDescriptionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EnactionDescriptionDTO{" +
            "id=" + getId() +
            ", actualBegin='" + getActualBegin() + "'" +
            ", actualEnd='" + getActualEnd() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
}
