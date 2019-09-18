package br.ufpa.labes.spm.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Email.
 */
@Entity
@Table(name = "email")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_server_host")
    private String emailServerHost;

    @Column(name = "email_server_port")
    private String emailServerPort;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "servico_tls")
    private Boolean servicoTls;

    @Column(name = "servico_ssl")
    private Boolean servicoSsl;

    @Column(name = "teste")
    private Boolean teste;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailServerHost() {
        return emailServerHost;
    }

    public Email emailServerHost(String emailServerHost) {
        this.emailServerHost = emailServerHost;
        return this;
    }

    public void setEmailServerHost(String emailServerHost) {
        this.emailServerHost = emailServerHost;
    }

    public String getEmailServerPort() {
        return emailServerPort;
    }

    public Email emailServerPort(String emailServerPort) {
        this.emailServerPort = emailServerPort;
        return this;
    }

    public void setEmailServerPort(String emailServerPort) {
        this.emailServerPort = emailServerPort;
    }

    public String getUserName() {
        return userName;
    }

    public Email userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Email passwordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean isServicoTls() {
        return servicoTls;
    }

    public Email servicoTls(Boolean servicoTls) {
        this.servicoTls = servicoTls;
        return this;
    }

    public void setServicoTls(Boolean servicoTls) {
        this.servicoTls = servicoTls;
    }

    public Boolean isServicoSsl() {
        return servicoSsl;
    }

    public Email servicoSsl(Boolean servicoSsl) {
        this.servicoSsl = servicoSsl;
        return this;
    }

    public void setServicoSsl(Boolean servicoSsl) {
        this.servicoSsl = servicoSsl;
    }

    public Boolean isTeste() {
        return teste;
    }

    public Email teste(Boolean teste) {
        this.teste = teste;
        return this;
    }

    public void setTeste(Boolean teste) {
        this.teste = teste;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email)) {
            return false;
        }
        return id != null && id.equals(((Email) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Email{" +
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
