package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import br.ufpa.labes.spm.domain.enumeration.AssetVisibility;

/**
 * A Asset.
 */
@Entity
@Table(name = "asset")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "applicability")
    private String applicability;

    @Column(name = "tags")
    private String tags;

    @Column(name = "path")
    private String path;

    @Column(name = "latest_version")
    private String latestVersion;

    @Column(name = "read_only")
    private Boolean readOnly;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_visibility")
    private AssetVisibility assetVisibility;

    @OneToOne
    @JoinColumn(unique = true)
    private AssetStat stats;

    @OneToMany(mappedBy = "theAsset")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AuthorStat> authorStats = new HashSet<>();

    @OneToMany(mappedBy = "theAsset")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TagStats> tagStats = new HashSet<>();

    @OneToMany(mappedBy = "theAsset")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LessonLearned> lessonsLearneds = new HashSet<>();

    @OneToMany(mappedBy = "theAsset")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AssetRelationship> relatedAssets = new HashSet<>();

    @OneToMany(mappedBy = "relatedAsset")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AssetRelationship> relatedByAssets = new HashSet<>();

    @OneToMany(mappedBy = "theAsset")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> comments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("theAssets")
    private Author owner;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "asset_favorited_by",
               joinColumns = @JoinColumn(name = "asset_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "favorited_by_id", referencedColumnName = "id"))
    private Set<Author> favoritedBies = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "asset_followers",
               joinColumns = @JoinColumn(name = "asset_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "followers_id", referencedColumnName = "id"))
    private Set<Author> followers = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "asset_collaborators",
               joinColumns = @JoinColumn(name = "asset_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "collaborators_id", referencedColumnName = "id"))
    private Set<Author> collaborators = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public Asset uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Asset creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public Asset publishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public String getName() {
        return name;
    }

    public Asset name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Asset description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplicability() {
        return applicability;
    }

    public Asset applicability(String applicability) {
        this.applicability = applicability;
        return this;
    }

    public void setApplicability(String applicability) {
        this.applicability = applicability;
    }

    public String getTags() {
        return tags;
    }

    public Asset tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPath() {
        return path;
    }

    public Asset path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public Asset latestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
        return this;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public Boolean isReadOnly() {
        return readOnly;
    }

    public Asset readOnly(Boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public AssetVisibility getAssetVisibility() {
        return assetVisibility;
    }

    public Asset assetVisibility(AssetVisibility assetVisibility) {
        this.assetVisibility = assetVisibility;
        return this;
    }

    public void setAssetVisibility(AssetVisibility assetVisibility) {
        this.assetVisibility = assetVisibility;
    }

    public AssetStat getStats() {
        return stats;
    }

    public Asset stats(AssetStat assetStat) {
        this.stats = assetStat;
        return this;
    }

    public void setStats(AssetStat assetStat) {
        this.stats = assetStat;
    }

    public Set<AuthorStat> getAuthorStats() {
        return authorStats;
    }

    public Asset authorStats(Set<AuthorStat> authorStats) {
        this.authorStats = authorStats;
        return this;
    }

    public Asset addAuthorStats(AuthorStat authorStat) {
        this.authorStats.add(authorStat);
        authorStat.setTheAsset(this);
        return this;
    }

    public Asset removeAuthorStats(AuthorStat authorStat) {
        this.authorStats.remove(authorStat);
        authorStat.setTheAsset(null);
        return this;
    }

    public void setAuthorStats(Set<AuthorStat> authorStats) {
        this.authorStats = authorStats;
    }

    public Set<TagStats> getTagStats() {
        return tagStats;
    }

    public Asset tagStats(Set<TagStats> tagStats) {
        this.tagStats = tagStats;
        return this;
    }

    public Asset addTagStats(TagStats tagStat) {
        this.tagStats.add(tagStat);
        tagStat.setTheAsset(this);
        return this;
    }

    public Asset removeTagStats(TagStats tagStat) {
        this.tagStats.remove(tagStat);
        tagStat.setTheAsset(null);
        return this;
    }

    public void setTagStats(Set<TagStats> tagStats) {
        this.tagStats = tagStats;
    }

    public Set<LessonLearned> getLessonsLearneds() {
        return lessonsLearneds;
    }

    public Asset lessonsLearneds(Set<LessonLearned> lessonLearneds) {
        this.lessonsLearneds = lessonLearneds;
        return this;
    }

    public Asset addLessonsLearned(LessonLearned lessonLearned) {
        this.lessonsLearneds.add(lessonLearned);
        lessonLearned.setTheAsset(this);
        return this;
    }

    public Asset removeLessonsLearned(LessonLearned lessonLearned) {
        this.lessonsLearneds.remove(lessonLearned);
        lessonLearned.setTheAsset(null);
        return this;
    }

    public void setLessonsLearneds(Set<LessonLearned> lessonLearneds) {
        this.lessonsLearneds = lessonLearneds;
    }

    public Set<AssetRelationship> getRelatedAssets() {
        return relatedAssets;
    }

    public Asset relatedAssets(Set<AssetRelationship> assetRelationships) {
        this.relatedAssets = assetRelationships;
        return this;
    }

    public Asset addRelatedAssets(AssetRelationship assetRelationship) {
        this.relatedAssets.add(assetRelationship);
        assetRelationship.setTheAsset(this);
        return this;
    }

    public Asset removeRelatedAssets(AssetRelationship assetRelationship) {
        this.relatedAssets.remove(assetRelationship);
        assetRelationship.setTheAsset(null);
        return this;
    }

    public void setRelatedAssets(Set<AssetRelationship> assetRelationships) {
        this.relatedAssets = assetRelationships;
    }

    public Set<AssetRelationship> getRelatedByAssets() {
        return relatedByAssets;
    }

    public Asset relatedByAssets(Set<AssetRelationship> assetRelationships) {
        this.relatedByAssets = assetRelationships;
        return this;
    }

    public Asset addRelatedByAssets(AssetRelationship assetRelationship) {
        this.relatedByAssets.add(assetRelationship);
        assetRelationship.setRelatedAsset(this);
        return this;
    }

    public Asset removeRelatedByAssets(AssetRelationship assetRelationship) {
        this.relatedByAssets.remove(assetRelationship);
        assetRelationship.setRelatedAsset(null);
        return this;
    }

    public void setRelatedByAssets(Set<AssetRelationship> assetRelationships) {
        this.relatedByAssets = assetRelationships;
    }

    public Set<Message> getComments() {
        return comments;
    }

    public Asset comments(Set<Message> messages) {
        this.comments = messages;
        return this;
    }

    public Asset addComments(Message message) {
        this.comments.add(message);
        message.setTheAsset(this);
        return this;
    }

    public Asset removeComments(Message message) {
        this.comments.remove(message);
        message.setTheAsset(null);
        return this;
    }

    public void setComments(Set<Message> messages) {
        this.comments = messages;
    }

    public Author getOwner() {
        return owner;
    }

    public Asset owner(Author author) {
        this.owner = author;
        return this;
    }

    public void setOwner(Author author) {
        this.owner = author;
    }

    public Set<Author> getFavoritedBies() {
        return favoritedBies;
    }

    public Asset favoritedBies(Set<Author> authors) {
        this.favoritedBies = authors;
        return this;
    }

    public Asset addFavoritedBy(Author author) {
        this.favoritedBies.add(author);
        author.getFavorites().add(this);
        return this;
    }

    public Asset removeFavoritedBy(Author author) {
        this.favoritedBies.remove(author);
        author.getFavorites().remove(this);
        return this;
    }

    public void setFavoritedBies(Set<Author> authors) {
        this.favoritedBies = authors;
    }

    public Set<Author> getFollowers() {
        return followers;
    }

    public Asset followers(Set<Author> authors) {
        this.followers = authors;
        return this;
    }

    public Asset addFollowers(Author author) {
        this.followers.add(author);
        author.getAssetsFolloweds().add(this);
        return this;
    }

    public Asset removeFollowers(Author author) {
        this.followers.remove(author);
        author.getAssetsFolloweds().remove(this);
        return this;
    }

    public void setFollowers(Set<Author> authors) {
        this.followers = authors;
    }

    public Set<Author> getCollaborators() {
        return collaborators;
    }

    public Asset collaborators(Set<Author> authors) {
        this.collaborators = authors;
        return this;
    }

    public Asset addCollaborators(Author author) {
        this.collaborators.add(author);
        author.getCollaborateOnAssets().add(this);
        return this;
    }

    public Asset removeCollaborators(Author author) {
        this.collaborators.remove(author);
        author.getCollaborateOnAssets().remove(this);
        return this;
    }

    public void setCollaborators(Set<Author> authors) {
        this.collaborators = authors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Asset)) {
            return false;
        }
        return id != null && id.equals(((Asset) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Asset{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", applicability='" + getApplicability() + "'" +
            ", tags='" + getTags() + "'" +
            ", path='" + getPath() + "'" +
            ", latestVersion='" + getLatestVersion() + "'" +
            ", readOnly='" + isReadOnly() + "'" +
            ", assetVisibility='" + getAssetVisibility() + "'" +
            "}";
    }
}
