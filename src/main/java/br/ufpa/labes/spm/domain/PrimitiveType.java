package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A PrimitiveType.
 */
@Entity
@Table(name = "primitive_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PrimitiveType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @OneToMany(mappedBy = "thePrimitiveType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ToolParameter> theToolParameters = new HashSet<>();

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

    public PrimitiveType ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public Set<ToolParameter> getTheToolParameters() {
        return theToolParameters;
    }

    public PrimitiveType theToolParameters(Set<ToolParameter> toolParameters) {
        this.theToolParameters = toolParameters;
        return this;
    }

    public PrimitiveType addTheToolParameter(ToolParameter toolParameter) {
        this.theToolParameters.add(toolParameter);
        toolParameter.setThePrimitiveType(this);
        return this;
    }

    public PrimitiveType removeTheToolParameter(ToolParameter toolParameter) {
        this.theToolParameters.remove(toolParameter);
        toolParameter.setThePrimitiveType(null);
        return this;
    }

    public void setTheToolParameters(Set<ToolParameter> toolParameters) {
        this.theToolParameters = toolParameters;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrimitiveType)) {
            return false;
        }
        return id != null && id.equals(((PrimitiveType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PrimitiveType{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            "}";
    }
}
