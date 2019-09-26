package br.ufpa.labes.spm.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Plugin} entity.
 */
public class PluginDTO implements Serializable {

    private Long id;

    
    private String name;

    @NotNull
    private String developerName;

    @NotNull
    private String configFilePath;


    private Long theCompanyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public String getConfigFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public Long getTheCompanyId() {
        return theCompanyId;
    }

    public void setTheCompanyId(Long userId) {
        this.theCompanyId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PluginDTO pluginDTO = (PluginDTO) o;
        if (pluginDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pluginDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PluginDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", developerName='" + getDeveloperName() + "'" +
            ", configFilePath='" + getConfigFilePath() + "'" +
            ", theCompany=" + getTheCompanyId() +
            "}";
    }
}
