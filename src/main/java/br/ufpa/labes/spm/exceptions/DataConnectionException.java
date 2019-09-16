/*
 * Created on 05/05/2005
 *
 */
package br.ufpa.labes.spm.exceptions;


/**
 * @author Adailton Lima
 * LABES-UFPA
 */
public class DataConnectionException extends DAOException {

    /**
     *
     */
    public DataConnectionException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public DataConnectionException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public DataConnectionException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public DataConnectionException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
