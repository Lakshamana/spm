package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ArtifactTask.
 */
@Entity
@Table(name = "artifact_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ArtifactTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "in_workspace_version")
    private String inWorkspaceVersion;

    @Column(name = "out_workspace_version")
    private String outWorkspaceVersion;

    @ManyToOne
    @JsonIgnoreProperties("theArtifactTasks")
    private Artifact theArtifact;

    @ManyToOne
    @JsonIgnoreProperties("theArtifactTasks")
    private Task theTask;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInWorkspaceVersion() {
        return inWorkspaceVersion;
    }

    public ArtifactTask inWorkspaceVersion(String inWorkspaceVersion) {
        this.inWorkspaceVersion = inWorkspaceVersion;
        return this;
    }

    public void setInWorkspaceVersion(String inWorkspaceVersion) {
        this.inWorkspaceVersion = inWorkspaceVersion;
    }

    public String getOutWorkspaceVersion() {
        return outWorkspaceVersion;
    }

    public ArtifactTask outWorkspaceVersion(String outWorkspaceVersion) {
        this.outWorkspaceVersion = outWorkspaceVersion;
        return this;
    }

    public void setOutWorkspaceVersion(String outWorkspaceVersion) {
        this.outWorkspaceVersion = outWorkspaceVersion;
    }

    public Artifact getTheArtifact() {
        return theArtifact;
    }

    public ArtifactTask theArtifact(Artifact artifact) {
        this.theArtifact = artifact;
        return this;
    }

    public void setTheArtifact(Artifact artifact) {
        this.theArtifact = artifact;
    }

    public Task getTheTask() {
        return theTask;
    }

    public ArtifactTask theTask(Task task) {
        this.theTask = task;
        return this;
    }

    public void setTheTask(Task task) {
        this.theTask = task;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArtifactTask)) {
            return false;
        }
        return id != null && id.equals(((ArtifactTask) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ArtifactTask{" +
            "id=" + getId() +
            ", inWorkspaceVersion='" + getInWorkspaceVersion() + "'" +
            ", outWorkspaceVersion='" + getOutWorkspaceVersion() + "'" +
            "}";
    }
}
