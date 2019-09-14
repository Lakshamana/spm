package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.JoinCon} entity.
 */
public class JoinConDTO implements Serializable {

    private Long id;

    private String kindJoinCon;


    private Set<MultipleConDTO> fromMultipleCons = new HashSet<>();

    private Long toActivityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKindJoinCon() {
        return kindJoinCon;
    }

    public void setKindJoinCon(String kindJoinCon) {
        this.kindJoinCon = kindJoinCon;
    }

    public Set<MultipleConDTO> getFromMultipleCons() {
        return fromMultipleCons;
    }

    public void setFromMultipleCons(Set<MultipleConDTO> multipleCons) {
        this.fromMultipleCons = multipleCons;
    }

    public Long getToActivityId() {
        return toActivityId;
    }

    public void setToActivityId(Long activityId) {
        this.toActivityId = activityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JoinConDTO joinConDTO = (JoinConDTO) o;
        if (joinConDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), joinConDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "JoinConDTO{" +
            "id=" + getId() +
            ", kindJoinCon='" + getKindJoinCon() + "'" +
            ", toActivity=" + getToActivityId() +
            "}";
    }
}
