package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "time")
    private LocalDate time;

    @ManyToOne
    @JsonIgnoreProperties("sentMessages")
    private Author sender;

    @ManyToOne
    @JsonIgnoreProperties("receivedMessages")
    private Author recipient;

    @ManyToOne
    @JsonIgnoreProperties("comments")
    private Asset theAsset;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Message content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getTime() {
        return time;
    }

    public Message time(LocalDate time) {
        this.time = time;
        return this;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public Author getSender() {
        return sender;
    }

    public Message sender(Author author) {
        this.sender = author;
        return this;
    }

    public void setSender(Author author) {
        this.sender = author;
    }

    public Author getRecipient() {
        return recipient;
    }

    public Message recipient(Author author) {
        this.recipient = author;
        return this;
    }

    public void setRecipient(Author author) {
        this.recipient = author;
    }

    public Asset getTheAsset() {
        return theAsset;
    }

    public Message theAsset(Asset asset) {
        this.theAsset = asset;
        return this;
    }

    public void setTheAsset(Asset asset) {
        this.theAsset = asset;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", time='" + getTime() + "'" +
            "}";
    }
}
