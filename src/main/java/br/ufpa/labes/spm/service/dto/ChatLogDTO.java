package br.ufpa.labes.spm.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ChatLog} entity.
 */
public class ChatLogDTO implements Serializable {

    private Long id;

    private String log;

    private LocalDate date;


    private Set<AgentDTO> involvedAgentsInChats = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<AgentDTO> getInvolvedAgentsInChats() {
        return involvedAgentsInChats;
    }

    public void setInvolvedAgentsInChats(Set<AgentDTO> agents) {
        this.involvedAgentsInChats = agents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChatLogDTO chatLogDTO = (ChatLogDTO) o;
        if (chatLogDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chatLogDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChatLogDTO{" +
            "id=" + getId() +
            ", log='" + getLog() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
