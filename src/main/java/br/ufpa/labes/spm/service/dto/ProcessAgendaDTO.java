package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ProcessAgenda} entity.
 */
public class ProcessAgendaDTO implements Serializable {

    private Long id;

    private tasks;

    private Long theTaskAgendaId;

    private Long theProcessId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheTaskAgendaId() {
        return theTaskAgendaId;
    }

    public void setTheTaskAgendaId(Long taskAgendaId) {
        this.theTaskAgendaId = taskAgendaId;
    }

    public Long getTheProcessId() {
        return theProcessId;
    }

    public void setTheProcessId(Long processId) {
        this.theProcessId = processId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProcessAgendaDTO processAgendaDTO = (ProcessAgendaDTO) o;
        if (processAgendaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), processAgendaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcessAgendaDTO{" +
            "id=" + getId() +
            ", theTaskAgenda=" + getTheTaskAgendaId() +
            ", theProcess=" + getTheProcessId() +
            "}";
    }
}
