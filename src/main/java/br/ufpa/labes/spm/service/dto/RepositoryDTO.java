package br.ufpa.labes.spm.service.dto;

//import Oid;

import java.io.Serializable;

import org.qrconsult.spm.converter.annotations.IgnoreMapping;


@SuppressWarnings("serial")
public class RepositoryDTO implements Serializable {

	private Integer oid;
	private String ident;
	private String controlVersionSystem;
	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	private String server;
	private String port;
	private String connectionMethod;
	private String repository;
	private String username;
	private String password;

	private boolean defaultUser;

	@IgnoreMapping
	private StructureDTO theStructure;

	public RepositoryDTO() {
		super();
		this.setControlVersionSystem("");// Repository.CVS;
		this.setIdent("");
		this.server = "localhost";
		this.port = "2401";
		this.connectionMethod = "";//Repository.CVS_PSERVER_CONNECTIONTYPE;
		this.repository = "/var/cvs";
		this.username = "";
		this.setPassword("");
		this.defaultUser = false;
		this.theStructure = null;
	}




	public StructureDTO getTheStructure() {
		return theStructure;
	}

	public void setTheStructure(StructureDTO theStructure) {
		this.theStructure = theStructure;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getControlVersionSystem() {
		return controlVersionSystem;
	}

	public void setControlVersionSystem(String controlVersionSystem) {
		this.controlVersionSystem = controlVersionSystem;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getConnectionMethod() {
		return connectionMethod;
	}

	public void setConnectionMethod(String connectionMethod) {
		this.connectionMethod = connectionMethod;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isDefaultUser() {
		return defaultUser;
	}

	public boolean getDefaultUser() {
		return defaultUser;
	}

	public void setDefaultUser(boolean defaultUser) {
		this.defaultUser = defaultUser;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	@Override
	public String toString() {
		return "repository=" + repository + ", theStructure="
				+ theStructure;
	}



}


