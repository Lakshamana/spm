package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A SpmConfiguration.
 */
@Entity
@Table(name = "spm_configuration")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SpmConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filtro")
    private String filtro;

    @Column(name = "idioma")
    private String idioma;

    @Column(name = "grafico_de_esforco")
    private Boolean graficoDeEsforco;

    @Column(name = "grafico_de_custos")
    private Boolean graficoDeCustos;

    @Column(name = "grafico_de_desempenho")
    private Boolean graficoDeDesempenho;

    @Column(name = "grafico_de_tarefas")
    private Boolean graficoDeTarefas;

    @Column(name = "senha_em_recuperacao")
    private Boolean senhaEmRecuperacao;

    @OneToOne(mappedBy = "configuration")
    @JsonIgnore
    private Agent agent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiltro() {
        return filtro;
    }

    public SpmConfiguration filtro(String filtro) {
        this.filtro = filtro;
        return this;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public String getIdioma() {
        return idioma;
    }

    public SpmConfiguration idioma(String idioma) {
        this.idioma = idioma;
        return this;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Boolean isGraficoDeEsforco() {
        return graficoDeEsforco;
    }

    public SpmConfiguration graficoDeEsforco(Boolean graficoDeEsforco) {
        this.graficoDeEsforco = graficoDeEsforco;
        return this;
    }

    public void setGraficoDeEsforco(Boolean graficoDeEsforco) {
        this.graficoDeEsforco = graficoDeEsforco;
    }

    public Boolean isGraficoDeCustos() {
        return graficoDeCustos;
    }

    public SpmConfiguration graficoDeCustos(Boolean graficoDeCustos) {
        this.graficoDeCustos = graficoDeCustos;
        return this;
    }

    public void setGraficoDeCustos(Boolean graficoDeCustos) {
        this.graficoDeCustos = graficoDeCustos;
    }

    public Boolean isGraficoDeDesempenho() {
        return graficoDeDesempenho;
    }

    public SpmConfiguration graficoDeDesempenho(Boolean graficoDeDesempenho) {
        this.graficoDeDesempenho = graficoDeDesempenho;
        return this;
    }

    public void setGraficoDeDesempenho(Boolean graficoDeDesempenho) {
        this.graficoDeDesempenho = graficoDeDesempenho;
    }

    public Boolean isGraficoDeTarefas() {
        return graficoDeTarefas;
    }

    public SpmConfiguration graficoDeTarefas(Boolean graficoDeTarefas) {
        this.graficoDeTarefas = graficoDeTarefas;
        return this;
    }

    public void setGraficoDeTarefas(Boolean graficoDeTarefas) {
        this.graficoDeTarefas = graficoDeTarefas;
    }

    public Boolean isSenhaEmRecuperacao() {
        return senhaEmRecuperacao;
    }

    public SpmConfiguration senhaEmRecuperacao(Boolean senhaEmRecuperacao) {
        this.senhaEmRecuperacao = senhaEmRecuperacao;
        return this;
    }

    public void setSenhaEmRecuperacao(Boolean senhaEmRecuperacao) {
        this.senhaEmRecuperacao = senhaEmRecuperacao;
    }

    public Agent getAgent() {
        return agent;
    }

    public SpmConfiguration agent(Agent agent) {
        this.agent = agent;
        return this;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpmConfiguration)) {
            return false;
        }
        return id != null && id.equals(((SpmConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SpmConfiguration{" +
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
