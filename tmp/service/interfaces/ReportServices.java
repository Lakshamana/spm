package br.ufpa.labes.spm.service.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import br.ufpa.labes.spm.service.dto.ActivitiesByAgentReportItem;
import br.ufpa.labes.spm.service.dto.AgentMetricsReportItem;
import br.ufpa.labes.spm.service.dto.AgentsByWorkGroupReportItem;
import br.ufpa.labes.spm.service.dto.AgentsByRoleReportItem;
import br.ufpa.labes.spm.service.dto.AgentsReportItem;
import br.ufpa.labes.spm.service.dto.ArtifactMetricsReportItem;
import br.ufpa.labes.spm.service.dto.CostDeviationReportItem;
import br.ufpa.labes.spm.service.dto.DocumentManagementPlanItem;
import br.ufpa.labes.spm.service.dto.KnowledgeItensReportItem;
import br.ufpa.labes.spm.service.dto.ProjectArtifactsReportItem;
import br.ufpa.labes.spm.service.dto.ProjectsBySystemReportItem;
import br.ufpa.labes.spm.service.dto.RequirementItem;
import br.ufpa.labes.spm.service.dto.ResourceMetricsReportItem;
import br.ufpa.labes.spm.service.dto.ResourceStatesReportItem;
import br.ufpa.labes.spm.service.dto.ResourcesPlanItem;
import br.ufpa.labes.spm.service.dto.SchedulePlanItem;
import br.ufpa.labes.spm.service.dto.WorkBreakdownStructureItem;
import br.ufpa.labes.spm.service.dto.ResourcesCostPlanItem;

@Remote
public interface ReportServices {
	List<AgentsReportItem> generateAgentReport(Date date);
	List<Object> generateActivitiesByProcessReport(String processIdent);
	List<AgentsByRoleReportItem> generateAgentsByRoleReport();
	List<AgentsByWorkGroupReportItem> generateAgentsByWorkGroupReport();
	List<ProjectArtifactsReportItem> generateProjectArtifactsReport(String processIdent);
	List<ActivitiesByAgentReportItem> generateActivitiesByAgentReport(String agentIdent);
	List<RequirementItem> generateRequirementItemReport(String params);
	List<CostDeviationReportItem> generateCostDeviationReport(String processIdent);
	List<ResourceStatesReportItem> generateResourceStatesReport();
	List<KnowledgeItensReportItem> generateKnowledgeItensReport(Date initialDate, Date finalDate);
	List<AgentMetricsReportItem> generateAgentMetricsReport(String agentIdent);
	List<ArtifactMetricsReportItem> generateArtifactMetricsReport(String artifactIdent);
	List<ResourceMetricsReportItem> generateResourceMetricsReport(String resourceIdent);
	List<SchedulePlanItem> generateSchedulePlanReport(String processIdent);
	List<DocumentManagementPlanItem> generateDocumentManagementPlanReport(String processIdent);
	List<Object> generateHumanResourcesReport(String processIdent);
	List<ResourcesPlanItem> generateResourcesPlanReport(String processIdent);
	List<ProjectsBySystemReportItem> generateProjectsBySystemReport(String systemIdent);
	List<WorkBreakdownStructureItem> generateWorkBreakdownStructureReport(String processIdent);
	List<ResourcesCostPlanItem> generateResourcesCostPlanReport(String processIdent);
}
