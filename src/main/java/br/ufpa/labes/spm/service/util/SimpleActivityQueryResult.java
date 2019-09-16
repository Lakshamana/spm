package br.ufpa.labes.spm.service.util;

import java.io.Serializable;

public class SimpleActivityQueryResult implements Serializable{

	String ident;

	String name;

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

}
