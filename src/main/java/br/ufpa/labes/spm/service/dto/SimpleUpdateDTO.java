package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SimpleUpdateDTO implements Serializable {
  private String oldValue;
  private String newValue;

  public String getOldValue() {
    return oldValue;
  }

  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }

  public String getNewValue() {
    return newValue;
  }

  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }

  public boolean equals(Object obj) {
    if (obj instanceof SimpleUpdateDTO) {
      SimpleUpdateDTO bean = (SimpleUpdateDTO) obj;

      return bean.getOldValue().equals(this.getOldValue())
          && bean.getNewValue().equals(this.getNewValue());
    }

    return false;
  }

  @Override
  public String toString() {
    return "New: " + newValue + "; Old: " + oldValue;
  }
}
