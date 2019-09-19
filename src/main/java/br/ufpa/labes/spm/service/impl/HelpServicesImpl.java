package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.IHelpTopicDAO;
import br.ufpa.labes.spm.service.dto.HelpTopicDTO;
import br.ufpa.labes.spm.domain.HelpTopic;
import br.ufpa.labes.spm.service.interfaces.HelpServices;
import br.ufpa.labes.spm.util.ident.ConversorDeIdent;
import br.ufpa.labes.spm.util.ident.SemCaracteresEspeciais;
import br.ufpa.labes.spm.util.ident.TrocaEspacoPorPonto;

public class HelpServicesImpl  implements HelpServices{

	IHelpTopicDAO helpTopicDAO;

	Converter converter = new ConverterImpl();

	@Override
	public List<HelpTopicDTO> getHelpTopics() {
		List<HelpTopic> helpTopics = helpTopicDAO.getHelpTopics();
		List<HelpTopicDTO> result = new ArrayList<HelpTopicDTO>();
		try {

			for (HelpTopic helpTopic : helpTopics) {
				HelpTopicDTO helpTopicDTO = (HelpTopicDTO) converter.getDTO(helpTopic, HelpTopicDTO.class);

				if (!helpTopic.getSessions().isEmpty()){
					appendToDTO(helpTopicDTO, helpTopic.getSessions());
				}
				result.add(helpTopicDTO);
			}
			System.out.println("antes de retornar");
			return result;
		} catch (ImplementationException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public HelpTopicDTO getHelpTopicByToken(String token) {
		TypedQuery<HelpTopic> query;
		String hql = "SELECT h FROM " + HelpTopic.class.getSimpleName() + " h WHERE h.tokenRelated = :token";
		query = helpTopicDAO.getPersistenceContext().createQuery(hql, HelpTopic.class);
		query.setParameter("token", token);

		List<HelpTopic> result = query.getResultList();
		if(!result.isEmpty()) {
			HelpTopic topic = result.get(0);
			HelpTopicDTO helpTopicDTO = HelpTopicDTO.nullHelpTopicDTO();
			try {
				helpTopicDTO = (HelpTopicDTO) converter.getDTO(topic, HelpTopicDTO.class);
			} catch (ImplementationException e) {
				e.printStackTrace();
			}
			return helpTopicDTO;
		}

		return HelpTopicDTO.nullHelpTopicDTO();
	}

	public void appendToDTO(HelpTopicDTO rootDTO, List<HelpTopic> list){
		for (int i = 0; i < list.size(); i++) {
			HelpTopicDTO helpTopicDTO;
			try {
				helpTopicDTO = (HelpTopicDTO) converter.getDTO(list.get(i), HelpTopicDTO.class);
				rootDTO.getSessions().add(helpTopicDTO);

				if (!list.get(i).getSessions().isEmpty()){
					appendToDTO(helpTopicDTO, list.get(i).getSessions());
				}
			} catch (ImplementationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public HelpTopicDTO saveTopic(HelpTopicDTO helpTopicDTO) {
		if (helpTopicDTO.getIdent() == null || "".equals(helpTopicDTO.getIdent())){ //Quando se trata de um novo t�pico de ajuda
			if (helpTopicDTO.getFather() == null){
				HelpTopic helpTopic;
				try {
					long n = helpTopicDAO.countTopics();
					n++;
					helpTopicDTO.setIdent(String.valueOf(n));

					helpTopic = (HelpTopic) converter.getEntity(helpTopicDTO, HelpTopic.class);
					helpTopicDAO.daoSave(helpTopic);

					String newIdent = ConversorDeIdent.de(helpTopic.getId()+ " " + helpTopic.getTitle()).para(new SemCaracteresEspeciais())
							.para(new TrocaEspacoPorPonto())
							.ident();

					System.out.println("-> " + newIdent);

					helpTopic.setIdent(newIdent);
					helpTopicDTO.setIdent(newIdent);
					helpTopicDAO.update(helpTopic);

					return helpTopicDTO;
				} catch (ImplementationException e) {
					e.printStackTrace();
					return null;
				}
			} else {
				HelpTopic father = helpTopicDAO.retrieveBySecondaryKey(helpTopicDTO.getFather().getIdent());
//				long n = father.getSessions().size()+1;
//
//				helpTopicDTO.setIdent(father.getIdent() + "." + n);
				try {
					HelpTopic helpTopic = (HelpTopic) converter.getEntity(helpTopicDTO, HelpTopic.class);
					helpTopic.setFather(father);
					father.getSessions().add(helpTopic);
					helpTopicDAO.daoSave(father);
					return helpTopicDTO;
				} catch (ImplementationException e) {
					e.printStackTrace();
					return null;
				}
			}
		} else { //Quando se trata de edi��o de um t�pico de ajuda
			HelpTopic helpTopic = helpTopicDAO.retrieveBySecondaryKey(helpTopicDTO.getIdent());
			helpTopic.setTitle(helpTopicDTO.getTitle());
			helpTopic.setContent(helpTopicDTO.getContent());
			helpTopic.setTokenRelated(helpTopicDTO.getTokenRelated());
			helpTopic = helpTopicDAO.daoSave(helpTopic);
			try {
				return (HelpTopicDTO) converter.getDTO(helpTopic, HelpTopicDTO.class);
			} catch (ImplementationException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public void removeTopic(HelpTopicDTO helpTopicDTO) {
		System.out.println(helpTopicDTO);
		HelpTopic helpTopic = helpTopicDAO.retrieveBySecondaryKey(helpTopicDTO.getIdent());
		System.out.println(helpTopic);
		helpTopicDAO.getPersistenceContext().remove(helpTopic);
	}
}
