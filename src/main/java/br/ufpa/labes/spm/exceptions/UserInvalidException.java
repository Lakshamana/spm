/*
 * Created on 05/05/2005
 *
 */
package br.ufpa.labes.spm.exceptions;

/** @author Adailton Lima LABES-UFPA */
public class UserInvalidException extends UserException {

  /** */
  public UserInvalidException() {
    super();
    // TODO Auto-generated constructor stub
  }

  /** @param arg0 */
  public UserInvalidException(String arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

  /**
   * @param arg0
   * @param arg1
   */
  public UserInvalidException(String arg0, Throwable arg1) {
    super(arg0, arg1);
    // TODO Auto-generated constructor stub
  }

  /** @param arg0 */
  public UserInvalidException(Throwable arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }
}
