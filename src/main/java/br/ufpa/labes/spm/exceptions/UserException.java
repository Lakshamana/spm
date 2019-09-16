/*
 * Created on 29/03/2005
 */
package br.ufpa.labes.spm.exceptions;

/**
 * @author breno
 */
public class UserException extends WebapseeException{

	/**
	 *
	 */
	private static final long serialVersionUID = -1770304200611262499L;

	public UserException() {
		super();
	}

	public UserException(String arg0) {
		super(arg0);
	}

	public UserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UserException(Throwable arg0) {
		super(arg0);
	}
}
