package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.AgentInstSug} entity.
 */
public class AgentInstSugDTO implements Serializable {

    private Long id;


    private Long theRoleId;

    private Long chosenAgentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheRoleId() {
        return theRoleId;
    }

    public void setTheRoleId(Long roleId) {
        this.theRoleId = roleId;
    }

    public Long getChosenAgentId() {
        return chosenAgentId;
    }

    public void setChosenAgentId(Long agentId) {
        this.chosenAgentId = agentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AgentInstSugDTO agentInstSugDTO = (AgentInstSugDTO) o;
        if (agentInstSugDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agentInstSugDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AgentInstSugDTO{" +
            "id=" + getId() +
            ", theRole=" + getTheRoleId() +
            ", chosenAgent=" + getChosenAgentId() +
            "}";
    }
}
