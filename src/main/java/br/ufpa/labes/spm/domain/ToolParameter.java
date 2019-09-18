package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ToolParameter.
 */
@Entity
@Table(name = "tool_parameter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ToolParameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "separator_symbol")
    private String separatorSymbol;

    @ManyToOne
    @JsonIgnoreProperties("theToolParameters")
    private ArtifactType theArtifactType;

    @ManyToOne
    @JsonIgnoreProperties("theToolParameters")
    private Subroutine theSubroutine;

    @ManyToOne
    @JsonIgnoreProperties("theToolParameters")
    private PrimitiveType thePrimitiveType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public ToolParameter label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSeparatorSymbol() {
        return separatorSymbol;
    }

    public ToolParameter separatorSymbol(String separatorSymbol) {
        this.separatorSymbol = separatorSymbol;
        return this;
    }

    public void setSeparatorSymbol(String separatorSymbol) {
        this.separatorSymbol = separatorSymbol;
    }

    public ArtifactType getTheArtifactType() {
        return theArtifactType;
    }

    public ToolParameter theArtifactType(ArtifactType artifactType) {
        this.theArtifactType = artifactType;
        return this;
    }

    public void setTheArtifactType(ArtifactType artifactType) {
        this.theArtifactType = artifactType;
    }

    public Subroutine getTheSubroutine() {
        return theSubroutine;
    }

    public ToolParameter theSubroutine(Subroutine subroutine) {
        this.theSubroutine = subroutine;
        return this;
    }

    public void setTheSubroutine(Subroutine subroutine) {
        this.theSubroutine = subroutine;
    }

    public PrimitiveType getThePrimitiveType() {
        return thePrimitiveType;
    }

    public ToolParameter thePrimitiveType(PrimitiveType primitiveType) {
        this.thePrimitiveType = primitiveType;
        return this;
    }

    public void setThePrimitiveType(PrimitiveType primitiveType) {
        this.thePrimitiveType = primitiveType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToolParameter)) {
            return false;
        }
        return id != null && id.equals(((ToolParameter) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ToolParameter{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", separatorSymbol='" + getSeparatorSymbol() + "'" +
            "}";
    }
}
