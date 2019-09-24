package br.ufpa.labes.spm.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link br.ufpa.labes.spm.domain.SpmConfiguration} entity.
 */
public class SpmConfigurationDTO implements Serializable {

    private Long id;

    private String filtro;

    private String idioma;

    private Boolean graficoDeEsforco;

    private Boolean graficoDeCustos;

    private Boolean graficoDeDesempenho;

    private Boolean graficoDeTarefas;

    private Boolean senhaEmRecuperacao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Boolean isGraficoDeEsforco() {
        return graficoDeEsforco;
    }

    public void setGraficoDeEsforco(Boolean graficoDeEsforco) {
        this.graficoDeEsforco = graficoDeEsforco;
    }

    public Boolean isGraficoDeCustos() {
        return graficoDeCustos;
    }

    public void setGraficoDeCustos(Boolean graficoDeCustos) {
        this.graficoDeCustos = graficoDeCustos;
    }

    public Boolean isGraficoDeDesempenho() {
        return graficoDeDesempenho;
    }

    public void setGraficoDeDesempenho(Boolean graficoDeDesempenho) {
        this.graficoDeDesempenho = graficoDeDesempenho;
    }

    public Boolean isGraficoDeTarefas() {
        return graficoDeTarefas;
    }

    public void setGraficoDeTarefas(Boolean graficoDeTarefas) {
        this.graficoDeTarefas = graficoDeTarefas;
    }

    public Boolean isSenhaEmRecuperacao() {
        return senhaEmRecuperacao;
    }

    public void setSenhaEmRecuperacao(Boolean senhaEmRecuperacao) {
        this.senhaEmRecuperacao = senhaEmRecuperacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpmConfigurationDTO spmConfigurationDTO = (SpmConfigurationDTO) o;
        if (spmConfigurationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), spmConfigurationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SpmConfigurationDTO{" +
            "id=" + getId() +
            ", filtro='" + getFiltro() + "'" +
            ", idioma='" + getIdioma() + "'" +
            ", graficoDeEsforco='" + isGraficoDeEsforco() + "'" +
            ", graficoDeCustos='" + isGraficoDeCustos() + "'" +
            ", graficoDeDesempenho='" + isGraficoDeDesempenho() + "'" +
            ", graficoDeTarefas='" + isGraficoDeTarefas() + "'" +
            ", senhaEmRecuperacao='" + isSenhaEmRecuperacao() + "'" +
            "}";
    }
}
