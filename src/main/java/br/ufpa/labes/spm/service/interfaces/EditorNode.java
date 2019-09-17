package org.qrconsult.spm.services.interfaces;

import java.io.Serializable;

public class EditorNode implements Serializable {

	private long UUID;
	private int x;
	private int y;
	private boolean visible;

	public EditorNode() {
		this.UUID = 0;
		this.x = 0;
		this.y = 0;
		this.visible = true;
	}

	public EditorNode(EditorNode pos) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.visible = pos.isVisible();
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

}
