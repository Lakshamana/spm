package br.ufpa.labes.spm.annotations;

public enum EnumCriteriaType {

	EQUALS("="),
	LIKE("like");

	private String formattedText;

	private EnumCriteriaType(String formattedText) {
		this.formattedText = formattedText;
	}

	public String getFormattedText() {
		return formattedText;
	}

}
