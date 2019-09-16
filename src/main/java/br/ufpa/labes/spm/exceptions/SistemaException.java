/**
 * Usuario WebServicesProcessor
 * @author Helder Klemp
 * Descri��o: Exca��o Simplificada para a camada cliente
 */
package br.ufpa.labes.spm.exceptions;

/**
 * @author
 */
public class SistemaException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3905523791684712497L;
	/**
	 *
	 */
	public SistemaException() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param arg0
	 */
	public SistemaException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param arg0
	 * @param arg1
	 */
	public SistemaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param arg0
	 */
	public SistemaException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
