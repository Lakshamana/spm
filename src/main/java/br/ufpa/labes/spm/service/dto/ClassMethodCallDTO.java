package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.ClassMethodCall} entity.
 */
public class ClassMethodCallDTO implements Serializable {

    private Long id;

    private String ident;

    private String className;

    private String methodName;

    @Lob
    private String description;


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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassMethodCallDTO classMethodCallDTO = (ClassMethodCallDTO) o;
        if (classMethodCallDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classMethodCallDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassMethodCallDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", className='" + getClassName() + "'" +
            ", methodName='" + getMethodName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
