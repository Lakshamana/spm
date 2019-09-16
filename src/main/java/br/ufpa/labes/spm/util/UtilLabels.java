package br.ufpa.labes.spm.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class UtilLabels {

  private static Properties cfg_file = null;

  static {
    try {
      cfg_file = new Properties();
      InputStream inputStream =
          UtilLabels.class.getClassLoader().getResourceAsStream("spm/classes-mapping.cfg");
      cfg_file.load(inputStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getPropertie(String key) {
    return cfg_file.getProperty(key);
  }

  public static final String DAO_FIND_OID = "findByPrimaryKey";
  public static final String DAO_FIND_IDENT = "findBySecondaryKey";
  public static final String DAO_SAVE = "save";
  public static final String DAO_DELETE = "delete";
  public static final String DAO_UPDATE = "update";
  public static final String DAO_FIND_ALL = "findAll";
  public static final String DAO_FIND_CRITERIA = "findByCriteria";
  // ....

}
