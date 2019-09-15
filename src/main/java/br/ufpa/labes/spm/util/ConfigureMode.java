package br.ufpa.labes.spm.util;

public class ConfigureMode {

	//This variable is used only when WebApsee is running in OffLine Web context, wich need
	//to know the realPath of app
	public static String realPath;

	//This variable is used for indicated if the version of app is web or desktop
	public static boolean webContext = false;

	public ConfigureMode() {
		realPath = "";
		webContext = false;
	}
}
