package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Author.
 */
@Entity
@Table(name = "author")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy=InheritanceType.JOINED)
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(name = "uid", unique = true)
    private String uid;

    @Column(name = "version")
    private Integer version;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "interests")
    private String interests;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "author")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AuthorStat> stats = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "author_authors_followed",
               joinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "authors_followed_id", referencedColumnName = "id"))
    private Set<Author> authorsFolloweds = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Asset> assets = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LessonLearned> theLessonLearneds = new HashSet<>();

    @OneToMany(mappedBy = "sender")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> sentMessages = new HashSet<>();

    @OneToMany(mappedBy = "recipient")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Message> receivedMessages = new HashSet<>();

    @ManyToMany(mappedBy = "favoritedBies")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Asset> favorites = new HashSet<>();

    @ManyToMany(mappedBy = "followers")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Asset> assetsFolloweds = new HashSet<>();

    @ManyToMany(mappedBy = "collaborators")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Asset> collaborateOnAssets = new HashSet<>();

    @ManyToMany(mappedBy = "authorsFolloweds")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Author> followers = new HashSet<>();

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

    public Author uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getVersion() {
        return version;
    }

    public Author version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public Author name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Author email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInterests() {
        return interests;
    }

    public Author interests(String interests) {
        this.interests = interests;
        return this;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getCity() {
        return city;
    }

    public Author city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public Author country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Author photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public Author photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public User getUser() {
        return user;
    }

    public Author user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<AuthorStat> getStats() {
        return stats;
    }

    public Author stats(Set<AuthorStat> authorStats) {
        this.stats = authorStats;
        return this;
    }

    public Author addStats(AuthorStat authorStat) {
        this.stats.add(authorStat);
        authorStat.setAuthor(this);
        return this;
    }

    public Author removeStats(AuthorStat authorStat) {
        this.stats.remove(authorStat);
        authorStat.setAuthor(null);
        return this;
    }

    public void setStats(Set<AuthorStat> authorStats) {
        this.stats = authorStats;
    }

    public Set<Author> getAuthorsFolloweds() {
        return authorsFolloweds;
    }

    public Author authorsFolloweds(Set<Author> authors) {
        this.authorsFolloweds = authors;
        return this;
    }

    public Author addAuthorsFollowed(Author author) {
        this.authorsFolloweds.add(author);
        author.getFollowers().add(this);
        return this;
    }

    public Author removeAuthorsFollowed(Author author) {
        this.authorsFolloweds.remove(author);
        author.getFollowers().remove(this);
        return this;
    }

    public void setAuthorsFolloweds(Set<Author> authors) {
        this.authorsFolloweds = authors;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public Author assets(Set<Asset> assets) {
        this.assets = assets;
        return this;
    }

    public Author addAssets(Asset asset) {
        this.assets.add(asset);
        asset.setOwner(this);
        return this;
    }

    public Author removeAssets(Asset asset) {
        this.assets.remove(asset);
        asset.setOwner(null);
        return this;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }

    public Set<LessonLearned> getTheLessonLearneds() {
        return theLessonLearneds;
    }

    public Author theLessonLearneds(Set<LessonLearned> lessonLearneds) {
        this.theLessonLearneds = lessonLearneds;
        return this;
    }

    public Author addTheLessonLearned(LessonLearned lessonLearned) {
        this.theLessonLearneds.add(lessonLearned);
        lessonLearned.setAuthor(this);
        return this;
    }

    public Author removeTheLessonLearned(LessonLearned lessonLearned) {
        this.theLessonLearneds.remove(lessonLearned);
        lessonLearned.setAuthor(null);
        return this;
    }

    public void setTheLessonLearneds(Set<LessonLearned> lessonLearneds) {
        this.theLessonLearneds = lessonLearneds;
    }

    public Set<Message> getSentMessages() {
        return sentMessages;
    }

    public Author sentMessages(Set<Message> messages) {
        this.sentMessages = messages;
        return this;
    }

    public Author addSentMessages(Message message) {
        this.sentMessages.add(message);
        message.setSender(this);
        return this;
    }

    public Author removeSentMessages(Message message) {
        this.sentMessages.remove(message);
        message.setSender(null);
        return this;
    }

    public void setSentMessages(Set<Message> messages) {
        this.sentMessages = messages;
    }

    public Set<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public Author receivedMessages(Set<Message> messages) {
        this.receivedMessages = messages;
        return this;
    }

    public Author addReceivedMessages(Message message) {
        this.receivedMessages.add(message);
        message.setRecipient(this);
        return this;
    }

    public Author removeReceivedMessages(Message message) {
        this.receivedMessages.remove(message);
        message.setRecipient(null);
        return this;
    }

    public void setReceivedMessages(Set<Message> messages) {
        this.receivedMessages = messages;
    }

    public Set<Asset> getFavorites() {
        return favorites;
    }

    public Author favorites(Set<Asset> assets) {
        this.favorites = assets;
        return this;
    }

    public Author addFavorites(Asset asset) {
        this.favorites.add(asset);
        asset.getFavoritedBies().add(this);
        return this;
    }

    public Author removeFavorites(Asset asset) {
        this.favorites.remove(asset);
        asset.getFavoritedBies().remove(this);
        return this;
    }

    public void setFavorites(Set<Asset> assets) {
        this.favorites = assets;
    }

    public Set<Asset> getAssetsFolloweds() {
        return assetsFolloweds;
    }

    public Author assetsFolloweds(Set<Asset> assets) {
        this.assetsFolloweds = assets;
        return this;
    }

    public Author addAssetsFollowed(Asset asset) {
        this.assetsFolloweds.add(asset);
        asset.getFollowers().add(this);
        return this;
    }

    public Author removeAssetsFollowed(Asset asset) {
        this.assetsFolloweds.remove(asset);
        asset.getFollowers().remove(this);
        return this;
    }

    public void setAssetsFolloweds(Set<Asset> assets) {
        this.assetsFolloweds = assets;
    }

    public Set<Asset> getCollaborateOnAssets() {
        return collaborateOnAssets;
    }

    public Author collaborateOnAssets(Set<Asset> assets) {
        this.collaborateOnAssets = assets;
        return this;
    }

    public Author addCollaborateOnAssets(Asset asset) {
        this.collaborateOnAssets.add(asset);
        asset.getCollaborators().add(this);
        return this;
    }

    public Author removeCollaborateOnAssets(Asset asset) {
        this.collaborateOnAssets.remove(asset);
        asset.getCollaborators().remove(this);
        return this;
    }

    public void setCollaborateOnAssets(Set<Asset> assets) {
        this.collaborateOnAssets = assets;
    }

    public Set<Author> getFollowers() {
        return followers;
    }

    public Author followers(Set<Author> authors) {
        this.followers = authors;
        return this;
    }

    public Author addFollowers(Author author) {
        this.followers.add(author);
        author.getAuthorsFolloweds().add(this);
        return this;
    }

    public Author removeFollowers(Author author) {
        this.followers.remove(author);
        author.getAuthorsFolloweds().remove(this);
        return this;
    }

    public void setFollowers(Set<Author> authors) {
        this.followers = authors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Author)) {
            return false;
        }
        return id != null && id.equals(((Author) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Author{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", version=" + getVersion() +
            ", name='" + getName() + "'" +
            ", email='" + getEmail() + "'" +
            ", interests='" + getInterests() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
