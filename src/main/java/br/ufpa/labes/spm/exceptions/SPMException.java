package br.ufpa.labes.spm.exceptions;

import import br.ufpa.labes.spm.domain.LogEntry;;

public class SPMException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private LogEntry entry;

	public SPMException(String message) {
		super(message);
	}

	public SPMException(Throwable t) {
		super(t);
	}

	public void setEntry(LogEntry entry) {
		this.entry = entry;
	}

	public LogEntry getEntry() {
		return entry;
	}

}
