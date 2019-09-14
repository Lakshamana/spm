package br.ufpa.labes.spm.repository.impl.connections;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.connections.IArtifactConDAO;
import br.ufpa.labes.spm.domain.ArtifactCon;
import br.ufpa.labes.spm.domain.Connection;

@Stateless
public class ArtifactConDAO extends BaseDAO<ArtifactCon, Integer> implements IArtifactConDAO{

	protected ArtifactConDAO(Class<ArtifactCon> businessClass) {
		super(businessClass);
	}

	public ArtifactConDAO() {
		super(ArtifactCon.class);
	}

	@Override
	public ArtifactCon save(ArtifactCon conn) {
		super.save(conn);
		String ident = conn.getIdent() + "." + conn.getOid();
		conn.setIdent(ident);
		this.update(conn);
		return conn;
	}
}
