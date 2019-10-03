package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Description.
 */
@Entity
@Table(name = "description")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Description implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Lob
    @Column(name = "why")
    private String why;

    @ManyToOne
    @JsonIgnoreProperties("theDerivedVersionDescriptions")
    private Template theOldVersion;

    @ManyToOne
    @JsonIgnoreProperties("theTemplateNewDescriptions")
    private Template theNewVersion;

    @OneToMany(mappedBy = "theOriginalVersionDescription")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Template> descTemplateOriginalVersions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Description date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWhy() {
        return why;
    }

    public Description why(String why) {
        this.why = why;
        return this;
    }

    public void setWhy(String why) {
        this.why = why;
    }

    public Template getTheOldVersion() {
        return theOldVersion;
    }

    public Description theOldVersion(Template template) {
        this.theOldVersion = template;
        return this;
    }

    public void setTheOldVersion(Template template) {
        this.theOldVersion = template;
    }

    public Template getTheNewVersion() {
        return theNewVersion;
    }

    public Description theNewVersion(Template template) {
        this.theNewVersion = template;
        return this;
    }

    public void setTheNewVersion(Template template) {
        this.theNewVersion = template;
    }

    public Set<Template> getDescTemplateOriginalVersions() {
        return descTemplateOriginalVersions;
    }

    public Description descTemplateOriginalVersions(Set<Template> templates) {
        this.descTemplateOriginalVersions = templates;
        return this;
    }

    public Description addDescTemplateOriginalVersion(Template template) {
        this.descTemplateOriginalVersions.add(template);
        template.setTheOriginalVersionDescription(this);
        return this;
    }

    public Description removeDescTemplateOriginalVersion(Template template) {
        this.descTemplateOriginalVersions.remove(template);
        template.setTheOriginalVersionDescription(null);
        return this;
    }

    public void setDescTemplateOriginalVersions(Set<Template> templates) {
        this.descTemplateOriginalVersions = templates;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Description)) {
            return false;
        }
        return id != null && id.equals(((Description) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Description{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", why='" + getWhy() + "'" +
            "}";
    }
}
