package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.RoleNeedsAbility} entity.
 */
public class RoleNeedsAbilityDTO implements Serializable {

    private Long id;

    private Integer degree;


    private Long theRoleId;

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

    public Long getTheRoleId() {
        return theRoleId;
    }

    public void setTheRoleId(Long roleId) {
        this.theRoleId = roleId;
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

        RoleNeedsAbilityDTO roleNeedsAbilityDTO = (RoleNeedsAbilityDTO) o;
        if (roleNeedsAbilityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), roleNeedsAbilityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RoleNeedsAbilityDTO{" +
            "id=" + getId() +
            ", degree=" + getDegree() +
            ", theRole=" + getTheRoleId() +
            ", theAbility=" + getTheAbilityId() +
            "}";
    }
}
