package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A DevelopingSystem.
 */
@Entity
@Table(name = "developing_system")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DevelopingSystem implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties("theSystems")
    private Company theOrganization;

    @OneToMany(mappedBy = "theSystem")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Project> theProjects = new HashSet<>();

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

    public DevelopingSystem ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public DevelopingSystem name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public DevelopingSystem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Company getTheOrganization() {
        return theOrganization;
    }

    public DevelopingSystem theOrganization(Company company) {
        this.theOrganization = company;
        return this;
    }

    public void setTheOrganization(Company company) {
        this.theOrganization = company;
    }

    public Set<Project> getTheProjects() {
        return theProjects;
    }

    public DevelopingSystem theProjects(Set<Project> projects) {
        this.theProjects = projects;
        return this;
    }

    public DevelopingSystem addTheProject(Project project) {
        this.theProjects.add(project);
        project.setTheSystem(this);
        return this;
    }

    public DevelopingSystem removeTheProject(Project project) {
        this.theProjects.remove(project);
        project.setTheSystem(null);
        return this;
    }

    public void setTheProjects(Set<Project> projects) {
        this.theProjects = projects;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DevelopingSystem)) {
            return false;
        }
        return id != null && id.equals(((DevelopingSystem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DevelopingSystem{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
