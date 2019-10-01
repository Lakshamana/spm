package br.ufpa.labes.spm.beans;

import java.io.Serializable;
import java.util.Hashtable;

public class ArtifactMngLogPackage implements Serializable{

	private Hashtable<String, ArtifactMngLog> logs;

	public ArtifactMngLogPackage(){
		this.logs = new Hashtable<String, ArtifactMngLog>();
	}

	public void addArtifact(String artifactIdent, ArtifactMngLog log){
		this.logs.put(artifactIdent, log);
	}

	public ArtifactMngLog getArtifactHistory(String artifactIdent){
		return this.logs.get(artifactIdent);
	}

	public String[] getArtifactIdents(){
		String[] artifactIdents = new String[logs.size()];
		this.logs.keySet().toArray(artifactIdents);
		return artifactIdents;
	}

	public Hashtable<String, ArtifactMngLog> getLogs(){
		return logs;
	}
}
