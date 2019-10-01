package br.ufpa.labes.spm.service.interfaces;

import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;
import br.ufpa.labes.spm.service.dto.MetricDefinitionsDTO;

public interface MetricDefinitionServices {
	public MetricDefinitionDTO saveMetricDefinition(MetricDefinitionDTO metricDefinitionDTO);
	public String[] getMetricTypes();
	public MetricDefinitionsDTO getMetricDefinitions();
	public MetricDefinitionsDTO getMetricDefinitions(String termoBusca, String domainFilter, boolean orgFilter);
	public MetricDefinitionDTO getMetricDefinitionDTO(String nameMetricDefinition);
}
