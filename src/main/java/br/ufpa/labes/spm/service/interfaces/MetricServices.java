package br.ufpa.labes.spm.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import br.ufpa.labes.spm.service.dto.MetricDTO;
import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;


@Remote
public interface MetricServices {
	public MetricDTO saveMetric(MetricDTO metricDTO);
	public List<MetricDefinitionDTO> getMetricsDefinitions();
	public String[] getTypes(String domain);
	public String[] getWithTypes(String domainFilter,String nameType);
}
