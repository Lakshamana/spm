package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A MultipleCon.
 */
@Entity
@Table(name = "multiple_con")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MultipleCon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fired")
    private Boolean fired;

    @OneToOne
    @JoinColumn(unique = true)
    private JoinCon theJoinConSub;

    @OneToOne
    @JoinColumn(unique = true)
    private BranchCon theBranchConSub;

    @ManyToOne
    @JsonIgnoreProperties("theMultipleCons")
    private Dependency theDependency;

    @OneToOne(mappedBy = "theMultipleConSub")
    @JsonIgnore
    private Connection theConnectionSuper;

    @OneToMany(mappedBy = "fromMultipleCon")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BranchCon> toBranchCons = new HashSet<>();

    @ManyToMany(mappedBy = "toMultipleCons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ArtifactCon> theArtifactCons = new HashSet<>();

    @ManyToMany(mappedBy = "toMultipleCons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<BranchANDCon> fromMultipleCons = new HashSet<>();

    @ManyToMany(mappedBy = "fromMultipleCons")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<JoinCon> theJoinCons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isFired() {
        return fired;
    }

    public MultipleCon fired(Boolean fired) {
        this.fired = fired;
        return this;
    }

    public void setFired(Boolean fired) {
        this.fired = fired;
    }

    public JoinCon getTheJoinConSub() {
        return theJoinConSub;
    }

    public MultipleCon theJoinConSub(JoinCon joinCon) {
        this.theJoinConSub = joinCon;
        return this;
    }

    public void setTheJoinConSub(JoinCon joinCon) {
        this.theJoinConSub = joinCon;
    }

    public BranchCon getTheBranchConSub() {
        return theBranchConSub;
    }

    public MultipleCon theBranchConSub(BranchCon branchCon) {
        this.theBranchConSub = branchCon;
        return this;
    }

    public void setTheBranchConSub(BranchCon branchCon) {
        this.theBranchConSub = branchCon;
    }

    public Dependency getTheDependency() {
        return theDependency;
    }

    public MultipleCon theDependency(Dependency dependency) {
        this.theDependency = dependency;
        return this;
    }

    public void setTheDependency(Dependency dependency) {
        this.theDependency = dependency;
    }

    public Connection getTheConnectionSuper() {
        return theConnectionSuper;
    }

    public MultipleCon theConnectionSuper(Connection connection) {
        this.theConnectionSuper = connection;
        return this;
    }

    public void setTheConnectionSuper(Connection connection) {
        this.theConnectionSuper = connection;
    }

    public Set<BranchCon> getToBranchCons() {
        return toBranchCons;
    }

    public MultipleCon toBranchCons(Set<BranchCon> branchCons) {
        this.toBranchCons = branchCons;
        return this;
    }

    public MultipleCon addToBranchCon(BranchCon branchCon) {
        this.toBranchCons.add(branchCon);
        branchCon.setFromMultipleCon(this);
        return this;
    }

    public MultipleCon removeToBranchCon(BranchCon branchCon) {
        this.toBranchCons.remove(branchCon);
        branchCon.setFromMultipleCon(null);
        return this;
    }

    public void setToBranchCons(Set<BranchCon> branchCons) {
        this.toBranchCons = branchCons;
    }

    public Set<ArtifactCon> getTheArtifactCons() {
        return theArtifactCons;
    }

    public MultipleCon theArtifactCons(Set<ArtifactCon> artifactCons) {
        this.theArtifactCons = artifactCons;
        return this;
    }

    public MultipleCon addTheArtifactCon(ArtifactCon artifactCon) {
        this.theArtifactCons.add(artifactCon);
        artifactCon.getToMultipleCons().add(this);
        return this;
    }

    public MultipleCon removeTheArtifactCon(ArtifactCon artifactCon) {
        this.theArtifactCons.remove(artifactCon);
        artifactCon.getToMultipleCons().remove(this);
        return this;
    }

    public void setTheArtifactCons(Set<ArtifactCon> artifactCons) {
        this.theArtifactCons = artifactCons;
    }

    public Set<BranchANDCon> getFromMultipleCons() {
        return fromMultipleCons;
    }

    public MultipleCon fromMultipleCons(Set<BranchANDCon> branchANDCons) {
        this.fromMultipleCons = branchANDCons;
        return this;
    }

    public MultipleCon addFromMultipleCon(BranchANDCon branchANDCon) {
        this.fromMultipleCons.add(branchANDCon);
        branchANDCon.getToMultipleCons().add(this);
        return this;
    }

    public MultipleCon removeFromMultipleCon(BranchANDCon branchANDCon) {
        this.fromMultipleCons.remove(branchANDCon);
        branchANDCon.getToMultipleCons().remove(this);
        return this;
    }

    public void setFromMultipleCons(Set<BranchANDCon> branchANDCons) {
        this.fromMultipleCons = branchANDCons;
    }

    public Set<JoinCon> getTheJoinCons() {
        return theJoinCons;
    }

    public MultipleCon theJoinCons(Set<JoinCon> joinCons) {
        this.theJoinCons = joinCons;
        return this;
    }

    public MultipleCon addTheJoinCon(JoinCon joinCon) {
        this.theJoinCons.add(joinCon);
        joinCon.getFromMultipleCons().add(this);
        return this;
    }

    public MultipleCon removeTheJoinCon(JoinCon joinCon) {
        this.theJoinCons.remove(joinCon);
        joinCon.getFromMultipleCons().remove(this);
        return this;
    }

    public void setTheJoinCons(Set<JoinCon> joinCons) {
        this.theJoinCons = joinCons;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MultipleCon)) {
            return false;
        }
        return id != null && id.equals(((MultipleCon) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MultipleCon{" +
            "id=" + getId() +
            ", fired='" + isFired() + "'" +
            "}";
    }
}
