package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TypesDTO implements Serializable{

	private String[] typeIdents;
	private String[] superTypes;
	private String[] rootTypes;

	public TypesDTO(){
		this.typeIdents = new String[0];
		this.superTypes = new String[0];
		this.rootTypes  = new String[0];
	}

	public TypesDTO(int numTypes){
		this.typeIdents = new String[numTypes];
		this.superTypes = new String[numTypes];
		this.rootTypes  = new String[numTypes];
	}

	public void addType(String typeIdent, String superTypeIdent, String rootType, int index){
		this.typeIdents[index] = typeIdent;
		this.superTypes[index] = superTypeIdent;
		this.rootTypes[index]  = rootType;
	}

	public String[] getTypeIdents() {
		return typeIdents;
	}

	public void setTypeIdents(String[] typeIdents) {
		this.typeIdents = typeIdents;
	}

	public String[] getSuperTypes() {
		return superTypes;
	}

	public void setSuperTypes(String[] superTypes) {
		this.superTypes = superTypes;
	}

	public String[] getRootTypes() {
		return rootTypes;
	}

	public void setRootTypes(String[] rootTypes) {
		this.rootTypes = rootTypes;
	}

}
