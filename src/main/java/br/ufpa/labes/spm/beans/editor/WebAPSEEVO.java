package br.ufpa.labes.spm.beans.editor;

import java.io.Serializable;

@SuppressWarnings("serial")
public class WebAPSEEVO implements Serializable {

  private String name;
  private String ident;
  private String type;
  private String[] roles;

  public WebAPSEEVO() {
    super();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdent() {
    return ident;
  }

  public void setIdent(String ident) {
    this.ident = ident;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String[] getRoles() {
    return roles;
  }

  public void setRoles(String[] roles) {
    this.roles = roles;
  }

  public String getXMLTag() {
    String rolesTag = "<ROLES>\n";
    if (roles != null && roles.length > 0) {
      for (int i = 0; i < roles.length; i++) {
        rolesTag += "<ROLE ID=\"" + roles[i] + "\" />\n ";
      }
    }
    rolesTag += "</ROLES>";

    return "<WEBAPSEEVO NAME=\""
        + this.name
        + "\" ID=\""
        + this.ident
        + "\" TYPE=\""
        + this.type
        + "\" >\n"
        + rolesTag
        + "</WEBAPSEEVO>";
  }
}
