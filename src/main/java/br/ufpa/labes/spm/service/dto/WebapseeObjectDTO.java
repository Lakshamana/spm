package br.ufpa.labes.spm.service.dto;

public class WebapseeObjectDTO {

	private Long oid;
	private String className;


	public Integer getId() {
		return oid;
	}
	public void setId(Long oid) {
		this.oid = oid;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}



}
