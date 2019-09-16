package br.ufpa.labes.spm.repository.impl.log;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.log.IChatLogDAO;
import br.ufpa.labes.spm.domain.ChatLog;

public class ChatLogDAO extends BaseDAO<ChatLog, Integer> implements IChatLogDAO {

  protected ChatLogDAO(Class<ChatLog> businessClass) {
    super(businessClass);
  }

  public ChatLogDAO() {
    super(ChatLog.class);
  }
}
