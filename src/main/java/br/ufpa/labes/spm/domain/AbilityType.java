package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AbilityType.
 */
@Entity
@Table(name = "ability_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AbilityType extends Type implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "theAbilityTypeSub")
    @JsonIgnore
    private Type theTypeSuper;

    @OneToMany(mappedBy = "theAbilityType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Ability> theAbilities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getTheTypeSuper() {
        return theTypeSuper;
    }

    public AbilityType theTypeSuper(Type type) {
        this.theTypeSuper = type;
        return this;
    }

    public void setTheTypeSuper(Type type) {
        this.theTypeSuper = type;
    }

    public Set<Ability> getTheAbilities() {
        return theAbilities;
    }

    public AbilityType theAbilities(Set<Ability> abilities) {
        this.theAbilities = abilities;
        return this;
    }

    public AbilityType addTheAbility(Ability ability) {
        this.theAbilities.add(ability);
        ability.setTheAbilityType(this);
        return this;
    }

    public AbilityType removeTheAbility(Ability ability) {
        this.theAbilities.remove(ability);
        ability.setTheAbilityType(null);
        return this;
    }

    public void setTheAbilities(Set<Ability> abilities) {
        this.theAbilities = abilities;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbilityType)) {
            return false;
        }
        return id != null && id.equals(((AbilityType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AbilityType{" +
            "id=" + getId() +
            "}";
    }
}
