package br.ufpa.labes.spm.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/** A DTO for the {@link br.ufpa.labes.spm.domain.Message} entity. */
public class MessageDTO implements Serializable {

  private Long id;

  @Lob private String content;

  private LocalDate time;

  private Long senderId;

  private Long recipientId;

  private Long theAssetId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDate getTime() {
    return time;
  }

  public void setTime(LocalDate time) {
    this.time = time;
  }

  public Long getSenderId() {
    return senderId;
  }

  public void setSenderId(Long authorId) {
    this.senderId = authorId;
  }

  public Long getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(Long authorId) {
    this.recipientId = authorId;
  }

  public Long getTheAssetId() {
    return theAssetId;
  }

  public void setTheAssetId(Long assetId) {
    this.theAssetId = assetId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MessageDTO messageDTO = (MessageDTO) o;
    if (messageDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), messageDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "MessageDTO{"
        + "id="
        + getId()
        + ", content='"
        + getContent()
        + "'"
        + ", time='"
        + getTime()
        + "'"
        + ", sender="
        + getSenderId()
        + ", recipient="
        + getRecipientId()
        + ", theAsset="
        + getTheAssetId()
        + "}";
  }
}
