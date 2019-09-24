package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.GraphicCoordinate} entity.
 */
public class GraphicCoordinateDTO implements Serializable {

    private Long id;

    private Integer x;

    private Integer y;

    private Boolean visible;

    private String theProcess;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getTheProcess() {
        return theProcess;
    }

    public void setTheProcess(String theProcess) {
        this.theProcess = theProcess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GraphicCoordinateDTO graphicCoordinateDTO = (GraphicCoordinateDTO) o;
        if (graphicCoordinateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), graphicCoordinateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GraphicCoordinateDTO{" +
            "id=" + getId() +
            ", x=" + getX() +
            ", y=" + getY() +
            ", visible='" + isVisible() + "'" +
            ", theProcess='" + getTheProcess() + "'" +
            "}";
    }
}
