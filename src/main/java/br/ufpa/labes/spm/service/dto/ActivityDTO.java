package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.Activity} entity.
 */
public class ActivityDTO implements Serializable {

    private Long id;

    private String ident;

    private String name;

    private Boolean isVersion;


    private Long theActivityTypeId;

    private Set<JoinConDTO> toJoinCons = new HashSet<>();

    private Set<BranchANDConDTO> fromBranchANDCons = new HashSet<>();

    private Set<ArtifactConDTO> fromArtifactCons = new HashSet<>();

    private Set<ArtifactConDTO> toArtifactCons = new HashSet<>();

    private Long isVersionOfId;

    private Long theProcessModelId;

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

    public Boolean isIsVersion() {
        return isVersion;
    }

    public void setIsVersion(Boolean isVersion) {
        this.isVersion = isVersion;
    }

    public Long getTheActivityTypeId() {
        return theActivityTypeId;
    }

    public void setTheActivityTypeId(Long activityTypeId) {
        this.theActivityTypeId = activityTypeId;
    }

    public Set<JoinConDTO> getToJoinCons() {
        return toJoinCons;
    }

    public void setToJoinCons(Set<JoinConDTO> joinCons) {
        this.toJoinCons = joinCons;
    }

    public Set<BranchANDConDTO> getFromBranchANDCons() {
        return fromBranchANDCons;
    }

    public void setFromBranchANDCons(Set<BranchANDConDTO> branchANDCons) {
        this.fromBranchANDCons = branchANDCons;
    }

    public Set<ArtifactConDTO> getFromArtifactCons() {
        return fromArtifactCons;
    }

    public void setFromArtifactCons(Set<ArtifactConDTO> artifactCons) {
        this.fromArtifactCons = artifactCons;
    }

    public Set<ArtifactConDTO> getToArtifactCons() {
        return toArtifactCons;
    }

    public void setToArtifactCons(Set<ArtifactConDTO> artifactCons) {
        this.toArtifactCons = artifactCons;
    }

    public Long getIsVersionOfId() {
        return isVersionOfId;
    }

    public void setIsVersionOfId(Long activityId) {
        this.isVersionOfId = activityId;
    }

    public Long getTheProcessModelId() {
        return theProcessModelId;
    }

    public void setTheProcessModelId(Long processModelId) {
        this.theProcessModelId = processModelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityDTO activityDTO = (ActivityDTO) o;
        if (activityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityDTO{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", isVersion='" + isIsVersion() + "'" +
            ", theActivityType=" + getTheActivityTypeId() +
            ", isVersionOf=" + getIsVersionOfId() +
            ", theProcessModel=" + getTheProcessModelId() +
            "}";
    }
}
