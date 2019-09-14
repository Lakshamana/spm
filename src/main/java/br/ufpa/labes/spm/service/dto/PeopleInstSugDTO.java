package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.PeopleInstSug} entity.
 */
public class PeopleInstSugDTO implements Serializable {

    private Long id;


    private Long theAgentInstSugId;

    private Long theWorkGroupInstSugId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheAgentInstSugId() {
        return theAgentInstSugId;
    }

    public void setTheAgentInstSugId(Long agentInstSugId) {
        this.theAgentInstSugId = agentInstSugId;
    }

    public Long getTheWorkGroupInstSugId() {
        return theWorkGroupInstSugId;
    }

    public void setTheWorkGroupInstSugId(Long workGroupInstSugId) {
        this.theWorkGroupInstSugId = workGroupInstSugId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PeopleInstSugDTO peopleInstSugDTO = (PeopleInstSugDTO) o;
        if (peopleInstSugDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), peopleInstSugDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PeopleInstSugDTO{" +
            "id=" + getId() +
            ", theAgentInstSug=" + getTheAgentInstSugId() +
            ", theWorkGroupInstSug=" + getTheWorkGroupInstSugId() +
            "}";
    }
}
