package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GanttActivityDTO implements Serializable {
  public static final int TYPE_NORMAL = 0;
  public static final int TYPE_DECOMPOSED = 1;
  public static final int TYPE_TASK = 2;
  private int type;
  private String agentIdent;
  private String agentName;
  private String ident;
  private String name;
  private String state;
  private String percentCompleted;
  private String duration;
  private String script;
  private String plannedBegin;
  private String plannedEnd;
  private String actualBegin;
  private String actualEnd;
  private boolean isDelegable;
  private int id;
  private int idpai;
  //	private List<GanttActivityDTO> activitys;

  public GanttActivityDTO(
      String agentName,
      String ident,
      String name,
      String percentCompleted,
      String duration,
      String plannedBegin) {
    this.agentName = agentName;
    this.ident = ident;
    this.name = name;
    this.percentCompleted = percentCompleted;
    this.duration = duration;
    this.plannedBegin = plannedBegin;
  }

  public GanttActivityDTO(
      String agentIdent,
      String agentName,
      String ident,
      String name,
      String state,
      String script,
      String plannedBegin,
      String plannedEnd,
      String actualBegin,
      String actualEnd) {
    this.agentIdent = agentIdent;
    this.agentName = agentName;
    this.ident = ident;
    this.name = name;
    this.state = state;
    this.script = script;
    this.plannedBegin = plannedBegin;
    this.plannedEnd = plannedEnd;
    this.actualBegin = actualBegin;
    this.actualEnd = actualEnd;
  }

  public GanttActivityDTO() {}

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public String getActualBegin() {
    return actualBegin;
  }

  public void setActualBegin(String actualBegin) {
    this.actualBegin = actualBegin;
  }

  public String getActualEnd() {
    return actualEnd;
  }

  public void setActualEnd(String actualEnd) {
    this.actualEnd = actualEnd;
  }

  public boolean isDelegable() {
    return isDelegable;
  }

  public void setDelegable(boolean isDelegable) {
    this.isDelegable = isDelegable;
  }

  public String getAgentIdent() {
    return agentIdent;
  }

  public void setAgentIdent(String agentIdent) {
    this.agentIdent = agentIdent;
  }

  public String getAgentName() {
    return agentName;
  }

  public void setAgentName(String agentName) {
    this.agentName = agentName;
  }

  public String getIdent() {
    return ident;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPercentCompleted() {
    return percentCompleted;
  }

  public void setPercentCompleted(String percentCompleted) {
    this.percentCompleted = percentCompleted;
  }

  public String getDuration() {
    return duration;
  }

  public void setDuration(String duration) {
    this.duration = duration;
  }

  public String getPlannedBegin() {
    return plannedBegin;
  }

  public void setPlannedBegin(String plannedBegin) {
    this.plannedBegin = plannedBegin;
  }

  public String getPlannedEnd() {
    return plannedEnd;
  }

  public void setPlannedEnd(String plannedEnd) {
    this.plannedEnd = plannedEnd;
  }

  @Override
  public String toString() {
    return "type:"
        + type
        + ", agentIdent:"
        + agentIdent
        + ", agentName:"
        + agentName
        + ", ident:"
        + ident
        + ", name:"
        + name
        + ", state:"
        + state
        + ", percentCompleted:"
        + percentCompleted
        + ", duration:"
        + duration
        + ", script:"
        + script
        + ", plannedBegin:"
        + plannedBegin
        + ", plannedEnd:"
        + plannedEnd
        + ", actualBegin:"
        + actualBegin
        + ", actualEnd:"
        + actualEnd
        + ", isDelegable:"
        + isDelegable
        + "";
  }

  public int getIdpai() {
    return idpai;
  }

  public void setIdpai(int idpai) {
    this.idpai = idpai;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  //	public List<GanttActivityDTO> getActivitys() {
  //		if(activitys==null){
  //			activitys = new ArrayList<GanttActivityDTO>();
  //		}
  //		return activitys;
  //	}
  //
  //	public void setActivitys(List<GanttActivityDTO> activitys) {
  //		this.activitys = activitys;
  //	}
  //

}
