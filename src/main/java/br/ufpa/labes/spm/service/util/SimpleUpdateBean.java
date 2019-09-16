package br.ufpa.labes.spm.service.util;
import java.io.Serializable;

public class SimpleUpdateBean  implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Object oldValue = null;
	private Object newValue = null;

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}

	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	public boolean equals( Object obj ) {
		if ( obj instanceof SimpleUpdateBean ) {
			SimpleUpdateBean bean = (SimpleUpdateBean)obj;

			return bean.getOldValue().equals( this.getOldValue()) && bean.getNewValue().equals(this.getNewValue());
		}

		return false;
	}
}
