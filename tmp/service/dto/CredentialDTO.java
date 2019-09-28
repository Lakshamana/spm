package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Credential} entity.
 */
public class CredentialDTO implements Serializable {

    private Long id;

    private String className;

    private String uid;


    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CredentialDTO credentialDTO = (CredentialDTO) o;
        if (credentialDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), credentialDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CredentialDTO{" +
            "id=" + getId() +
            ", className='" + getClassName() + "'" +
            ", uid='" + getUid() + "'" +
            ", user=" + getUserId() +
            "}";
    }
}
