package br.ufpa.labes.spm.beans;

import java.io.Serializable;


public class ArtifactMngVersion implements Serializable{

	private String revision;
	private String date;
	private String author;
	private String comment;
	private String activity;
	private String binaries;

	public ArtifactMngVersion(){
		super();
		this.revision = "";
		this.date = "";
		this.author = "";
		this.comment = "";
		this.activity = "";
		this.binaries = "";
	}

	public ArtifactMngVersion(String revision, String date, String author, String comment, String activity, String binaries){
		this.revision = revision;
		this.date = date;
		this.author = author;
		this.comment = comment;
		this.activity = activity;
		this.binaries = binaries;
	}

	/*
	 * Return the author
	 */
	public String getAuthor() {
		return author;
	}

	/*
	 * Return the comment
	 */
	public String getComment() {
		return comment;
	}

	/*
	 * Return the date
	 */
	public String getDate() {
		return date;
	}

	/*
	 * Return the revision number
	 */
	public String getRevision() {
		return revision;
	}

	/*
	 * Return the activity ident that generated this version
	 */
	public String getActivity() {
		return activity;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String toString() {
		String out = new String();
		out = "Version : --> : Revision :" + getRevision()
		+ " | Date : " + getDate()
		+ " | Author : " + getAuthor()
		+ " | Activity : " + getActivity()
		+ " | Comment : " + getComment()
		+ " | Binaries : " + getBinaries();
		return out;
	}

	public String getBinaries() {
		return binaries;
	}

	public void setBinaries(String binaries) {
		this.binaries = binaries;
	}
}
