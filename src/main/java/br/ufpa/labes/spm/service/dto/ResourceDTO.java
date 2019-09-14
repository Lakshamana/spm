package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Resource} entity.
 */
public class ResourceDTO implements Serializable {

    private Long id;

    private String ident;

    private String name;

    @Lob
    private String description;

    private Float mtbfTime;

    private String mtbfUnitTime;

    private String currency;

    private Float cost;

    private Boolean isActive;


    private Long theConsumableSubId;

    private Long theExclusiveSubId;

    private Long theShareableSubId;

    private Long belongsToId;

    private Long theResourceTypeId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getMtbfTime() {
        return mtbfTime;
    }

    public void setMtbfTime(Float mtbfTime) {
        this.mtbfTime = mtbfTime;
    }

    public String getMtbfUnitTime() {
        return mtbfUnitTime;
    }

    public void setMtbfUnitTime(String mtbfUnitTime) {
        this.mtbfUnitTime = mtbfUnitTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getTheConsumableSubId() {
        return theConsumableSubId;
    }

    public void setTheConsumableSubId(Long consumableId) {
        this.theConsumableSubId = consumableId;
    }

    public Long getTheExclusiveSubId() {
        return theExclusiveSubId;
    }

    public void setTheExclusiveSubId(Long exclusiveId) {
        this.theExclusiveSubId = exclusiveId;
    }

    public Long getTheShareableSubId() {
        return theShareableSubId;
    }

    public void setTheShareableSubId(Long shareableId) {
        this.theShareableSubId = shareableId;
    }

    public Long getBelongsToId() {
        return belongsToId;
    }

    public void setBelongsToId(Long resourceId) {
        this.belongsToId = resourceId;
    }

    public Long getTheResourceTypeId() {
        return theResourceTypeId;
    }

    public void setTheResourceTypeId(Long resourceTypeId) {
        this.theResourceTypeId = resourceTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ResourceDTO resourceDTO = (ResourceDTO) o;
        if (resourceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), resourceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResourceDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", mtbfTime=" + getMtbfTime() +
            ", mtbfUnitTime='" + getMtbfUnitTime() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", cost=" + getCost() +
            ", isActive='" + isIsActive() + "'" +
            ", theConsumableSub=" + getTheConsumableSubId() +
            ", theExclusiveSub=" + getTheExclusiveSubId() +
            ", theShareableSub=" + getTheShareableSubId() +
            ", belongsTo=" + getBelongsToId() +
            ", theResourceType=" + getTheResourceTypeId() +
            "}";
    }
}
