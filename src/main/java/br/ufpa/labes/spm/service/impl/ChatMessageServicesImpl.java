package org.qrconsult.spm.services.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.qrconsult.spm.dataAccess.interfaces.agent.IAgentDAO;
import org.qrconsult.spm.dataAccess.interfaces.chat.IChatMessageDAO;
import org.qrconsult.spm.model.agent.Agent;
import org.qrconsult.spm.model.chat.ChatMessage;
import org.qrconsult.spm.services.interfaces.ChatMessageServices;

@Stateless
public class ChatMessageServicesImpl implements ChatMessageServices {

	@EJB
	IAgentDAO agentDAO;
	
	@EJB
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
			message.setDe(agenteMsg);
			messageDAO.save(message);
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
