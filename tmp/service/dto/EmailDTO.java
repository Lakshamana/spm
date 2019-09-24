package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class EmailDTO implements Serializable {

	private Long id;

	private String serverHost;

	private String serverPort;

	private String userName;

	private Boolean  servicoTls;

	private Boolean  teste;

	private Boolean  servicoSsl;

	private String password;

	public Boolean isServicoTls() {
		return servicoTls;
	}

	public Boolean setServicoTls(Boolean servicoTls) {
		return this.servicoTls = servicoTls;
	}

	public Boolean isServicoSsl() {
		return servicoSsl;
	}

	public Boolean setTeste(Boolean teste) {
		return this.teste = teste;
	}

	public Boolean isTeste() {
		return teste;
	}

	public Boolean setServicoSsl(Boolean servicoSsl) {
		return this.servicoSsl = servicoSsl;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getServerPort() {
		return serverPort;
	}


	public void setServerPort(String serverp) {
		this.serverPort = serverp;
	}

	public String getServerHost() {
		return serverHost;
	}


	public void setServerHost(String serverh) {
		this.serverHost = serverh;
	}

	public String getUserName() {
		return userName;
	}


	public void setUserName(String user) {
		this.userName = user;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


}

