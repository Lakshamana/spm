package br.ufpa.labes.spm.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import br.ufpa.labes.spm.service.dto.EstimationDTO;
import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;

@Remote
public interface EstimationServices {
	public EstimationDTO saveEstimation(EstimationDTO estimationDTO);
	public List<MetricDefinitionDTO> getMetricsDefinitions();
	public String[] getTypes(String domainFilter);
	public String[] getWithTypes(String domainFilter,String nameType);
}
