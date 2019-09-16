package br.ufpa.labes.spm.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.ImageIcon;

public class ResourcesUtility {

  private static Properties cfg_file = null;

  static {
    cfg_file = new Properties();
    try {
      InputStream inStream =
          Thread.currentThread().getContextClassLoader().getResourceAsStream("resources.cfg");
      cfg_file.load(inStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getUserBundle() {
    return (String) cfg_file.get("bundle.userBundle");
  }

  public static String getBundleRootDir() {
    return (String) cfg_file.get("bundle.rootDir");
  }

  public static String getLookAndFeel() {
    return (String) cfg_file.get("view.lookAndFeel.default");
  }

  public static void setUserBundle(String param) {
    cfg_file.setProperty("bundle.userBundle", param);
  }

  public static void setBundleRootDir(String param) {
    cfg_file.setProperty("bundle.rootBundle", param);
  }

  public static String getProductID() {
    return (String) cfg_file.get("product.ident");
  }

  public static ImageIcon loadIcon(String pack, String filename) {
    String path = (String) cfg_file.get("pack." + pack);

    return (new ImageIcon(
        ResourcesUtility.class.getClassLoader().getResource(path + "/" + filename)));
  }

  public static void storeAllProperties() {
    try {
      cfg_file.store(
          new FileOutputStream("resources.cfg"),
          "Resources Properties for WebAPSEE"); //$NON-NLS-1$ //$NON-NLS-2$

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
