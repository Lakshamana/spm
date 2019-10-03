package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ReqAgentRequiresAbility} entity.
 */
public class ReqAgentRequiresAbilityDTO implements Serializable {

    private Long id;

    private Integer degree;


    private Long theReqAgentId;

    private Long theAbilityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public Long getTheReqAgentId() {
        return theReqAgentId;
    }

    public void setTheReqAgentId(Long reqAgentId) {
        this.theReqAgentId = reqAgentId;
    }

    public Long getTheAbilityId() {
        return theAbilityId;
    }

    public void setTheAbilityId(Long abilityId) {
        this.theAbilityId = abilityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO = (ReqAgentRequiresAbilityDTO) o;
        if (reqAgentRequiresAbilityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reqAgentRequiresAbilityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReqAgentRequiresAbilityDTO{" +
            "id=" + getId() +
            ", degree=" + getDegree() +
            ", theReqAgent=" + getTheReqAgentId() +
            ", theAbility=" + getTheAbilityId() +
            "}";
    }
}
