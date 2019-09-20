package br.ufpa.labes.spm.service.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.chat.IChatMessageDAO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.ChatMessage;
import br.ufpa.labes.spm.service.interfaces.ChatMessageServices;

public class ChatMessageServicesImpl implements ChatMessageServices {

	IAgentDAO agentDAO;

	IChatMessageDAO messageDAO;

	TypedQuery<ChatMessage> query;

	@Override
	public List<ChatMessage> buscarTodasAsMensagensDaConversa(String ident) {
		String hql = "SELECT m FROM " + "ChatMessage" + " m WHERE m.ident = :ident";
		query = messageDAO.getPersistenceContext().createQuery(hql, ChatMessage.class);
		query.setParameter("ident", ident);
		List<ChatMessage> result = query.getResultList();
		return result;
	}

	@Override
	public void salvarMensagem(ChatMessage message, String de) {
		Agent agenteMsg = this.getAgent(de);
		System.out.println(agenteMsg);
		boolean encontrouAgente = !(new Agent().equals(agenteMsg));
		System.out.println(encontrouAgente);
		if(encontrouAgente) {
			message.setFromAgent(agenteMsg);
			messageDAO.daoSave(message);
		}
	}


	private Agent getAgent(String ident) {
		String hql = "SELECT a FROM " + "Agent" + " a WHERE a.name = :ident";
		TypedQuery<Agent> query = agentDAO.getPersistenceContext().createQuery(hql, Agent.class);
		query.setParameter("ident", ident);

		List<Agent> agents = query.getResultList();
		boolean singleResult = agents.size() >= 1;
		if(singleResult) {
			return agents.get(0);
		} else {
			return new Agent();
		}
	}
}
