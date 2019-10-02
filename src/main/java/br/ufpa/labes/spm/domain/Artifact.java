package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Artifact.
 */
@Entity
@Table(name = "artifact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Artifact implements Serializable {

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

    @Column(name = "path_name")
    private String pathName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "latest_version")
    private String latestVersion;

    @Column(name = "is_template")
    private Boolean isTemplate;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "theArtifact")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<InvolvedArtifact> theInvolvedArtifacts = new HashSet<>();

    @OneToMany(mappedBy = "theArtifact")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArtifactParam> theArtifactParams = new HashSet<>();

    @OneToMany(mappedBy = "theArtifact")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Automatic> theAutomatics = new HashSet<>();

    @OneToMany(mappedBy = "artifact")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArtifactMetric> theArtifactMetrics = new HashSet<>();

    @OneToMany(mappedBy = "artifact")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArtifactEstimation> theArtifactEstimations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("theArtifacts")
    private ArtifactType theArtifactType;

    @ManyToOne
    @JsonIgnoreProperties("derivedTos")
    private Artifact derivedFrom;

    @ManyToOne
    @JsonIgnoreProperties("possesses")
    private Artifact belongsTo;

    @ManyToOne
    @JsonIgnoreProperties("theArtifacts")
    private VCSRepository theRepository;

    @ManyToOne
    @JsonIgnoreProperties("finalArtifacts")
    private Project theProject;

    @OneToMany(mappedBy = "derivedFrom")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Artifact> derivedTos = new HashSet<>();

    @OneToMany(mappedBy = "belongsTo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Artifact> possesses = new HashSet<>();

    @OneToMany(mappedBy = "theArtifact")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArtifactTask> theArtifactTasks = new HashSet<>();

    @OneToMany(mappedBy = "theArtifact")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ArtifactCon> theArtifactCons = new HashSet<>();

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

    public Artifact ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public Artifact name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Artifact description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathName() {
        return pathName;
    }

    public Artifact pathName(String pathName) {
        this.pathName = pathName;
        return this;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getFileName() {
        return fileName;
    }

    public Artifact fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public Artifact latestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
        return this;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public Boolean isIsTemplate() {
        return isTemplate;
    }

    public Artifact isTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
        return this;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Artifact isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<InvolvedArtifact> getTheInvolvedArtifacts() {
        return theInvolvedArtifacts;
    }

    public Artifact theInvolvedArtifacts(Set<InvolvedArtifact> involvedArtifacts) {
        this.theInvolvedArtifacts = involvedArtifacts;
        return this;
    }

    public Artifact addTheInvolvedArtifacts(InvolvedArtifact involvedArtifact) {
        this.theInvolvedArtifacts.add(involvedArtifact);
        involvedArtifact.setTheArtifact(this);
        return this;
    }

    public Artifact removeTheInvolvedArtifacts(InvolvedArtifact involvedArtifact) {
        this.theInvolvedArtifacts.remove(involvedArtifact);
        involvedArtifact.setTheArtifact(null);
        return this;
    }

    public void setTheInvolvedArtifacts(Set<InvolvedArtifact> involvedArtifacts) {
        this.theInvolvedArtifacts = involvedArtifacts;
    }

    public Set<ArtifactParam> getTheArtifactParams() {
        return theArtifactParams;
    }

    public Artifact theArtifactParams(Set<ArtifactParam> artifactParams) {
        this.theArtifactParams = artifactParams;
        return this;
    }

    public Artifact addTheArtifactParam(ArtifactParam artifactParam) {
        this.theArtifactParams.add(artifactParam);
        artifactParam.setTheArtifact(this);
        return this;
    }

    public Artifact removeTheArtifactParam(ArtifactParam artifactParam) {
        this.theArtifactParams.remove(artifactParam);
        artifactParam.setTheArtifact(null);
        return this;
    }

    public void setTheArtifactParams(Set<ArtifactParam> artifactParams) {
        this.theArtifactParams = artifactParams;
    }

    public Set<Automatic> getTheAutomatics() {
        return theAutomatics;
    }

    public Artifact theAutomatics(Set<Automatic> automatics) {
        this.theAutomatics = automatics;
        return this;
    }

    public Artifact addTheAutomatic(Automatic automatic) {
        this.theAutomatics.add(automatic);
        automatic.setTheArtifact(this);
        return this;
    }

    public Artifact removeTheAutomatic(Automatic automatic) {
        this.theAutomatics.remove(automatic);
        automatic.setTheArtifact(null);
        return this;
    }

    public void setTheAutomatics(Set<Automatic> automatics) {
        this.theAutomatics = automatics;
    }

    public Set<ArtifactMetric> getTheArtifactMetrics() {
        return theArtifactMetrics;
    }

    public Artifact theArtifactMetrics(Set<ArtifactMetric> artifactMetrics) {
        this.theArtifactMetrics = artifactMetrics;
        return this;
    }

    public Artifact addTheArtifactMetric(ArtifactMetric artifactMetric) {
        this.theArtifactMetrics.add(artifactMetric);
        artifactMetric.setArtifact(this);
        return this;
    }

    public Artifact removeTheArtifactMetric(ArtifactMetric artifactMetric) {
        this.theArtifactMetrics.remove(artifactMetric);
        artifactMetric.setArtifact(null);
        return this;
    }

    public void setTheArtifactMetrics(Set<ArtifactMetric> artifactMetrics) {
        this.theArtifactMetrics = artifactMetrics;
    }

    public Set<ArtifactEstimation> getTheArtifactEstimations() {
        return theArtifactEstimations;
    }

    public Artifact theArtifactEstimations(Set<ArtifactEstimation> artifactEstimations) {
        this.theArtifactEstimations = artifactEstimations;
        return this;
    }

    public Artifact addTheArtifactEstimation(ArtifactEstimation artifactEstimation) {
        this.theArtifactEstimations.add(artifactEstimation);
        artifactEstimation.setArtifact(this);
        return this;
    }

    public Artifact removeTheArtifactEstimation(ArtifactEstimation artifactEstimation) {
        this.theArtifactEstimations.remove(artifactEstimation);
        artifactEstimation.setArtifact(null);
        return this;
    }

    public void setTheArtifactEstimations(Set<ArtifactEstimation> artifactEstimations) {
        this.theArtifactEstimations = artifactEstimations;
    }

    public ArtifactType getTheArtifactType() {
        return theArtifactType;
    }

    public Artifact theArtifactType(ArtifactType artifactType) {
        this.theArtifactType = artifactType;
        return this;
    }

    public void setTheArtifactType(ArtifactType artifactType) {
        this.theArtifactType = artifactType;
    }

    public Artifact getDerivedFrom() {
        return derivedFrom;
    }

    public Artifact derivedFrom(Artifact artifact) {
        this.derivedFrom = artifact;
        return this;
    }

    public void setDerivedFrom(Artifact artifact) {
        this.derivedFrom = artifact;
    }

    public Artifact getBelongsTo() {
        return belongsTo;
    }

    public Artifact belongsTo(Artifact artifact) {
        this.belongsTo = artifact;
        return this;
    }

    public void setBelongsTo(Artifact artifact) {
        this.belongsTo = artifact;
    }

    public VCSRepository getTheRepository() {
        return theRepository;
    }

    public Artifact theRepository(VCSRepository vCSRepository) {
        this.theRepository = vCSRepository;
        return this;
    }

    public void setTheRepository(VCSRepository vCSRepository) {
        this.theRepository = vCSRepository;
    }

    public Project getTheProject() {
        return theProject;
    }

    public Artifact theProject(Project project) {
        this.theProject = project;
        return this;
    }

    public void setTheProject(Project project) {
        this.theProject = project;
    }

    public Set<Artifact> getDerivedTos() {
        return derivedTos;
    }

    public Artifact derivedTos(Set<Artifact> artifacts) {
        this.derivedTos = artifacts;
        return this;
    }

    public Artifact addDerivedTo(Artifact artifact) {
        this.derivedTos.add(artifact);
        artifact.setDerivedFrom(this);
        return this;
    }

    public Artifact removeDerivedTo(Artifact artifact) {
        this.derivedTos.remove(artifact);
        artifact.setDerivedFrom(null);
        return this;
    }

    public void setDerivedTos(Set<Artifact> artifacts) {
        this.derivedTos = artifacts;
    }

    public Set<Artifact> getPossesses() {
        return possesses;
    }

    public Artifact possesses(Set<Artifact> artifacts) {
        this.possesses = artifacts;
        return this;
    }

    public Artifact addPossess(Artifact artifact) {
        this.possesses.add(artifact);
        artifact.setBelongsTo(this);
        return this;
    }

    public Artifact removePossess(Artifact artifact) {
        this.possesses.remove(artifact);
        artifact.setBelongsTo(null);
        return this;
    }

    public void setPossesses(Set<Artifact> artifacts) {
        this.possesses = artifacts;
    }

    public Set<ArtifactTask> getTheArtifactTasks() {
        return theArtifactTasks;
    }

    public Artifact theArtifactTasks(Set<ArtifactTask> artifactTasks) {
        this.theArtifactTasks = artifactTasks;
        return this;
    }

    public Artifact addTheArtifactTasks(ArtifactTask artifactTask) {
        this.theArtifactTasks.add(artifactTask);
        artifactTask.setTheArtifact(this);
        return this;
    }

    public Artifact removeTheArtifactTasks(ArtifactTask artifactTask) {
        this.theArtifactTasks.remove(artifactTask);
        artifactTask.setTheArtifact(null);
        return this;
    }

    public void setTheArtifactTasks(Set<ArtifactTask> artifactTasks) {
        this.theArtifactTasks = artifactTasks;
    }

    public Set<ArtifactCon> getTheArtifactCons() {
        return theArtifactCons;
    }

    public Artifact theArtifactCons(Set<ArtifactCon> artifactCons) {
        this.theArtifactCons = artifactCons;
        return this;
    }

    public Artifact addTheArtifactCon(ArtifactCon artifactCon) {
        this.theArtifactCons.add(artifactCon);
        artifactCon.setTheArtifact(this);
        return this;
    }

    public Artifact removeTheArtifactCon(ArtifactCon artifactCon) {
        this.theArtifactCons.remove(artifactCon);
        artifactCon.setTheArtifact(null);
        return this;
    }

    public void setTheArtifactCons(Set<ArtifactCon> artifactCons) {
        this.theArtifactCons = artifactCons;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Artifact)) {
            return false;
        }
        return id != null && id.equals(((Artifact) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Artifact{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", pathName='" + getPathName() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", latestVersion='" + getLatestVersion() + "'" +
            ", isTemplate='" + isIsTemplate() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
