package br.ufpa.labes.spm.util;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class SortCriteria implements Serializable {

  private HashMap<String, SortEntry> entries = new HashMap<String, SortEntry>();

  public SortEntry addSorting(String field, byte type) {
    SortEntry newEntry = new SortEntry(field, type);
    entries.put(field, newEntry);

    return newEntry;
  }

  public SortEntry removeSorting(String field) {
    return entries.remove(field);
  }

  public HashMap<String, SortEntry> getEntries() {
    return entries;
  }

  public class SortEntry {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_ASCENDING = 1;
    public static final int TYPE_DESCENDING = 2;

    private String fieldName = "";
    private byte type = TYPE_NONE;

    public SortEntry(String fieldName, byte type) {
      this.fieldName = fieldName;
      this.type = type;
    }

    public String getFieldName() {
      return fieldName;
    }

    public void setType(byte type) {
      this.type = type;
    }

    public byte getType() {
      return type;
    }
  }
}
