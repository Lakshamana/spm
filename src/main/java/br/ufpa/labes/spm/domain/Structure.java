package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Structure.
 */
@Entity
@Table(name = "structure")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Structure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("theStructures")
    private Node rootElement;

    @OneToMany(mappedBy = "theStructure")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<VCSRepository> theVCSRepositories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Node getRootElement() {
        return rootElement;
    }

    public Structure rootElement(Node node) {
        this.rootElement = node;
        return this;
    }

    public void setRootElement(Node node) {
        this.rootElement = node;
    }

    public Set<VCSRepository> getTheVCSRepositories() {
        return theVCSRepositories;
    }

    public Structure theVCSRepositories(Set<VCSRepository> vCSRepositories) {
        this.theVCSRepositories = vCSRepositories;
        return this;
    }

    public Structure addTheVCSRepository(VCSRepository vCSRepository) {
        this.theVCSRepositories.add(vCSRepository);
        vCSRepository.setTheStructure(this);
        return this;
    }

    public Structure removeTheVCSRepository(VCSRepository vCSRepository) {
        this.theVCSRepositories.remove(vCSRepository);
        vCSRepository.setTheStructure(null);
        return this;
    }

    public void setTheVCSRepositories(Set<VCSRepository> vCSRepositories) {
        this.theVCSRepositories = vCSRepositories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Structure)) {
            return false;
        }
        return id != null && id.equals(((Structure) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Structure{" +
            "id=" + getId() +
            "}";
    }
}
