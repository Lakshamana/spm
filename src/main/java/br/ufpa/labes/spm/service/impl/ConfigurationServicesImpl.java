package br.ufpa.labes.spm.service.impl;

import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IConfiDAO;
import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.SpmConfigurationDTO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.Configuration;
import br.ufpa.labes.spm.service.interfaces.AgentServices;
import br.ufpa.labes.spm.service.interfaces.ConfigurationServices;

public class ConfigurationServicesImpl implements ConfigurationServices {

	IConfiDAO confiDAO;

	IAgentDAO agentDAO;

	AgentServices agentServices;

	Configuration confiAtual = new Configuration();
	Converter converter = new ConverterImpl();

	private static final String CONFIG_CLASSNAME = Configuration.class.getSimpleName();
	private Query query;
	public boolean perfilSave(SpmConfigurationDTO confi,AgentDTO agente) {
		System.out.println("Chegou no perfil"+confi.getIdioma()+"agente"+agente.getId());

		Configuration configuration = new Configuration();
		configuration = convertConfigurationDTOToConfiguration(confi);

		configuration.setAgent(convertAgentDTOToAgent((AgentDTO) agente));
		configuration.setId(confi.getId());
		System.out.println("id da confi " +configuration.getId());
		confiDAO.update(configuration);

		if (configuration != null)
			return true;
		else
			return false;

	}

	private Configuration convertConfigurationDTOToConfiguration(SpmConfigurationDTO configurationDTO) {
		System.out.println("caiu na conversão");
		try {
			Configuration config = new Configuration();
			config = (Configuration) converter.getEntity(configurationDTO, Configuration.class);
			config.setSenhaEmRecuperacao(configurationDTO.getSenhaEmRecuperacao());

			return config;
		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Agent convertAgentDTOToAgent(AgentDTO agentDTO) {
		try {

			Agent agent = new Agent();
			agent = (Agent) converter.getEntity(agentDTO, agent);
			agent.setTheTaskAgenda(null);

			return agent;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}

	private SpmConfigurationDTO convertConfigurationToConfigurationDTO(Configuration config) {
		SpmConfigurationDTO configDTO = new SpmConfigurationDTO();

		configDTO.setId(config.getId());
		configDTO.setIdioma(config.getIdioma());
		configDTO.setFiltro(config.getFiltro());
		configDTO.setSenhaEmRecuperacao(config.getSenhaEmRecuperacao());
		configDTO.setAgent(config.getAgent().getName());

		return configDTO;
	}

	@Override
	public SpmConfigurationDTO getPerfil(Integer agent_oid) {

		query = confiDAO
				.getPersistenceContext()
				.createQuery("SELECT configuration FROM "
								+ CONFIG_CLASSNAME
								+ " AS configuration "
								+ "WHERE configuration.agent.oid = :agent_oid");
		query.setParameter("agent_oid", agent_oid);
		if(query.getResultList().isEmpty()) {
			System.out.print("nulo");
			return null;
		} else {
			Configuration configuration = (Configuration) query.getResultList().get(0);
			System.out.print("retorno: "+configuration.getAgent()+"filtro:"+configuration.getFiltro()+"oid: "+configuration.getId());
			return convertConfigurationToConfigurationDTO(configuration);
		}
	}

	@Override
	public SpmConfigurationDTO updateConfiguration(Integer agentOid, SpmConfigurationDTO configuration) {
		SpmConfigurationDTO perfil = this.getPerfil(agentOid);

		atualizarPefil(perfil, configuration);
		Configuration config = convertConfigurationDTOToConfiguration(perfil);
		Agent agent = agentDAO.retrieve(agentOid);
		config.setAgent(agent);

		confiDAO.update(config);

		return perfil;
	}



	private SpmConfigurationDTO atualizarPefil(SpmConfigurationDTO fromDB,
			SpmConfigurationDTO configuration) {
		fromDB.setGraficoDeCustos(configuration.getGraficoDeCustos());
		fromDB.setGraficoDeEsforco(configuration.getGraficoDeEsforco());
		fromDB.setGraficoDeDesempenho(configuration.getGraficoDeDesempenho());
		fromDB.setGraficoDeTarefas(configuration.getGraficoDeTarefas());

		return fromDB;
	}

}
