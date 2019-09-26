package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role implements Serializable {

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

    @OneToMany(mappedBy = "theRole")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReqAgent> theReqAgents = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("commands")
    private Role subordinate;

    @ManyToOne
    @JsonIgnoreProperties("theRoles")
    private RoleType theRoleType;

    @OneToMany(mappedBy = "theRole")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentPlaysRole> theAgentPlaysRoles = new HashSet<>();

    @OneToMany(mappedBy = "subordinate")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Role> commands = new HashSet<>();

    @OneToMany(mappedBy = "theRole")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RoleNeedsAbility> theRoleNeedsAbilities = new HashSet<>();

    @OneToMany(mappedBy = "theRole")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentInstSug> theAgentInstSugs = new HashSet<>();

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

    public Role ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public Role name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Role description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ReqAgent> getTheReqAgents() {
        return theReqAgents;
    }

    public Role theReqAgents(Set<ReqAgent> reqAgents) {
        this.theReqAgents = reqAgents;
        return this;
    }

    public Role addTheReqAgent(ReqAgent reqAgent) {
        this.theReqAgents.add(reqAgent);
        reqAgent.setTheRole(this);
        return this;
    }

    public Role removeTheReqAgent(ReqAgent reqAgent) {
        this.theReqAgents.remove(reqAgent);
        reqAgent.setTheRole(null);
        return this;
    }

    public void setTheReqAgents(Set<ReqAgent> reqAgents) {
        this.theReqAgents = reqAgents;
    }

    public Role getSubordinate() {
        return subordinate;
    }

    public Role subordinate(Role role) {
        this.subordinate = role;
        return this;
    }

    public void setSubordinate(Role role) {
        this.subordinate = role;
    }

    public RoleType getTheRoleType() {
        return theRoleType;
    }

    public Role theRoleType(RoleType roleType) {
        this.theRoleType = roleType;
        return this;
    }

    public void setTheRoleType(RoleType roleType) {
        this.theRoleType = roleType;
    }

    public Set<AgentPlaysRole> getTheAgentPlaysRoles() {
        return theAgentPlaysRoles;
    }

    public Role theAgentPlaysRoles(Set<AgentPlaysRole> agentPlaysRoles) {
        this.theAgentPlaysRoles = agentPlaysRoles;
        return this;
    }

    public Role addTheAgentPlaysRole(AgentPlaysRole agentPlaysRole) {
        this.theAgentPlaysRoles.add(agentPlaysRole);
        agentPlaysRole.setTheRole(this);
        return this;
    }

    public Role removeTheAgentPlaysRole(AgentPlaysRole agentPlaysRole) {
        this.theAgentPlaysRoles.remove(agentPlaysRole);
        agentPlaysRole.setTheRole(null);
        return this;
    }

    public void setTheAgentPlaysRoles(Set<AgentPlaysRole> agentPlaysRoles) {
        this.theAgentPlaysRoles = agentPlaysRoles;
    }

    public Set<Role> getCommands() {
        return commands;
    }

    public Role commands(Set<Role> roles) {
        this.commands = roles;
        return this;
    }

    public Role addCommands(Role role) {
        this.commands.add(role);
        role.setSubordinate(this);
        return this;
    }

    public Role removeCommands(Role role) {
        this.commands.remove(role);
        role.setSubordinate(null);
        return this;
    }

    public void setCommands(Set<Role> roles) {
        this.commands = roles;
    }

    public Set<RoleNeedsAbility> getTheRoleNeedsAbilities() {
        return theRoleNeedsAbilities;
    }

    public Role theRoleNeedsAbilities(Set<RoleNeedsAbility> roleNeedsAbilities) {
        this.theRoleNeedsAbilities = roleNeedsAbilities;
        return this;
    }

    public Role addTheRoleNeedsAbility(RoleNeedsAbility roleNeedsAbility) {
        this.theRoleNeedsAbilities.add(roleNeedsAbility);
        roleNeedsAbility.setTheRole(this);
        return this;
    }

    public Role removeTheRoleNeedsAbility(RoleNeedsAbility roleNeedsAbility) {
        this.theRoleNeedsAbilities.remove(roleNeedsAbility);
        roleNeedsAbility.setTheRole(null);
        return this;
    }

    public void setTheRoleNeedsAbilities(Set<RoleNeedsAbility> roleNeedsAbilities) {
        this.theRoleNeedsAbilities = roleNeedsAbilities;
    }

    public Set<AgentInstSug> getTheAgentInstSugs() {
        return theAgentInstSugs;
    }

    public Role theAgentInstSugs(Set<AgentInstSug> agentInstSugs) {
        this.theAgentInstSugs = agentInstSugs;
        return this;
    }

    public Role addTheAgentInstSug(AgentInstSug agentInstSug) {
        this.theAgentInstSugs.add(agentInstSug);
        agentInstSug.setTheRole(this);
        return this;
    }

    public Role removeTheAgentInstSug(AgentInstSug agentInstSug) {
        this.theAgentInstSugs.remove(agentInstSug);
        agentInstSug.setTheRole(null);
        return this;
    }

    public void setTheAgentInstSugs(Set<AgentInstSug> agentInstSugs) {
        this.theAgentInstSugs = agentInstSugs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
