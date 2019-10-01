package br.ufpa.labes.spm.beans;

import java.io.Serializable;


public class ArtifactMngLog implements Serializable{

	private ArtifactMngVersion[] log;
	private int index;

	public ArtifactMngLog(int count){
		log = new ArtifactMngVersion[count];
		index=0;
	}

	public ArtifactMngLog() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * Return an specific version. The first version is the lastest version
	 */
	public ArtifactMngVersion getVersion(int index){
		return log[index];
	}

	/*
	 * Add a version to the log
	 */
	public void addVersion(ArtifactMngVersion version){
		log[index] = version;
		index++;
	}

	/*
	 * Return the number of revisions
	 */
	public int getVersionsNumber(){
		return index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ArtifactMngVersion[] getLog() {
		return log;
	}

	public void setLog(ArtifactMngVersion[] log) {
		this.log = log;
	}



}
