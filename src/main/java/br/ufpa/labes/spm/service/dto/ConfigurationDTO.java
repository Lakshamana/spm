package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


import org.qrconsult.spm.converter.annotations.IgnoreMapping;

@SuppressWarnings("serial")
@XmlRootElement(name="config")
public class ConfigurationDTO implements Serializable {

	private Integer oid;

	private String filtro;
	@IgnoreMapping
	private String tokenKey;
	@IgnoreMapping
	private String tokenSecret;

	private String idioma;
	@IgnoreMapping
	private String agent;

	private boolean graficoDeEsforco;

	private boolean graficoDeCustos;

	private boolean graficoDeDesempenho;

	private boolean graficoDeTarefas;
	@IgnoreMapping
	private boolean senhaEmRecuperacao;

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
	@XmlTransient
	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public String getTokenKey() {
		return tokenKey;
	}


	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}


	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public boolean isGraficoDeEsforco() {
		return graficoDeEsforco;
	}

	public boolean getGraficoDeEsforco() {
		return graficoDeEsforco;
	}

	public void setGraficoDeEsforco(boolean graficoDeEsforco) {
		this.graficoDeEsforco = graficoDeEsforco;
	}

	public boolean isGraficoDeCustos() {
		return graficoDeCustos;
	}

	public boolean getGraficoDeCustos() {
		return graficoDeCustos;
	}

	public void setGraficoDeCustos(boolean graficoDeCustos) {
		this.graficoDeCustos = graficoDeCustos;
	}

	public boolean isGraficoDeDesempenho() {
		return graficoDeDesempenho;
	}

	public boolean getGraficoDeDesempenho() {
		return graficoDeDesempenho;
	}

	public void setGraficoDeDesempenho(boolean graficoDeDesempenho) {
		this.graficoDeDesempenho = graficoDeDesempenho;
	}

	public boolean isGraficoDeTarefas() {
		return graficoDeTarefas;
	}

	public boolean getGraficoDeTarefas() {
		return graficoDeTarefas;
	}

	public void setGraficoDeTarefas(boolean graficoDeTarefas) {
		this.graficoDeTarefas = graficoDeTarefas;
	}

	public boolean getSenhaEmRecuperacao() {
		return senhaEmRecuperacao;
	}

	public boolean isSenhaEmRecuperacao() {
		return senhaEmRecuperacao;
	}
	public void setSenhaEmRecuperacao(boolean senhaEmRecuperacao) {
		this.senhaEmRecuperacao = senhaEmRecuperacao;
	}

}
