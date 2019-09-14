package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ConnectionType.
 */
@Entity
@Table(name = "connection_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConnectionType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "theConnectionTypeSub")
    @JsonIgnore
    private Type theTypeSuper;

    @OneToMany(mappedBy = "theConnectionType")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Connection> theConnections = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getTheTypeSuper() {
        return theTypeSuper;
    }

    public ConnectionType theTypeSuper(Type type) {
        this.theTypeSuper = type;
        return this;
    }

    public void setTheTypeSuper(Type type) {
        this.theTypeSuper = type;
    }

    public Set<Connection> getTheConnections() {
        return theConnections;
    }

    public ConnectionType theConnections(Set<Connection> connections) {
        this.theConnections = connections;
        return this;
    }

    public ConnectionType addTheConnection(Connection connection) {
        this.theConnections.add(connection);
        connection.setTheConnectionType(this);
        return this;
    }

    public ConnectionType removeTheConnection(Connection connection) {
        this.theConnections.remove(connection);
        connection.setTheConnectionType(null);
        return this;
    }

    public void setTheConnections(Set<Connection> connections) {
        this.theConnections = connections;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConnectionType)) {
            return false;
        }
        return id != null && id.equals(((ConnectionType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ConnectionType{" +
            "id=" + getId() +
            "}";
    }
}
