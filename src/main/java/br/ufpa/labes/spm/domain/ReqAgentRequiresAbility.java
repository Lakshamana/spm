package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ReqAgentRequiresAbility.
 */
@Entity
@Table(name = "req_agent_requires_ability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReqAgentRequiresAbility implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "degree")
    private Integer degree;

    @ManyToOne
    @JsonIgnoreProperties("theReqAgentRequiresAbilities")
    private ReqAgent theReqAgent;

    @ManyToOne
    @JsonIgnoreProperties("theReqAgentRequiresAbilities")
    private Ability theAbility;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDegree() {
        return degree;
    }

    public ReqAgentRequiresAbility degree(Integer degree) {
        this.degree = degree;
        return this;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public ReqAgent getTheReqAgent() {
        return theReqAgent;
    }

    public ReqAgentRequiresAbility theReqAgent(ReqAgent reqAgent) {
        this.theReqAgent = reqAgent;
        return this;
    }

    public void setTheReqAgent(ReqAgent reqAgent) {
        this.theReqAgent = reqAgent;
    }

    public Ability getTheAbility() {
        return theAbility;
    }

    public ReqAgentRequiresAbility theAbility(Ability ability) {
        this.theAbility = ability;
        return this;
    }

    public void setTheAbility(Ability ability) {
        this.theAbility = ability;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReqAgentRequiresAbility)) {
            return false;
        }
        return id != null && id.equals(((ReqAgentRequiresAbility) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ReqAgentRequiresAbility{" +
            "id=" + getId() +
            ", degree=" + getDegree() +
            "}";
    }

    public void removeFromTheAbility(){
      if (this.theAbility!=null){
        this.theAbility.removeTheReqAgentRequiresAbility(this);
      }
    }

    public void insertIntoTheAbility(Ability theAbility){
      theAbility.addTheReqAgentRequiresAbility(this);
    }

    public void removeFromTheReqAgent(){
      if (this.theReqAgent!=null){
        this.theReqAgent.removeTheReqAgentRequiresAbility(this);
      }
    }

    public void insertIntoTheReqAgent(ReqAgent theReqAgent){
      theReqAgent.addTheReqAgentRequiresAbility(this);
    }


}
