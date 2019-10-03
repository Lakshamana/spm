package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Calendar.
 */
@Entity
@Table(name = "calendar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Calendar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "calendar")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotWorkingDay> notWorkingDays = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("theCalendars")
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Calendar name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<NotWorkingDay> getNotWorkingDays() {
        return notWorkingDays;
    }

    public Calendar notWorkingDays(Set<NotWorkingDay> notWorkingDays) {
        this.notWorkingDays = notWorkingDays;
        return this;
    }

    public Calendar addNotWorkingDays(NotWorkingDay notWorkingDay) {
        this.notWorkingDays.add(notWorkingDay);
        notWorkingDay.setCalendar(this);
        return this;
    }

    public Calendar removeNotWorkingDays(NotWorkingDay notWorkingDay) {
        this.notWorkingDays.remove(notWorkingDay);
        notWorkingDay.setCalendar(null);
        return this;
    }

    public void setNotWorkingDays(Set<NotWorkingDay> notWorkingDays) {
        this.notWorkingDays = notWorkingDays;
    }

    public Project getProject() {
        return project;
    }

    public Calendar project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Calendar)) {
            return false;
        }
        return id != null && id.equals(((Calendar) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Calendar{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
