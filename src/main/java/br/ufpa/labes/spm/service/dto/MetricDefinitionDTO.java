package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.MetricDefinition} entity.
 */
public class MetricDefinitionDTO implements Serializable {

    private Long id;

    private String name;

    @Lob
    private String description;

    private String kind;

    private Float rangeFrom;

    private Float rangeTo;

    @Lob
    private String howToMeasure;


    private Long metricTypeId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Float getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(Float rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public Float getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(Float rangeTo) {
        this.rangeTo = rangeTo;
    }

    public String getHowToMeasure() {
        return howToMeasure;
    }

    public void setHowToMeasure(String howToMeasure) {
        this.howToMeasure = howToMeasure;
    }

    public Long getMetricTypeId() {
        return metricTypeId;
    }

    public void setMetricTypeId(Long metricTypeId) {
        this.metricTypeId = metricTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MetricDefinitionDTO metricDefinitionDTO = (MetricDefinitionDTO) o;
        if (metricDefinitionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), metricDefinitionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MetricDefinitionDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", kind='" + getKind() + "'" +
            ", rangeFrom=" + getRangeFrom() +
            ", rangeTo=" + getRangeTo() +
            ", howToMeasure='" + getHowToMeasure() + "'" +
            ", metricType=" + getMetricTypeId() +
            "}";
    }
}
