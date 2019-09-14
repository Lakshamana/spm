package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Node.
 */
@Entity
@Table(name = "node")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "data")
    private String data;

    @Column(name = "service_file_id")
    private String serviceFileId;

    @OneToMany(mappedBy = "theNode")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Node> children = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("children")
    private Node theNode;

    @OneToMany(mappedBy = "rootElement")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Structure> theStructures = new HashSet<>();

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

    public Node ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getData() {
        return data;
    }

    public Node data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getServiceFileId() {
        return serviceFileId;
    }

    public Node serviceFileId(String serviceFileId) {
        this.serviceFileId = serviceFileId;
        return this;
    }

    public void setServiceFileId(String serviceFileId) {
        this.serviceFileId = serviceFileId;
    }

    public Set<Node> getChildren() {
        return children;
    }

    public Node children(Set<Node> nodes) {
        this.children = nodes;
        return this;
    }

    public Node addChildren(Node node) {
        this.children.add(node);
        node.setTheNode(this);
        return this;
    }

    public Node removeChildren(Node node) {
        this.children.remove(node);
        node.setTheNode(null);
        return this;
    }

    public void setChildren(Set<Node> nodes) {
        this.children = nodes;
    }

    public Node getTheNode() {
        return theNode;
    }

    public Node theNode(Node node) {
        this.theNode = node;
        return this;
    }

    public void setTheNode(Node node) {
        this.theNode = node;
    }

    public Set<Structure> getTheStructures() {
        return theStructures;
    }

    public Node theStructures(Set<Structure> structures) {
        this.theStructures = structures;
        return this;
    }

    public Node addTheStructure(Structure structure) {
        this.theStructures.add(structure);
        structure.setRootElement(this);
        return this;
    }

    public Node removeTheStructure(Structure structure) {
        this.theStructures.remove(structure);
        structure.setRootElement(null);
        return this;
    }

    public void setTheStructures(Set<Structure> structures) {
        this.theStructures = structures;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Node)) {
            return false;
        }
        return id != null && id.equals(((Node) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Node{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", data='" + getData() + "'" +
            ", serviceFileId='" + getServiceFileId() + "'" +
            "}";
    }
}
