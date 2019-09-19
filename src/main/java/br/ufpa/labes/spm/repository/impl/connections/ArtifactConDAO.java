package br.ufpa.labes.spm.repository.impl.connections;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IArtifactConDAO;
import br.ufpa.labes.spm.domain.ArtifactCon;

public class ArtifactConDAO extends BaseDAO<ArtifactCon, Integer> implements IArtifactConDAO {

  protected ArtifactConDAO(Class<ArtifactCon> businessClass) {
    super(businessClass);
  }

  public ArtifactConDAO() {
    super(ArtifactCon.class);
  }

  @Override
  public ArtifactCon save(ArtifactCon conn) {
    super.daoSave(conn);
    String ident = conn.getIdent() + "." + conn.getId();
    conn.setIdent(ident);
    this.update(conn);
    return conn;
  }
}
