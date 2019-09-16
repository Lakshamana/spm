package br.ufpa.labes.spm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/** A ChatLog. */
@Entity
@Table(name = "chat_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChatLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "log")
  private String log;

  @Column(name = "date")
  private LocalDate date;

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "chat_log_involved_agents_in_chat",
      joinColumns = @JoinColumn(name = "chat_log_id", referencedColumnName = "id"),
      inverseJoinColumns =
          @JoinColumn(name = "involved_agents_in_chat_id", referencedColumnName = "id"))
  private Set<Agent> involvedAgentsInChats = new HashSet<>();

  // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLog() {
    return log;
  }

  public ChatLog log(String log) {
    this.log = log;
    return this;
  }

  public void setLog(String log) {
    this.log = log;
  }

  public LocalDate getDate() {
    return date;
  }

  public ChatLog date(LocalDate date) {
    this.date = date;
    return this;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public Set<Agent> getInvolvedAgentsInChats() {
    return involvedAgentsInChats;
  }

  public ChatLog involvedAgentsInChats(Set<Agent> agents) {
    this.involvedAgentsInChats = agents;
    return this;
  }

  public ChatLog addInvolvedAgentsInChat(Agent agent) {
    this.involvedAgentsInChats.add(agent);
    agent.getTheChatLogs().add(this);
    return this;
  }

  public ChatLog removeInvolvedAgentsInChat(Agent agent) {
    this.involvedAgentsInChats.remove(agent);
    agent.getTheChatLogs().remove(this);
    return this;
  }

  public void setInvolvedAgentsInChats(Set<Agent> agents) {
    this.involvedAgentsInChats = agents;
  }
  // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not
  // remove

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ChatLog)) {
      return false;
    }
    return id != null && id.equals(((ChatLog) o).id);
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return "ChatLog{"
        + "id="
        + getId()
        + ", log='"
        + getLog()
        + "'"
        + ", date='"
        + getDate()
        + "'"
        + "}";
  }
}
