/*
 * Created on 20/05/2005
 *
 */
package br.ufpa.labes.spm.exceptions;


/**
 * @author Adailton Lima
 * LABES-UFPA
 */
public class LockedProcessException extends WebapseeException {

    /**
     *
     */
    public LockedProcessException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public LockedProcessException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public LockedProcessException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public LockedProcessException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
    }
}
