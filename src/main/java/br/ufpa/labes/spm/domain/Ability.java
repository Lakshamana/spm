package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ability.
 */
@Entity
@Table(name = "ability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "theAbility")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReqAgentRequiresAbility> theReqAgentRequiresAbilities = new HashSet<>();

    @OneToMany(mappedBy = "theAbility")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentHasAbility> theAgentHasAbilities = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("theAbilities")
    private AbilityType theAbilityType;

    @OneToMany(mappedBy = "theAbility")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoleNeedsAbility> theRoleNeedsAbilities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public Ability ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public Ability name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Ability description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ReqAgentRequiresAbility> getTheReqAgentRequiresAbilities() {
        return theReqAgentRequiresAbilities;
    }

    public Ability theReqAgentRequiresAbilities(Set<ReqAgentRequiresAbility> reqAgentRequiresAbilities) {
        this.theReqAgentRequiresAbilities = reqAgentRequiresAbilities;
        return this;
    }

    public Ability addTheReqAgentRequiresAbility(ReqAgentRequiresAbility reqAgentRequiresAbility) {
        this.theReqAgentRequiresAbilities.add(reqAgentRequiresAbility);
        reqAgentRequiresAbility.setTheAbility(this);
        return this;
    }

    public Ability removeTheReqAgentRequiresAbility(ReqAgentRequiresAbility reqAgentRequiresAbility) {
        this.theReqAgentRequiresAbilities.remove(reqAgentRequiresAbility);
        reqAgentRequiresAbility.setTheAbility(null);
        return this;
    }

    public void setTheReqAgentRequiresAbilities(Set<ReqAgentRequiresAbility> reqAgentRequiresAbilities) {
        this.theReqAgentRequiresAbilities = reqAgentRequiresAbilities;
    }

    public Set<AgentHasAbility> getTheAgentHasAbilities() {
        return theAgentHasAbilities;
    }

    public Ability theAgentHasAbilities(Set<AgentHasAbility> agentHasAbilities) {
        this.theAgentHasAbilities = agentHasAbilities;
        return this;
    }

    public Ability addTheAgentHasAbility(AgentHasAbility agentHasAbility) {
        this.theAgentHasAbilities.add(agentHasAbility);
        agentHasAbility.setTheAbility(this);
        return this;
    }

    public Ability removeTheAgentHasAbility(AgentHasAbility agentHasAbility) {
        this.theAgentHasAbilities.remove(agentHasAbility);
        agentHasAbility.setTheAbility(null);
        return this;
    }

    public void setTheAgentHasAbilities(Set<AgentHasAbility> agentHasAbilities) {
        this.theAgentHasAbilities = agentHasAbilities;
    }

    public AbilityType getTheAbilityType() {
        return theAbilityType;
    }

    public Ability theAbilityType(AbilityType abilityType) {
        this.theAbilityType = abilityType;
        return this;
    }

    public void setTheAbilityType(AbilityType abilityType) {
        this.theAbilityType = abilityType;
    }

    public Set<RoleNeedsAbility> getTheRoleNeedsAbilities() {
        return theRoleNeedsAbilities;
    }

    public Ability theRoleNeedsAbilities(Set<RoleNeedsAbility> roleNeedsAbilities) {
        this.theRoleNeedsAbilities = roleNeedsAbilities;
        return this;
    }

    public Ability addTheRoleNeedsAbility(RoleNeedsAbility roleNeedsAbility) {
        this.theRoleNeedsAbilities.add(roleNeedsAbility);
        roleNeedsAbility.setTheAbility(this);
        return this;
    }

    public Ability removeTheRoleNeedsAbility(RoleNeedsAbility roleNeedsAbility) {
        this.theRoleNeedsAbilities.remove(roleNeedsAbility);
        roleNeedsAbility.setTheAbility(null);
        return this;
    }

    public void setTheRoleNeedsAbilities(Set<RoleNeedsAbility> roleNeedsAbilities) {
        this.theRoleNeedsAbilities = roleNeedsAbilities;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ability)) {
            return false;
        }
        return id != null && id.equals(((Ability) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ability{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
