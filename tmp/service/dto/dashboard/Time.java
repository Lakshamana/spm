package br.ufpa.labes.spm.service.dto.dashboard;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement(name = "time")
public class Time implements Serializable {

  private Integer hour;
  private Integer minutes;

  public Time() {}

  public Time(Integer hour, Integer minutes) {
    this.hour = hour;
    this.minutes = minutes;
  }

  public Integer getHour() {
    return hour;
  }

  public void setHour(Integer hour) {
    this.hour = hour;
  }

  public Integer getMinutes() {
    return minutes;
  }

  public void setMinutes(Integer minutes) {
    this.minutes = minutes;
  }

  @Override
  public String toString() {
    String hourString = "0";
    String minuteString = "0";
    if (this.hour < 10) hourString = "0" + this.hour;
    if (this.minutes < 10) minuteString = "0" + this.minutes;
    return hourString + ":" + minuteString;
  }
}
