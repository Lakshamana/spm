package br.ufpa.labes.spm.repository.interfaces.chat;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.ChatMessage;

@Local
public interface IChatMessageDAO extends IBaseDAO<ChatMessage, Integer> {

}
