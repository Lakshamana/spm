package br.ufpa.labes.spm.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Event} entity.
 */
public class EventDTO implements Serializable {

    private Long id;

    @Lob
    private String why;

    private LocalDate when;

    private Boolean isCreatedByApsee;


    private Long theLogId;

    private Long theEventTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWhy() {
        return why;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public LocalDate getWhen() {
        return when;
    }

    public void setWhen(LocalDate when) {
        this.when = when;
    }

    public Boolean isIsCreatedByApsee() {
        return isCreatedByApsee;
    }

    public void setIsCreatedByApsee(Boolean isCreatedByApsee) {
        this.isCreatedByApsee = isCreatedByApsee;
    }

    public Long getTheLogId() {
        return theLogId;
    }

    public void setTheLogId(Long spmLogId) {
        this.theLogId = spmLogId;
    }

    public Long getTheEventTypeId() {
        return theEventTypeId;
    }

    public void setTheEventTypeId(Long eventTypeId) {
        this.theEventTypeId = eventTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventDTO eventDTO = (EventDTO) o;
        if (eventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EventDTO{" +
            "id=" + getId() +
            ", why='" + getWhy() + "'" +
            ", when='" + getWhen() + "'" +
            ", isCreatedByApsee='" + isIsCreatedByApsee() + "'" +
            ", theLog=" + getTheLogId() +
            ", theEventType=" + getTheEventTypeId() +
            "}";
    }
}
