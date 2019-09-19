package br.ufpa.labes.spm.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Task} entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    private String localState;

    private LocalDate beginDate;

    private LocalDate endDate;

    private Float workingHours;

    private LocalDate dateDelegatedTo;

    private LocalDate dateDelegatedFrom;


    private Long theProcessAgendaId;

    private Long delegatedFromId;

    private Long delegatedToId;

    private Long theNormalId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalState() {
        return localState;
    }

    public void setLocalState(String localState) {
        this.localState = localState;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Float getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Float workingHours) {
        this.workingHours = workingHours;
    }

    public LocalDate getDateDelegatedTo() {
        return dateDelegatedTo;
    }

    public void setDateDelegatedTo(LocalDate dateDelegatedTo) {
        this.dateDelegatedTo = dateDelegatedTo;
    }

    public LocalDate getDateDelegatedFrom() {
        return dateDelegatedFrom;
    }

    public void setDateDelegatedFrom(LocalDate dateDelegatedFrom) {
        this.dateDelegatedFrom = dateDelegatedFrom;
    }

    public Long getTheProcessAgendaId() {
        return theProcessAgendaId;
    }

    public void setTheProcessAgendaId(Long processAgendaId) {
        this.theProcessAgendaId = processAgendaId;
    }

    public Long getDelegatedFromId() {
        return delegatedFromId;
    }

    public void setDelegatedFromId(Long agentId) {
        this.delegatedFromId = agentId;
    }

    public Long getDelegatedToId() {
        return delegatedToId;
    }

    public void setDelegatedToId(Long agentId) {
        this.delegatedToId = agentId;
    }

    public Long getTheNormalId() {
        return theNormalId;
    }

    public void setTheNormalId(Long normalId) {
        this.theNormalId = normalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;
        if (taskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + getId() +
            ", localState='" + getLocalState() + "'" +
            ", beginDate='" + getBeginDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", workingHours=" + getWorkingHours() +
            ", dateDelegatedTo='" + getDateDelegatedTo() + "'" +
            ", dateDelegatedFrom='" + getDateDelegatedFrom() + "'" +
            ", theProcessAgenda=" + getTheProcessAgendaId() +
            ", delegatedFrom=" + getDelegatedFromId() +
            ", delegatedTo=" + getDelegatedToId() +
            ", theNormal=" + getTheNormalId() +
            "}";
    }
}
