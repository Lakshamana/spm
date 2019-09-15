package br.ufpa.labes.spm.util;

public final class Translator {

	public static String getGlobalPackageName(String className) {
		return UtilLabels.getPropertie("global." + className).trim();
	}

	public static String getDAOPackageName(String className) {
		return UtilLabels.getPropertie("dao." + className).trim();
	}

	public static String getInternalPackageName(String className) {
		return UtilLabels.getPropertie("internal." + className).trim();
	}

	public static String getSuper(String className) {
		return UtilLabels.getPropertie("super." + className).trim();
	}

	public static String getBackOperation(String ownerClassName,String relationName) {
		String temp = UtilLabels.getPropertie("backMethod." +ownerClassName+"."+relationName);
		if(temp!=null){ return temp.trim();}else return temp;
	}
}
