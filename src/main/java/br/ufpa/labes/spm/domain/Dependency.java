package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Dependency.
 */
@Entity
@Table(name = "dependency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dependency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kind_dep")
    private String kindDep;

    @OneToOne
    @JoinColumn(unique = true)
    private Sequence theSequence;

    @OneToOne(mappedBy = "theDependency")
    @JsonIgnore
    private MultipleCon theMultipleCon;

    @OneToMany(mappedBy = "theDependencyToMultipleCons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MultipleCon> theMultipleConsToDependencies = new HashSet<>();

    @OneToMany(mappedBy = "theDependencyToMultipleSequences")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sequence> theMultipleSequences = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKindDep() {
        return kindDep;
    }

    public Dependency kindDep(String kindDep) {
        this.kindDep = kindDep;
        return this;
    }

    public void setKindDep(String kindDep) {
        this.kindDep = kindDep;
    }

    public Sequence getTheSequence() {
        return theSequence;
    }

    public Dependency theSequence(Sequence sequence) {
        this.theSequence = sequence;
        return this;
    }

    public void setTheSequence(Sequence sequence) {
        this.theSequence = sequence;
    }

    public MultipleCon getTheMultipleCon() {
        return theMultipleCon;
    }

    public Dependency theMultipleCon(MultipleCon multipleCon) {
        this.theMultipleCon = multipleCon;
        return this;
    }

    public void setTheMultipleCon(MultipleCon multipleCon) {
        this.theMultipleCon = multipleCon;
    }

    public Set<MultipleCon> getTheMultipleConsToDependencies() {
        return theMultipleConsToDependencies;
    }

    public Dependency theMultipleConsToDependencies(Set<MultipleCon> multipleCons) {
        this.theMultipleConsToDependencies = multipleCons;
        return this;
    }

    public Dependency addTheMultipleConsToDependency(MultipleCon multipleCon) {
        this.theMultipleConsToDependencies.add(multipleCon);
        multipleCon.setTheDependencyToMultipleCons(this);
        return this;
    }

    public Dependency removeTheMultipleConsToDependency(MultipleCon multipleCon) {
        this.theMultipleConsToDependencies.remove(multipleCon);
        multipleCon.setTheDependencyToMultipleCons(null);
        return this;
    }

    public void setTheMultipleConsToDependencies(Set<MultipleCon> multipleCons) {
        this.theMultipleConsToDependencies = multipleCons;
    }

    public Set<Sequence> getTheMultipleSequences() {
        return theMultipleSequences;
    }

    public Dependency theMultipleSequences(Set<Sequence> sequences) {
        this.theMultipleSequences = sequences;
        return this;
    }

    public Dependency addTheMultipleSequences(Sequence sequence) {
        this.theMultipleSequences.add(sequence);
        sequence.setTheDependencyToMultipleSequences(this);
        return this;
    }

    public Dependency removeTheMultipleSequences(Sequence sequence) {
        this.theMultipleSequences.remove(sequence);
        sequence.setTheDependencyToMultipleSequences(null);
        return this;
    }

    public void setTheMultipleSequences(Set<Sequence> sequences) {
        this.theMultipleSequences = sequences;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dependency)) {
            return false;
        }
        return id != null && id.equals(((Dependency) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dependency{" +
            "id=" + getId() +
            ", kindDep='" + getKindDep() + "'" +
            "}";
    }
}
