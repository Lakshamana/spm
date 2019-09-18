package br.ufpa.labes.spm.service.interfaces;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.processKnowledge.MetricDefinitionDTO;
import org.qrconsult.spm.dtos.processKnowledge.MetricDefinitionsDTO;

@Remote
public interface MetricDefinitionServices {
	public MetricDefinitionDTO saveMetricDefinition(MetricDefinitionDTO metricDefinitionDTO);
	public String[] getMetricTypes();
	public MetricDefinitionsDTO getMetricDefinitions();
	public MetricDefinitionsDTO getMetricDefinitions(String termoBusca, String domainFilter, boolean orgFilter);
	public MetricDefinitionDTO getMetricDefinitionDTO(String nameMetricDefinition);
}
