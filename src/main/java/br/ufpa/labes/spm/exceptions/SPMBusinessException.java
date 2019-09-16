package br.ufpa.labes.spm.exceptions;

public class SPMBusinessException extends SPMException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public SPMBusinessException(String message) {
		super(message);
	}

	public SPMBusinessException(Throwable t) {
		super(t);
	}

}
