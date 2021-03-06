package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Agent} entity.
 */
public class AgentDTO implements Serializable {

    private Long id;

    private String ident;

    private String name;

    private String eMail;

    private Float costHour;

    private String password;

    private Integer tipoUser;

    private Boolean isActive;

    private Boolean online;

    @Lob
    private byte[] photo;

    private String photoContentType;
    private String upload;

    @Lob
    private String description;


    private Long theTaskAgendaId;

    private Long configurationId;

    private Long theResourceEventId;

    private Set<ProcessDTO> theProcesses = new HashSet<>();

    private Set<WorkGroupDTO> theWorkGroups = new HashSet<>();

    private Set<CompanyUnitDTO> theOrgUnits = new HashSet<>();

    private Long theEmailConfigurationId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Float getCostHour() {
        return costHour;
    }

    public void setCostHour(Float costHour) {
        this.costHour = costHour;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTipoUser() {
        return tipoUser;
    }

    public void setTipoUser(Integer tipoUser) {
        this.tipoUser = tipoUser;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTheTaskAgendaId() {
        return theTaskAgendaId;
    }

    public void setTheTaskAgendaId(Long taskAgendaId) {
        this.theTaskAgendaId = taskAgendaId;
    }

    public Long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(Long spmConfigurationId) {
        this.configurationId = spmConfigurationId;
    }

    public Long getTheResourceEventId() {
        return theResourceEventId;
    }

    public void setTheResourceEventId(Long resourceEventId) {
        this.theResourceEventId = resourceEventId;
    }

    public Set<ProcessDTO> getTheProcesses() {
        return theProcesses;
    }

    public void setTheProcesses(Set<ProcessDTO> processes) {
        this.theProcesses = processes;
    }

    public Set<WorkGroupDTO> getTheWorkGroups() {
        return theWorkGroups;
    }

    public void setTheWorkGroups(Set<WorkGroupDTO> workGroups) {
        this.theWorkGroups = workGroups;
    }

    public Set<CompanyUnitDTO> getTheOrgUnits() {
        return theOrgUnits;
    }

    public void setTheOrgUnits(Set<CompanyUnitDTO> companyUnits) {
        this.theOrgUnits = companyUnits;
    }

    public Long getTheEmailConfigurationId() {
        return theEmailConfigurationId;
    }

    public void setTheEmailConfigurationId(Long emailConfigurationId) {
        this.theEmailConfigurationId = emailConfigurationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AgentDTO agentDTO = (AgentDTO) o;
        if (agentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), agentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AgentDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", eMail='" + geteMail() + "'" +
            ", costHour=" + getCostHour() +
            ", password='" + getPassword() + "'" +
            ", tipoUser=" + getTipoUser() +
            ", isActive='" + isIsActive() + "'" +
            ", online='" + isOnline() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", upload='" + getUpload() + "'" +
            ", description='" + getDescription() + "'" +
            ", theTaskAgenda=" + getTheTaskAgendaId() +
            ", configuration=" + getConfigurationId() +
            ", theResourceEvent=" + getTheResourceEventId() +
            ", theEmailConfiguration=" + getTheEmailConfigurationId() +
            "}";
    }
}
