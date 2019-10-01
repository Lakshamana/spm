package br.ufpa.labes.spm.service.interfaces;

import java.util.List;


import br.ufpa.labes.spm.domain.ChatMessage;

public interface ChatMessageServices {

	public List<ChatMessage> buscarTodasAsMensagensDaConversa(String ident);

	public void salvarMensagem(ChatMessage message, String de);

}
