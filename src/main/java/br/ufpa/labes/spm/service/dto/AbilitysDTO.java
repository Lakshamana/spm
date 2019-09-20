package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AbilitysDTO implements Serializable {
	private String[] nameAbilitys;
	private String[] identAbilitys;
	private String[] typeIdents;

	public AbilitysDTO() {
		this.setTypeIdents(new String[0]);
		this.setNameAbilitys(new String[0]);
	}

	public AbilitysDTO(int sizeList){
		this.typeIdents = new String[sizeList];
	}

	public String[] getTypeIdents() {
		return typeIdents;
	}

	public void setTypeIdents(String[] typeIdents) {
		this.typeIdents = typeIdents;
	}

	public String[] getNameAbilitys() {
		return nameAbilitys;
	}

	public void setNameAbilitys(String[] nameAbilitys) {
		this.nameAbilitys = nameAbilitys;
	}

	public String[] getIdentAbilitys() {
		return identAbilitys;
	}

	public void setIdentAbilitys(String[] identAbilitys) {
		this.identAbilitys = identAbilitys;
	}

}
