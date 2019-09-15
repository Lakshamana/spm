package br.ufpa.labes.spm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ArtifactManagementFileConfigurationUtil {

	public static String getProperty(String value) {
		return cfg_file.getProperty(value);
	}

	public static void setControlVersionSystemProperty(String value) {
		cfg_file.setProperty("controlVersionSystem", value);
	}

	public static void setEnactmentKey(String value) {
		cfg_file.setProperty("enactmentkey", value);
	}

	public static String getEnactmentKeyProperty() {
		return cfg_file.getProperty("enactmentkey");
	}

	public static void setServerProperty(String value) {
		cfg_file.setProperty("server", value);
	}

	public static void setRepositoryProperty(String value) {
		cfg_file.setProperty("repository", value);
	}

	public static void setConnectionMethodProperty(String value) {
		cfg_file.setProperty("connectionMethod", value);
	}

	public static void setPortProperty(String value) {
		cfg_file.setProperty("port", value);
	}

	public static void setIsPserverProperty(String value) {
		cfg_file.setProperty("isPserver", value);
	}

	public static void setIsDefaultUser(String value) {
		cfg_file.setProperty("isDefaultUser", value);
	}

	public static String getControlVersionSystemProperty() {
		return cfg_file.getProperty("controlVersionSystem");
	}

	public static String getServerProperty() {
		return cfg_file.getProperty("server");
	}

	public static String getRepositoryProperty() {
		return cfg_file.getProperty("repository");
	}

	public static String getConnectionMethodProperty() {
		return cfg_file.getProperty("connectionMethod");
	}

	public static String getPortProperty() {
		return cfg_file.getProperty("port");
	}

	public static String getIsPserverProperty() {
		return cfg_file.getProperty("isPserver");
	}

	public static String getIsDefaultUser() {
		return cfg_file.getProperty("isDefaultUser");
	}

	private static Properties cfg_file = null;

	static {
		cfg_file = new Properties();

		try {
			if (!ConfigureMode.webContext) {
				cfg_file.load(new FileInputStream(new File(
						"artifactManagement-config.cfg")));
			} else {
				cfg_file.load(new FileInputStream(new File(
						ConfigureMode.realPath + File.separator
								+ "artifactManagement-config.cfg")));
			}

		} catch (FileNotFoundException e) {
			System.out
					.println(" ### FATAL ERROR WITH CONFIGURATION FILE #####");
			System.out.println(" ### Application will exit #####");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out
					.println(" ### FATAL ERROR WITH CONFIGURATION FILE #####");
			System.out.println(" ### Application will exit #####");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void storeAllProperties() {
		try {
			if (!ConfigureMode.webContext) {
				cfg_file.store(new FileOutputStream(
						"artifactManagement-config.cfg"),
						"Configuration Properties File for "
								+ "WebAPSEE Pro"/*ResourcesUtility.getProductID()*/);
			} else {
				cfg_file.store(new FileOutputStream(ConfigureMode.realPath
						+ File.separator + "artifactManagement-config.cfg"),
						"Configuration Properties File for "
								+ "WebAPSEE Pro"/*ResourcesUtility.getProductID()*/);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
