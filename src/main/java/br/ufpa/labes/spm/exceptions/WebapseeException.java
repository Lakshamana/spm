package br.ufpa.labes.spm.exceptions;

import java.io.Serializable;

import br.ufpa.labes.spm.domain.LogEntry;;

public class WebapseeException extends Exception implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private LogEntry entry;

	public WebapseeException() {
		super();
	}

	public WebapseeException(String message) {
		super(message);
	}

	public WebapseeException(Throwable cause) {
		super(cause);
	}

	public WebapseeException(String message, Throwable cause) {
		super(message, cause);
	}

	public void setEntry(LogEntry entry) {
		this.entry = entry;
	}

	public LogEntry getEntry() {
		return entry;
	}

}
