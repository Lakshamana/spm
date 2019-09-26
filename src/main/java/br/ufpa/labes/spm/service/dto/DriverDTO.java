package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Driver} entity.
 */
public class DriverDTO implements Serializable {

    private Long id;

    private String tipo;

    private String appKey;

    private String appSecret;

    private String appKeyGoogle;

    private String appSecretGoogle;

    private String requestUrl;


    private Long thePluginId;

    private Long companyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppKeyGoogle() {
        return appKeyGoogle;
    }

    public void setAppKeyGoogle(String appKeyGoogle) {
        this.appKeyGoogle = appKeyGoogle;
    }

    public String getAppSecretGoogle() {
        return appSecretGoogle;
    }

    public void setAppSecretGoogle(String appSecretGoogle) {
        this.appSecretGoogle = appSecretGoogle;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Long getThePluginId() {
        return thePluginId;
    }

    public void setThePluginId(Long pluginId) {
        this.thePluginId = pluginId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DriverDTO driverDTO = (DriverDTO) o;
        if (driverDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), driverDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DriverDTO{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", appKey='" + getAppKey() + "'" +
            ", appSecret='" + getAppSecret() + "'" +
            ", appKeyGoogle='" + getAppKeyGoogle() + "'" +
            ", appSecretGoogle='" + getAppSecretGoogle() + "'" +
            ", requestUrl='" + getRequestUrl() + "'" +
            ", thePlugin=" + getThePluginId() +
            ", company=" + getCompanyId() +
            "}";
    }
}
