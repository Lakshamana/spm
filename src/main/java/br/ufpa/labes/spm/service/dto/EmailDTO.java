package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Email} entity.
 */
public class EmailDTO implements Serializable {

    private Long id;

    private String emailServerHost;

    private String emailServerPort;

    private String userName;

    private String passwordHash;

    private Boolean servicoTls;

    private Boolean servicoSsl;

    private Boolean teste;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailServerHost() {
        return emailServerHost;
    }

    public void setEmailServerHost(String emailServerHost) {
        this.emailServerHost = emailServerHost;
    }

    public String getEmailServerPort() {
        return emailServerPort;
    }

    public void setEmailServerPort(String emailServerPort) {
        this.emailServerPort = emailServerPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean isServicoTls() {
        return servicoTls;
    }

    public void setServicoTls(Boolean servicoTls) {
        this.servicoTls = servicoTls;
    }

    public Boolean isServicoSsl() {
        return servicoSsl;
    }

    public void setServicoSsl(Boolean servicoSsl) {
        this.servicoSsl = servicoSsl;
    }

    public Boolean isTeste() {
        return teste;
    }

    public void setTeste(Boolean teste) {
        this.teste = teste;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EmailDTO emailDTO = (EmailDTO) o;
        if (emailDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), emailDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EmailDTO{" +
            "id=" + getId() +
            ", emailServerHost='" + getEmailServerHost() + "'" +
            ", emailServerPort='" + getEmailServerPort() + "'" +
            ", userName='" + getUserName() + "'" +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", servicoTls='" + isServicoTls() + "'" +
            ", servicoSsl='" + isServicoSsl() + "'" +
            ", teste='" + isTeste() + "'" +
            "}";
    }
}
