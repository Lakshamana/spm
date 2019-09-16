package br.ufpa.labes.spm.repository.interfaces;

public class IDAO {

  private static IDAOFactory daoFactory;

  public IDAO() {};

  public static IDAOFactory getInstance() {
    if (daoFactory == null) {
      daoFactory = new IDAOFactory();
    }
    return daoFactory;
  }
}
