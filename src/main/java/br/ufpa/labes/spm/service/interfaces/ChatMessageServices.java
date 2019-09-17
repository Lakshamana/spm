package org.qrconsult.spm.services.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.model.chat.ChatMessage;

@Remote
public interface ChatMessageServices {

	public List<ChatMessage> buscarTodasAsMensagensDaConversa(String ident);
	
	public void salvarMensagem(ChatMessage message, String de);
	
}
