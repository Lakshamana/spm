package br.ufpa.labes.spm.service.interfaces;

import java.util.List;


import br.ufpa.labes.spm.service.dto.MetricDTO;
import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;


public interface MetricServices {
	public MetricDTO saveMetric(MetricDTO metricDTO);
	public List<MetricDefinitionDTO> getMetricsDefinitions();
	public String[] getTypes(String domain);
	public String[] getWithTypes(String domainFilter,String nameType);
}
