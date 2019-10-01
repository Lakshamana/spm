package br.ufpa.labes.spm.beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ArtifactMngParamDownload implements Serializable{

	public String username; //login on WebAPSEE Artifact Management

	private String artifactIdent;

	public String artifactName;//Artifact Name (Filename)
	public String artifactPath;//Process identifier

	public String arguments;//Arguments of the operation

	public ArtifactMngParamDownload(){
		this.username = "admin";
		this.artifactIdent = "";
		this.artifactPath = "";
		this.artifactName = "";
		this.arguments = "";
	}

	/**
	 * @return the user name
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username to login on WebAPSEE Artifact Management
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the artifact name
	 */
	public String getArtifactName() {
		return artifactName;
	}

	/**
	 * @param artifactName - artifact name
	 */
	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}

	/**
	 * @return the artifact path
	 */
	public String getArtifactPath() {
		return artifactPath;
	}

	/**
	 * @param artifactPath - artifact path into the WebAPSEE Artifact Management repository
	 */
	public void setArtifactPath(String artifactPath) {
		this.artifactPath = artifactPath;
	}

	/**
	 * @return the arguments
	 */
	public String getArguments() {
		return arguments;
	}

	/**
	 * @param arguments can be a specific version to checkout an old version OR the artifact name to get version history
	 */
	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public String getArtifactIdent() {
		return artifactIdent;
	}

	public void setArtifactIdent(String artifactIdent) {
		this.artifactIdent = artifactIdent;
	}
}
