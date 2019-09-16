/*
 * Created on 30/03/2005
 */
package br.ufpa.labes.spm.exceptions;

/** @author breno */
public class DataBaseException extends DAOException {

  public DataBaseException() {
    super();
  }

  public DataBaseException(String arg0) {
    super(arg0);
  }

  public DataBaseException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  public DataBaseException(Throwable arg0) {
    super(arg0);
  }
}
