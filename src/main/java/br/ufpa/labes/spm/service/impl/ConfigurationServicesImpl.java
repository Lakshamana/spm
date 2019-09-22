package br.ufpa.labes.spm.service.impl;

import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import br.ufpa.labes.spm.exceptions.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IConfiDAO;
import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.SpmConfigurationDTO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.SpmConfiguration;
import br.ufpa.labes.spm.service.interfaces.AgentServices;
import br.ufpa.labes.spm.service.interfaces.ConfigurationServices;

public class ConfigurationServicesImpl implements ConfigurationServices {

	IConfiDAO confiDAO;

	IAgentDAO agentDAO;

	AgentServices agentServices;

	SpmConfiguration confiAtual = new SpmConfiguration();
	Converter converter = new ConverterImpl();

	private static final String CONFIG_CLASSNAME = SpmConfiguration.class.getSimpleName();
	private Query query;
	public boolean perfilSave(SpmConfigurationDTO confi,AgentDTO agente) {
		System.out.println("Chegou no perfil"+confi.getIdioma()+"agente"+agente.getId());

		SpmConfiguration spmconfiguration = new SpmConfiguration();
		spmconfiguration = convertConfigurationDTOToConfiguration(confi);

		spmconfiguration.setTheAgent(convertAgentDTOToAgent((AgentDTO) agente));
		spmconfiguration.setId(confi.getId());
		System.out.println("id da confi " +spmconfiguration.getId());
		confiDAO.update(spmconfiguration);

		if (spmconfiguration != null)
			return true;
		else
			return false;

	}

	private SpmConfiguration convertConfigurationDTOToConfiguration(SpmConfigurationDTO configurationDTO) {
		System.out.println("caiu na conversão");
		try {
			SpmConfiguration config = new SpmConfiguration();
			config = (SpmConfiguration) converter.getEntity(configurationDTO, SpmConfiguration.class);
			config.setSenhaEmRecuperacao(configurationDTO.isSenhaEmRecuperacao());

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

	private SpmConfigurationDTO convertConfigurationToConfigurationDTO(SpmConfiguration config) {
		SpmConfigurationDTO configDTO = new SpmConfigurationDTO();

		configDTO.setId(config.getId());
		configDTO.setIdioma(config.getIdioma());
		configDTO.setFiltro(config.getFiltro());
		configDTO.setSenhaEmRecuperacao(config.isSenhaEmRecuperacao());
		configDTO.setTheAgent(config.getTheAgent().getName());

		return configDTO;
	}

	@Override
	public SpmConfigurationDTO getPerfil(Integer agent_oid) {

		query = confiDAO
				.getPersistenceContext()
				.createQuery("SELECT spmconfiguration FROM "
								+ CONFIG_CLASSNAME
								+ " AS spmconfiguration "
								+ "WHERE spmconfiguration.agent.oid = :agent_oid");
		query.setParameter("agent_oid", agent_oid);
		if(query.getResultList().isEmpty()) {
			System.out.print("nulo");
			return null;
		} else {
			SpmConfiguration spmconfiguration = (SpmConfiguration) query.getResultList().get(0);
			System.out.print("retorno: "+spmconfiguration.getTheAgent()+"filtro:"+spmconfiguration.getFiltro()+"oid: "+spmconfiguration.getId());
			return convertConfigurationToConfigurationDTO(spmconfiguration);
		}
	}

	@Override
	public SpmConfigurationDTO updateConfiguration(Integer agentOid, SpmConfigurationDTO spmconfiguration) {
		SpmConfigurationDTO perfil = this.getPerfil(agentOid);

		atualizarPefil(perfil, spmconfiguration);
		SpmConfiguration config = convertConfigurationDTOToConfiguration(perfil);
		Agent agent = agentDAO.retrieve(agentOid);
		config.setTheAgent(agent);

		confiDAO.update(config);

		return perfil;
	}



	private SpmConfigurationDTO atualizarPefil(SpmConfigurationDTO fromDB,
			SpmConfigurationDTO spmconfiguration) {
		fromDB.setGraficoDeCustos(spmconfiguration.getGraficoDeCustos());
		fromDB.setGraficoDeEsforco(spmconfiguration.getGraficoDeEsforco());
		fromDB.setGraficoDeDesempenho(spmconfiguration.getGraficoDeDesempenho());
		fromDB.setGraficoDeTarefas(spmconfiguration.getGraficoDeTarefas());

		return fromDB;
	}

}
