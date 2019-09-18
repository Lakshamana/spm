package br.ufpa.labes.spm.service.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.formReport.reportItems.ActivitiesByAgentReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.AgentMetricsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.AgentsByGroupReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.AgentsByRoleReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.AgentsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ArtifactMetricsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.CostDeviationReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.DocumentManagementPlanItem;
import org.qrconsult.spm.dtos.formReport.reportItems.KnowledgeItensReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ProjectArtifactsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ProjectsBySystemReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.RequirementItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ResourceMetricsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ResourceStatesReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ResourcesPlanItem;
import org.qrconsult.spm.dtos.formReport.reportItems.SchedulePlanItem;
import org.qrconsult.spm.dtos.formReport.reportItems.WorkBreakdownStructureItem;
import org.qrconsult.spm.dtos.formReport.reportItems.resourcesCostPlan.ResourcesCostPlanItem;

@Remote
public interface ReportServices {
	List<AgentsReportItem> generateAgentReport(Date date);
	List<Object> generateActivitiesByProcessReport(String processIdent);
	List<AgentsByRoleReportItem> generateAgentsByRoleReport();
	List<AgentsByGroupReportItem> generateAgentsByGroupReport();
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
