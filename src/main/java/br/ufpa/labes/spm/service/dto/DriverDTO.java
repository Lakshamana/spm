package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DriverDTO implements Serializable{


	/**
	 *
	 */
	private String appKeyGoogle;


	private String appSecretGoogle;

	private Integer oid;


	private String tipo;


	private String appKey;


	private String appSecret;


	private String requestUrl;


	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getAppKeyGoogle() {
		return appKeyGoogle;
	}


	public void setAppKeyGoogle(String appKeyGoogle) {
		this.appKeyGoogle = appKeyGoogle;
	}

	public String getAppSecretGoogle() {
		return appSecretGoogle;
	}


	public void setAppSecretGoogle(String appSecretGoogle) {
		this.appSecretGoogle = appSecretGoogle;
	}

}
