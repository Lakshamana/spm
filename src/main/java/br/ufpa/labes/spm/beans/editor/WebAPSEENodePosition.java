package br.ufpa.labes.spm.beans.editor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

@SuppressWarnings("serial")
public class WebAPSEENodePosition implements Serializable {
  private long UUID;

  private int x;
  private int y;
  private boolean visible;

  private String instanceID;
  private String typeID;

  private int nodeType; // identified by the constants defined above
  private Collection<String> theReferredObjects;

  public static final int ACTIVITYNODE = 0;

  public static final int ARTIFACTCONNODE = 1;
  public static final int BRANCHNODE = 2;
  public static final int JOINNODE = 3;
  public static final int SEQUENCENODE = 4;

  public static final int REQAGENTNODE = 5;
  public static final int REQGROUPNODE = 6;
  public static final int REQRESOURCENODE = 7;

  public WebAPSEENodePosition() {
    this.UUID = 0;
    this.x = 0;
    this.y = 0;
    this.visible = true;
    this.instanceID = "";
    this.typeID = "";
    this.nodeType = -1;
    this.theReferredObjects = new HashSet<String>();
  }

  public WebAPSEENodePosition(int x, int y, String instanceID, String typeID, int nodeType) {
    this.x = x;
    this.y = y;
    this.instanceID = instanceID;
    this.typeID = typeID;
    this.nodeType = nodeType;
    this.UUID = 0;
    this.visible = true;
    this.theReferredObjects = new HashSet<String>();
  }

  public WebAPSEENodePosition(WebAPSEENodePosition pos) {
    this.x = pos.getX();
    this.y = pos.getY();
    this.visible = pos.isVisible();
    this.instanceID = pos.getInstanceID();
    this.typeID = pos.getTypeID();
    this.nodeType = pos.getNodeType();
    this.theReferredObjects = new HashSet<String>(); // not copy
  }

  public long getUUID() {
    return UUID;
  }

  public void setUUID(long uuid) {
    UUID = uuid;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public String getInstanceID() {
    return instanceID;
  }

  public void setInstanceID(String instanceID) {
    this.instanceID = instanceID;
  }

  public String getTypeID() {
    return typeID;
  }

  public void setTypeID(String typeID) {
    this.typeID = typeID;
  }

  public int getNodeType() {
    return nodeType;
  }

  public void setNodeType(int nodeType) {
    this.nodeType = nodeType;
  }

  public Collection<String> getTheReferredObjects() {
    return theReferredObjects;
  }

  public void setTheReferredObjects(Collection<String> theReferredObjects) {
    this.theReferredObjects = theReferredObjects;
  }

  public void addToTheReferredObjects(String theReferredObject) {
    this.theReferredObjects.add(theReferredObject);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((instanceID == null) ? 0 : instanceID.hashCode());
    result = prime * result + nodeType;
    result = prime * result + ((typeID == null) ? 0 : typeID.hashCode());
    result = prime * result + x;
    result = prime * result + y;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    WebAPSEENodePosition other = (WebAPSEENodePosition) obj;
    if (instanceID == null) {
      if (other.instanceID != null) return false;
    } else if (!instanceID.equals(other.instanceID)) return false;
    if (nodeType != other.nodeType) return false;
    if (typeID == null) {
      if (other.typeID != null) return false;
    } else if (!typeID.equals(other.typeID)) return false;
    if (x != other.x) return false;
    if (y != other.y) return false;
    return true;
  }

  @Override
  public String toString() {
    String webapseeObject =
        "ident: "
            + instanceID
            + "; x: "
            + x
            + "; y: "
            + y
            + "; typeIdent: "
            + typeID
            + "; nodeType: "
            + nodeType;
    return webapseeObject;
  }
}
