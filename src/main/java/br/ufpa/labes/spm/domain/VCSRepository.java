package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A VCSRepository.
 */
@Entity
@Table(name = "vcs_repository")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VCSRepository implements Serializable {

    public static final transient String
    CVS = "CVS",
    SVN = "SVN";

    public static final transient String
    CVS_PSERVER_CONNECTIONTYPE = "pserver",
    CVS_SERVER_CONNECTIONTYPE = "server",
    CVS_SSHSERVER_CONNECTIONTYPE = "sshserver",
    SVN_HTTP_PROTOCOL = "http",
    SVN_HTTPS_PROTOCOL = "https";

    public static final transient String
    CVS_PSERVER_PORT = "2401",
    CVS_SERVER_PORT = "504",
    CVS_SSHSERVER_PORT = "22",
    SVN_HTTP_PORT = "80",
    SVN_HTTPS_PORT = "443";

    public static final transient String
    DEFAULT_REPOSITORY = "default";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "control_version_system")
    private String controlVersionSystem;

    @Column(name = "server")
    private String server;

    @Column(name = "port")
    private String port;

    @Column(name = "connection_method")
    private String connectionMethod;

    @Column(name = "repository")
    private String repository;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "default_user")
    private Boolean defaultUser;

    @ManyToOne
    @JsonIgnoreProperties("theRepositories")
    private Structure theStructure;

    @OneToMany(mappedBy = "theRepository")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Artifact> theArtifacts = new HashSet<>();

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

    public VCSRepository ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getControlVersionSystem() {
        return controlVersionSystem;
    }

    public VCSRepository controlVersionSystem(String controlVersionSystem) {
        this.controlVersionSystem = controlVersionSystem;
        return this;
    }

    public void setControlVersionSystem(String controlVersionSystem) {
        this.controlVersionSystem = controlVersionSystem;
    }

    public String getServer() {
        return server;
    }

    public VCSRepository server(String server) {
        this.server = server;
        return this;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public VCSRepository port(String port) {
        this.port = port;
        return this;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getConnectionMethod() {
        return connectionMethod;
    }

    public VCSRepository connectionMethod(String connectionMethod) {
        this.connectionMethod = connectionMethod;
        return this;
    }

    public void setConnectionMethod(String connectionMethod) {
        this.connectionMethod = connectionMethod;
    }

    public String getRepository() {
        return repository;
    }

    public VCSRepository repository(String repository) {
        this.repository = repository;
        return this;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getUsername() {
        return username;
    }

    public VCSRepository username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public VCSRepository password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isDefaultUser() {
        return defaultUser;
    }

    public VCSRepository defaultUser(Boolean defaultUser) {
        this.defaultUser = defaultUser;
        return this;
    }

    public void setDefaultUser(Boolean defaultUser) {
        this.defaultUser = defaultUser;
    }

    public Structure getTheStructure() {
        return theStructure;
    }

    public VCSRepository theStructure(Structure structure) {
        this.theStructure = structure;
        return this;
    }

    public void setTheStructure(Structure structure) {
        this.theStructure = structure;
    }

    public Set<Artifact> getTheArtifacts() {
        return theArtifacts;
    }

    public VCSRepository theArtifacts(Set<Artifact> artifacts) {
        this.theArtifacts = artifacts;
        return this;
    }

    public VCSRepository addTheArtifacts(Artifact artifact) {
        this.theArtifacts.add(artifact);
        artifact.setTheRepository(this);
        return this;
    }

    public VCSRepository removeTheArtifacts(Artifact artifact) {
        this.theArtifacts.remove(artifact);
        artifact.setTheRepository(null);
        return this;
    }

    public void setTheArtifacts(Set<Artifact> artifacts) {
        this.theArtifacts = artifacts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VCSRepository)) {
            return false;
        }
        return id != null && id.equals(((VCSRepository) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "VCSRepository{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", controlVersionSystem='" + getControlVersionSystem() + "'" +
            ", server='" + getServer() + "'" +
            ", port='" + getPort() + "'" +
            ", connectionMethod='" + getConnectionMethod() + "'" +
            ", repository='" + getRepository() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", defaultUser='" + isDefaultUser() + "'" +
            "}";
    }
}
