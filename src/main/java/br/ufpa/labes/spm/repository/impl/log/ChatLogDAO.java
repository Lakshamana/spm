package br.ufpa.labes.spm.repository.impl.log;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IChatLogDAO;
import br.ufpa.labes.spm.domain.ChatLog;

@Stateless
public class ChatLogDAO extends BaseDAO<ChatLog, Integer> implements IChatLogDAO{

	protected ChatLogDAO(Class<ChatLog> businessClass) {
		super(businessClass);
	}

	public ChatLogDAO() {
		super(ChatLog.class);
	}


}
