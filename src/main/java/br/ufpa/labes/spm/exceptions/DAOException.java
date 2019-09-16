/*
 * Created on Oct 13, 2004
 *
 */
package br.ufpa.labes.spm.exceptions;

/** @author root */
public class DAOException extends WebapseeException {

  /** Comment for <code>serialVersionUID</code> */
  private static final long serialVersionUID = 3977859570704332337L;

  public DAOException() {
    super();
  }

  public DAOException(String message) {
    super(message);
  }

  public DAOException(String message, Throwable cause) {
    super(message, cause);
  }

  public DAOException(Throwable cause) {
    super(cause);
  }
}
