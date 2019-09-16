/*
 * Created on 05/05/2005
 *
 */
package br.ufpa.labes.spm.exceptions;


/**
 * @author Adailton Lima
 * LABES-UFPA
 */
public class DataNotFoundException extends DAOException {

    /**
     *
     */
    public DataNotFoundException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public DataNotFoundException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public DataNotFoundException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
