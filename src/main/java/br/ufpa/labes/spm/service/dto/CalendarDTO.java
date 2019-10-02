package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class CalendarDTO implements Serializable {

  private Long id;

  private String name;

  private ArrayList<String> notWorkingDays;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<String> getNotWorkingDays() {
    return notWorkingDays;
  }

  public void setNotWorkingDays(ArrayList<String> notWorkingDays) {
    this.notWorkingDays = notWorkingDays;
  }
}
