package br.ufpa.labes.spm.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.NotWorkingDay} entity.
 */
public class NotWorkingDayDTO implements Serializable {

    private Long id;

    
    private String name;


    private Long calendarId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotWorkingDayDTO notWorkingDayDTO = (NotWorkingDayDTO) o;
        if (notWorkingDayDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notWorkingDayDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotWorkingDayDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", calendar=" + getCalendarId() +
            "}";
    }
}
