package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/** A ChatMessage. */
@Entity
@Table(name = "chat_message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChatMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ident")
  private String ident;

  @Column(name = "text")
  private String text;

  @NotNull
  @Column(name = "timestamp", nullable = false)
  private LocalDate timestamp;

  @OneToOne
  @JoinColumn(unique = true)
  private Agent fromAgent;

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

  public ChatMessage ident(String ident) {
    this.ident = ident;
    return this;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getText() {
    return text;
  }

  public ChatMessage text(String text) {
    this.text = text;
    return this;
  }

  public void setText(String text) {
    this.text = text;
  }

  public LocalDate getTimestamp() {
    return timestamp;
  }

  public ChatMessage timestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  public Agent getFromAgent() {
    return fromAgent;
  }

  public ChatMessage fromAgent(Agent agent) {
    this.fromAgent = agent;
    return this;
  }

  public void setFromAgent(Agent agent) {
    this.fromAgent = agent;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ChatMessage)) {
      return false;
    }
    return id != null && id.equals(((ChatMessage) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ChatMessage{"
        + "id="
        + getId()
        + ", ident='"
        + getIdent()
        + "'"
        + ", text='"
        + getText()
        + "'"
        + ", timestamp='"
        + getTimestamp()
        + "'"
        + "}";
  }
}
