package br.ufpa.labes.spm.repository.interfaces;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.EntityManager;

import org.qrconsult.spm.util.PagingContext;
import org.qrconsult.spm.util.SortCriteria;

@Local
public interface IReportDAO{
	public EntityManager getPersistenceContext();
	
	public List<Object[]> getAgentsReportData(Date date);
	public List<Object[]> getActivitiesByProcessReportData(String processIdent);
	public List<Object[]> getAgentsByRoleReportData();
	public List<Object[]> getAgentsByGroupReportData();
	public List<Object[]> getProjectArtifactsReportData(String projectIdent);
	public List<Object[]> getActivitiesByAgentsReportData(String agentIdent , Date beginDate, Date endDate , String role , boolean allStates);
	public List<Object[]> getCostDeviationReportData(String processIdent);
	public List<Object[]> getResourceStatesReportData();
	public List<Object[]> getKnowledgeItensReportData(Date initialDate, Date finalDate);
	public List<Object[]> getAgentMetricsReportData(String agentName);
	public List<Object[]> getArtifactMetricsReportData(String artifactIdent);
	public List<Object[]> getResourceMetricsReportData(String resourceIdent);
	public List<Object[]> getScheduleData(String processIdent);
	public List<Object[]> getDocumentManagementPlanData(String processIdent);
	public List<Object[]> getResourcesPlanData(String ident);
	public List<Object[]> getHumanResourcesPlanData(String processIdent);
	public List<Object[]> getAllocableActivitiesData(String processIdent);
	public List<Object[]> getProjectsBySystemReportData(String systemIdent);
	public List<Object[]> getWorkBreakdownStructureData(String processIdent);
	public List<Object[]> getResourcesCostPlanData(String processIdent);
}