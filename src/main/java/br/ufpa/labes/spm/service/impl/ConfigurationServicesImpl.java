package br.ufpa.labes.spm.service.impl;

import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IConfiDAO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import org.qrconsult.spm.dtos.formAgent.ConfigurationDTO;
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
	public boolean perfilSave(ConfigurationDTO confi,AgentDTO agente) {
		System.out.println("Chegou no perfil"+confi.getIdioma()+"agente"+agente.getOid());

		Configuration configuration = new Configuration();
		configuration = convertConfigurationDTOToConfiguration(confi);

		configuration.setAgent(convertAgentDTOToAgent((AgentDTO) agente));
		configuration.setOid(confi.getOid());
		System.out.println("id da confi " +configuration.getOid());
		confiDAO.update(configuration);

		if (configuration != null)
			return true;
		else
			return false;

	}

	private Configuration convertConfigurationDTOToConfiguration(ConfigurationDTO configurationDTO) {
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

	private ConfigurationDTO convertConfigurationToConfigurationDTO(Configuration config) {
		ConfigurationDTO configDTO = new ConfigurationDTO();

		configDTO.setOid(config.getOid());
		configDTO.setIdioma(config.getIdioma());
		configDTO.setFiltro(config.getFiltro());
		configDTO.setSenhaEmRecuperacao(config.getSenhaEmRecuperacao());
		configDTO.setAgent(config.getAgent().getName());

		return configDTO;
	}

	@Override
	public ConfigurationDTO getPerfil(Integer agent_oid) {

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
			System.out.print("retorno: "+configuration.getAgent()+"filtro:"+configuration.getFiltro()+"oid: "+configuration.getOid());
			return convertConfigurationToConfigurationDTO(configuration);
		}
	}

	@Override
	public ConfigurationDTO updateConfiguration(Integer agentOid, ConfigurationDTO configuration) {
		ConfigurationDTO perfil = this.getPerfil(agentOid);

		atualizarPefil(perfil, configuration);
		Configuration config = convertConfigurationDTOToConfiguration(perfil);
		Agent agent = agentDAO.retrieve(agentOid);
		config.setAgent(agent);

		confiDAO.update(config);

		return perfil;
	}



	private ConfigurationDTO atualizarPefil(ConfigurationDTO fromDB,
			ConfigurationDTO configuration) {
		fromDB.setGraficoDeCustos(configuration.getGraficoDeCustos());
		fromDB.setGraficoDeEsforco(configuration.getGraficoDeEsforco());
		fromDB.setGraficoDeDesempenho(configuration.getGraficoDeDesempenho());
		fromDB.setGraficoDeTarefas(configuration.getGraficoDeTarefas());

		return fromDB;
	}

}
