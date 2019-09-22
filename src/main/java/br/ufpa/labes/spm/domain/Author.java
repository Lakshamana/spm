package br.ufpa.labes.spm.domain;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

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

    @Column(name = "uid")
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

    @Column(name = "photo_url")
    private String photoURL;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "author_authors_followed",
               joinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "authors_followed_id", referencedColumnName = "id"))
    private Set<Author> authorsFolloweds = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Asset> theAssets = new HashSet<>();

    @OneToMany(mappedBy = "author")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AuthorStat> theAuthorStats = new HashSet<>();

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
    private Set<Author> theAuthors = new HashSet<>();

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

    public String getPhotoURL() {
        return photoURL;
    }

    public Author photoURL(String photoURL) {
        this.photoURL = photoURL;
        return this;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
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

    public Set<Author> getAuthorsFolloweds() {
        return authorsFolloweds;
    }

    public Author authorsFolloweds(Set<Author> authors) {
        this.authorsFolloweds = authors;
        return this;
    }

    public Author addAuthorsFollowed(Author author) {
        this.authorsFolloweds.add(author);
        author.getTheAuthors().add(this);
        return this;
    }

    public Author removeAuthorsFollowed(Author author) {
        this.authorsFolloweds.remove(author);
        author.getTheAuthors().remove(this);
        return this;
    }

    public void setAuthorsFolloweds(Set<Author> authors) {
        this.authorsFolloweds = authors;
    }

    public Set<Asset> getTheAssets() {
        return theAssets;
    }

    public Author theAssets(Set<Asset> assets) {
        this.theAssets = assets;
        return this;
    }

    public Author addTheAssets(Asset asset) {
        this.theAssets.add(asset);
        asset.setOwner(this);
        return this;
    }

    public Author removeTheAssets(Asset asset) {
        this.theAssets.remove(asset);
        asset.setOwner(null);
        return this;
    }

    public void setTheAssets(Set<Asset> assets) {
        this.theAssets = assets;
    }

    public Set<AuthorStat> getTheAuthorStats() {
        return theAuthorStats;
    }

    public Author theAuthorStats(Set<AuthorStat> authorStats) {
        this.theAuthorStats = authorStats;
        return this;
    }

    public Author addTheAuthorStats(AuthorStat authorStat) {
        this.theAuthorStats.add(authorStat);
        authorStat.setAuthor(this);
        return this;
    }

    public Author removeTheAuthorStats(AuthorStat authorStat) {
        this.theAuthorStats.remove(authorStat);
        authorStat.setAuthor(null);
        return this;
    }

    public void setTheAuthorStats(Set<AuthorStat> authorStats) {
        this.theAuthorStats = authorStats;
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

    public Set<Author> getTheAuthors() {
        return theAuthors;
    }

    public Author theAuthors(Set<Author> authors) {
        this.theAuthors = authors;
        return this;
    }

    public Author addTheAuthor(Author author) {
        this.theAuthors.add(author);
        author.getAuthorsFolloweds().add(this);
        return this;
    }

    public Author removeTheAuthor(Author author) {
        this.theAuthors.remove(author);
        author.getAuthorsFolloweds().remove(this);
        return this;
    }

    public void setTheAuthors(Set<Author> authors) {
        this.theAuthors = authors;
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
            ", photoURL='" + getPhotoURL() + "'" +
            "}";
    }

    public void sendMessageTo(Author author, String msg) {
      Message message = new Message(this, msg, author);

      author.receivedMessages.add(message);
      this.sentMessages.add(message);
    }

    public int getReceivedMessagesCount() {
      return this.receivedMessages.size();
    }

    public int getSentMessagesCount() {
      return this.sentMessages.size();
    }
}
