package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/** A DTO for the {@link br.ufpa.labes.spm.domain.ChatMessage} entity. */
public class ChatMessageDTO implements Serializable {

  private Long id;

  private String ident;

  private String text;

  @NotNull private LocalDate timestamp;

  private Long fromAgentId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdent() {
    return ident;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public LocalDate getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  public Long getFromAgentId() {
    return fromAgentId;
  }

  public void setFromAgentId(Long agentId) {
    this.fromAgentId = agentId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ChatMessageDTO chatMessageDTO = (ChatMessageDTO) o;
    if (chatMessageDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), chatMessageDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "ChatMessageDTO{"
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
        + ", fromAgent="
        + getFromAgentId()
        + "}";
  }
}
