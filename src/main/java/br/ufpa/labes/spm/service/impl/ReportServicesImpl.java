package br.ufpa.labes.spm.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import br.ufpa.labes.spm.repository.interfaces.IReportDAO;
import org.qrconsult.spm.dtos.formReport.reportItems.ActivitiesByAgentReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ActivitiesByProcessReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.AgentItemBean;
import org.qrconsult.spm.dtos.formReport.reportItems.AgentMetricsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.AgentsByGroupReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.AgentsByRoleReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.AgentsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ArtifactMetricsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.CostDeviationReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.DocumentManagementPlanItem;
import org.qrconsult.spm.dtos.formReport.reportItems.HumanResourcesPlanItem;
import org.qrconsult.spm.dtos.formReport.reportItems.KnowledgeItensReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ProjectArtifactsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ProjectsBySystemReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.RequirementItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ResourceItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ResourceMetricsReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ResourceStatesReportItem;
import org.qrconsult.spm.dtos.formReport.reportItems.ResourcesPlanItem;
import org.qrconsult.spm.dtos.formReport.reportItems.SchedulePlanItem;
import org.qrconsult.spm.dtos.formReport.reportItems.WorkBreakdownStructureItem;
import org.qrconsult.spm.dtos.formReport.reportItems.resourcesCostPlan.AllocableActivityItem;
import org.qrconsult.spm.dtos.formReport.reportItems.resourcesCostPlan.ResourcesCostConsumableItem;
import org.qrconsult.spm.dtos.formReport.reportItems.resourcesCostPlan.ResourcesCostExclusiveItem;
import org.qrconsult.spm.dtos.formReport.reportItems.resourcesCostPlan.ResourcesCostHumanItem;
import org.qrconsult.spm.dtos.formReport.reportItems.resourcesCostPlan.ResourcesCostPlanItem;
import org.qrconsult.spm.dtos.formReport.reportItems.resourcesCostPlan.ResourcesCostShareableItem;
import br.ufpa.labes.spm.service.interfaces.ReportServices;

@Stateless
public class ReportServicesImpl implements ReportServices {
	IReportDAO reportDAO;

	@Override
	public List<AgentsReportItem> generateAgentReport(Date date) {
		Date atDate = date;

		Date todayDate = new Date();

		SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );

		if (formatter.format(atDate).equals(formatter.format(todayDate))) {
			atDate = null;
		}

		List<Object[]> data = reportDAO.getAgentsReportData(atDate);
		if (data.size() == 0)
			return new ArrayList<AgentsReportItem>();

		List<AgentsReportItem> beansList = new ArrayList<AgentsReportItem>();

		for ( Object[] entry : data ) {
			AgentsReportItem item = new AgentsReportItem();

			item.setAgentIdent( (String)entry[ 0 ] );
			item.setAgentName( (String)entry[ 1 ] );
			item.setAgentCostHour( (Double)entry[ 2 ] );
			item.setAgentWorkload( (Integer)entry[ 3 ] );

			beansList.add( item );
		}

		return beansList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> generateActivitiesByProcessReport(String processIdent) {
		List<Object[]> data = reportDAO.getActivitiesByProcessReportData(processIdent);
		if (data.size() == 0)
			return new ArrayList<Object>();

		List<Object> beansList = new ArrayList<Object>();
		for ( Object[] entry : data ) {
			List<Object[]> tasks = (List<Object[]>) entry[ 9 ];

			for ( Object[] task : tasks ) {
				ActivitiesByProcessReportItem item = new ActivitiesByProcessReportItem();
				System.out.println("A => " + (String)entry[ 1 ] + " - " + ((Date)entry[ 3 ]).toString());
				item.setActivityIdent( (String)entry[ 0 ] );
				item.setActivityName( (String)entry[ 1 ] );
				item.setActivityState( (String)entry[ 2 ] );
				item.setPlannedBegin( (Date)entry[ 3 ] );
				item.setPlannedEnd( (Date)entry[ 4 ] );
				item.setActualBegin( (Date)entry[ 5 ] );
				item.setActualEnd( (Date)entry[ 6 ] );
				item.setPlannedAmountOfHours( (Double)entry[ 7 ] );
				item.setActualAmountOfHours( (Double)entry[ 8 ] );
				item.setAgentName( (String)task[ 2 ] );

				try {
					item.setLocalState( (String)task[ 3 ] );
					item.setWorkingHours( new Double( (Float)task[ 4 ] ) );
				}
				catch (ArrayIndexOutOfBoundsException e) {

				}

				beansList.add( item );
			}
		}

		return beansList;
	}

	@Override
	public List<AgentsByRoleReportItem> generateAgentsByRoleReport() {
		List<Object[]> data = reportDAO.getAgentsByRoleReportData();
		System.out.println("No service: " + data);
		if (data.size() == 0)
			return new ArrayList<AgentsByRoleReportItem>();

		List<AgentsByRoleReportItem> beansList = new ArrayList<AgentsByRoleReportItem>();
		for ( Object[] entry : data ) {
			SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
			AgentsByRoleReportItem item = new AgentsByRoleReportItem();

			item.setRoleName( (String)entry[ 0 ] );
			item.setAgentName( (String)entry[ 1 ] );
			item.setSinceDate( ( entry[ 2 ] != null ? formatter.format( entry[ 2 ] ) : "" ) );

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<AgentsByGroupReportItem> generateAgentsByGroupReport() {
		List<Object[]> data = reportDAO.getAgentsByGroupReportData();

		if ( data.size() == 0 )
			return new ArrayList<AgentsByGroupReportItem>();

		List<AgentsByGroupReportItem> beansList = new ArrayList<AgentsByGroupReportItem>();

		for ( Object[] entry : data ) {
			AgentsByGroupReportItem item = new AgentsByGroupReportItem();

			item.setGroupName( (String)entry[ 0 ] );
			item.setAgentName( (String)entry[ 1 ] );

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<ProjectArtifactsReportItem> generateProjectArtifactsReport(String processIdent) {
		List<Object[]> data = reportDAO.getProjectArtifactsReportData(processIdent);

		if ( data.size() == 0 )
			return new ArrayList<ProjectArtifactsReportItem>();

		List<ProjectArtifactsReportItem> beansList = new ArrayList<ProjectArtifactsReportItem>();
		for ( Object[] entry : data ) {
			ProjectArtifactsReportItem item = new ProjectArtifactsReportItem();

			item.setProjectName( (String)entry[ 0 ] );
			item.setSystemName( (String)entry[ 1 ] );
			item.setArtifactName( (String)entry[ 2 ] );
			try {
				item.setArtifactType( (String)entry[ 3 ] );
			}
			catch( ClassCastException e ) {
				System.out.println( entry[ 3 ] );
			}

			beansList.add( item );

		}
		return beansList;
	}

	@Override
	public List<ActivitiesByAgentReportItem> generateActivitiesByAgentReport(String agentIdent) {
		List<Object[]> data = reportDAO.getActivitiesByAgentsReportData(agentIdent, null, null, null, true );
		if ( data.size() == 0 )
			return new ArrayList<ActivitiesByAgentReportItem>();

		List<ActivitiesByAgentReportItem> beansList = new ArrayList<ActivitiesByAgentReportItem>();

		for ( Object[] entry : data ) {
			ActivitiesByAgentReportItem item = new ActivitiesByAgentReportItem();

			item.setAgentIdent( (String)entry[ 0 ] );
			item.setAgentName( (String)entry[ 1 ] );
			item.setAgentRole( (String)entry[ 2 ] );
			item.setAgentCostHour( (Double)entry[ 3 ] );
			item.setAgentWorkload( Integer.valueOf( (String)entry[ 4 ] ) );
			item.setProcessIdent( (String)entry[ 5 ] );
			item.setActivityName( (String)entry[ 6 ] );
			item.setActivityState( (String)entry[ 7 ] );
			item.setActivityDelay( (Integer)entry[ 8 ] );
			item.setActivityActualEnd((String)entry[ 9 ]);
			item.setActivityPlannedEnd((String)entry[ 10 ]);
			item.setActivityActualBegin((String)entry[ 11 ]);
			item.setActivityPlannedBegin((String)entry[ 12 ]);

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<CostDeviationReportItem> generateCostDeviationReport(String processIdent) {
		List<Object[]> data = reportDAO.getCostDeviationReportData(processIdent);

		if ( data.size() == 0 )
			return new ArrayList<CostDeviationReportItem>();

		List<CostDeviationReportItem> beansList = new ArrayList<CostDeviationReportItem>();
		for ( Object[] entry : data ) {
			CostDeviationReportItem item = new CostDeviationReportItem();

			item.setProcessIdent( (String)entry[ 0 ] );
			item.setActivityName( (String)entry[ 1 ] );
			item.setActualCost( (Double)entry[ 2 ] );
			item.setEstimatedCost( (Double)entry[ 3 ] );

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<ResourceStatesReportItem> generateResourceStatesReport() {
		List<Object[]> data = reportDAO.getResourceStatesReportData();

		if ( data.size() == 0 )
			return new ArrayList<ResourceStatesReportItem>();

		List<ResourceStatesReportItem> beansList = new ArrayList<ResourceStatesReportItem>();

		for ( Object[] entry : data ) {
			ResourceStatesReportItem item = new ResourceStatesReportItem();

			item.setResourceIdent( (String)entry[ 0 ] );
			item.setResourceType( (String)entry[ 1 ] );
			item.setResourceClass( (String)entry[ 2 ] );
			item.setResourceCost( (Double)entry[ 3 ] );
			item.setResourceUnit( (String)entry[ 4 ] );
			item.setResourceState( (String)entry[ 5 ] );

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<KnowledgeItensReportItem> generateKnowledgeItensReport(Date initialDate, Date finalDate) {
		List<Object[]> data = reportDAO.getKnowledgeItensReportData(initialDate, finalDate);

		if ( data.size() == 0 )
			return new ArrayList<KnowledgeItensReportItem>();

		List<KnowledgeItensReportItem> beansList = new ArrayList<KnowledgeItensReportItem>();
		SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
		for ( Object[] entry : data ) {
			KnowledgeItensReportItem item = new KnowledgeItensReportItem();

			item.setKnowledgeItemIdent( ( String ) entry[ 0 ] );
			item.setKnowledgeItemDate( ( entry[ 1 ] != null ? formatter.format( entry[ 1 ] ) : "" ) );
			item.setKnowledgeItemStatus( ( String ) entry[ 2 ] );
			item.setAgentIdent( ( String ) entry[ 3 ] );
			item.setAgentName( ( String ) entry[ 4 ] );

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<RequirementItem> generateRequirementItemReport(String params) {
		/*SystemDAO systemDAO = new SystemDAO();

		System system;
		system = null;

		system = (System) systemDAO.retrieveBySecondaryKey(params.getParams().get(RequirementListBySystemReportParam.SYSTEM).getName());
		Collection<Requirement> requirementsColl = null;*/

		return null;
	}

	@Override
	public List<AgentMetricsReportItem> generateAgentMetricsReport(String agentIdent) {
		List<Object[]> data = reportDAO.getAgentMetricsReportData(agentIdent);
		if ( data.size() == 0 )
			return new ArrayList<AgentMetricsReportItem>();

		List<AgentMetricsReportItem> beansList = new ArrayList<AgentMetricsReportItem>();

		for ( Object[] entry : data ) {
			SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
			AgentMetricsReportItem item = new AgentMetricsReportItem();

			item.setAgentIdent( (String)entry[ 0 ] );
			item.setAgentName( (String)entry[ 1 ] );
			item.setMetricDefinitionName( (String)entry[ 2 ] );
			item.setMetricValue( new Double( (Float)entry[ 3 ] ) );
			item.setMetricUnit( (String)entry[ 4 ] );
			item.setMetricPeriodBegin( ( entry[ 5 ] != null ? formatter.format( entry[ 5 ] ) : "" ) );
			item.setMetricPeriodEnd( ( entry[ 6 ] != null ? formatter.format( entry[ 6 ] ) : "" ) );

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<ArtifactMetricsReportItem> generateArtifactMetricsReport(String artifactIdent) {
		List<Object[]> data = reportDAO.getArtifactMetricsReportData(artifactIdent);

		List<ArtifactMetricsReportItem> beansList = new ArrayList<ArtifactMetricsReportItem>();

		for ( Object[] entry : data ) {
			SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
			ArtifactMetricsReportItem item = new ArtifactMetricsReportItem();

			item.setArtifactIdent( (String)entry[ 0 ] );
			item.setArtifactType( (String)entry[ 1 ] );
			item.setMetricDefinitionName( (String)entry[ 2 ] );
			item.setMetricValue( Double.valueOf( (Float)entry[ 3 ] ) );
			item.setMetricUnit( (String)entry[ 4 ] );
			item.setMetricPeriodBegin( ( entry[ 5 ] != null ? formatter.format( entry[ 5 ] ) : "" ) );
			item.setMetricPeriodEnd( ( entry[ 6 ] != null ? formatter.format( entry[ 6 ] ) : "" ) );

			beansList.add( item );
		}

		return beansList;
	}

	@Override
	public List<ResourceMetricsReportItem> generateResourceMetricsReport(String resourceIdent) {
		List<Object[]> data = reportDAO.getResourceMetricsReportData(resourceIdent);

		if ( data.size() == 0 )
			return new ArrayList<ResourceMetricsReportItem>();

		List<ResourceMetricsReportItem> beansList = new ArrayList<ResourceMetricsReportItem>();

		for ( Object[] entry : data ) {
			SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
			ResourceMetricsReportItem item = new ResourceMetricsReportItem();

			item.setResourceIdent( (String)entry[ 0 ] );
			item.setResourceType( (String)entry[ 1 ] );
			item.setMetricDefinitionName( (String)entry[ 2 ] );
			item.setMetricValue( Double.valueOf( (Float)entry[ 3 ] ) );
			item.setMetricUnit( (String)entry[ 4 ] );
			item.setMetricPeriodBegin( ( entry[ 5 ] != null ? formatter.format( entry[ 5 ] ) : "" ) );
			item.setMetricPeriodEnd( ( entry[ 6 ] != null ? formatter.format( entry[ 6 ] ) : "" ) );

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<SchedulePlanItem> generateSchedulePlanReport(String processIdent) {
		List<Object[]> data = new ArrayList<Object[]>();
		data.addAll(reportDAO.getScheduleData(processIdent));

		if ( data.size() == 0 )
			return new ArrayList<SchedulePlanItem>();

		List<SchedulePlanItem> beansList = new ArrayList<SchedulePlanItem>();

		for ( Object[] entry : data ) {
			SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
			SchedulePlanItem item = new SchedulePlanItem();

			item.setProcessIdent( (String)entry[ 0 ] );
			item.setActivityIdent( (String)entry[ 1 ] );
			item.setActivityName( (String)entry[ 2 ] );
			item.setActivityState( (String)entry[ 3 ] );
			item.setActivityPlannedBegin( ( entry[ 4 ] != null ? formatter.format( entry[ 4 ] ) : "" ) );
			item.setActivityPlannedEnd( entry[ 5 ] != null ? formatter.format( entry[ 5 ] ) : "" );
			item.setActivityEstimation( (Double)entry[ 6 ] );
			item.setActivityActualBegin( ( entry[ 7 ] != null ? formatter.format( entry[ 7 ] ) : "" ) );
			item.setActivityActualEnd( ( entry[ 8 ] != null ? formatter.format( entry[ 8 ] ) : "" ) );
			item.setActivityAvgWorkedHours( (Double)entry[ 9 ] );

			try {
				double deviation = (item.getActivityEstimation()==0 ? 0 : item.getActivityAvgWorkedHours() / item.getActivityEstimation() - 1);
				item.setHourDeviation( new Double( deviation ) );
			}
			catch( ArithmeticException e )
			{
				item.setHourDeviation( -1.0 );
			}

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<DocumentManagementPlanItem> generateDocumentManagementPlanReport(String processIdent) {
		List<Object[]> data = reportDAO.getDocumentManagementPlanData(processIdent);

		if ( data.size() == 0 )
			return new ArrayList<DocumentManagementPlanItem>();

		List<DocumentManagementPlanItem> beansList = new ArrayList<DocumentManagementPlanItem>();

		for ( Object[] entry : data ) {
			SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
			DocumentManagementPlanItem item = new DocumentManagementPlanItem();

			item.setProcessIdent( (String)entry[ 0 ] );
			item.setActivityName( (String)entry[ 1 ] );
			item.setPlannedBegin( ( entry[ 2 ] != null ? formatter.format( entry[ 2 ] ) : "" ) );
			item.setPlannedEnd( ( entry[ 3 ] != null ? formatter.format( entry[ 3 ] ) : "" ) );
			item.setAgentName( (String)entry[ 4 ] );
			item.setInputArtifact( (String)entry[ 5 ] );
			item.setOutputArtifact( (String)entry[ 6 ] );

			beansList.add( item );
		}
		return beansList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> generateHumanResourcesReport(String processIdent) {
		List<Object[]> data = reportDAO.getHumanResourcesPlanData(processIdent);
		List<AllocableActivityItem> activities = new ArrayList<AllocableActivityItem>();

		List<Object> listOfReturn = new ArrayList<Object>();

		if ( data.size() == 0 )
			return new ArrayList<Object>();

		List<HumanResourcesPlanItem> beansList = new ArrayList<HumanResourcesPlanItem>();

		for ( Object[] entry : data ) {
			HumanResourcesPlanItem item = new HumanResourcesPlanItem();

			item.setActivityIdent( (String)entry[ 0 ] );
			item.setActivityName( (String)entry[ 1 ] );

			for (Object[] agentEntry : (List<Object[]>)entry[2]) {
				AgentItemBean agentItem = new AgentItemBean();

				agentItem.setRoleName((String)agentEntry[0]);
				agentItem.setAgentName((String)agentEntry[1]);
				agentItem.setWorkingHours((Double)agentEntry[2]);

				item.addAgent(agentItem);
			}

			beansList.add( item );
		}
		listOfReturn.add(beansList);

		List<Object[]> allocableActivities = reportDAO.getAllocableActivitiesData(processIdent);

		for (Object[] activityArray : allocableActivities ) {
			AllocableActivityItem activity = new AllocableActivityItem();

			activity.setActivityIdent( (String)activityArray[ 0 ] );
			activity.setActivityName( (String)activityArray[ 1 ] );
			activity.setEstimatedHours( (Double)activityArray[ 2 ] );

			activities.add( activity );
		}
		listOfReturn.add(allocableActivities);

		return listOfReturn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ResourcesPlanItem> generateResourcesPlanReport(String processIdent) {
		List<Object[]> data = reportDAO.getResourcesPlanData(processIdent);

		if ( data.size() == 0 )
			return new ArrayList<ResourcesPlanItem>();

		List<ResourcesPlanItem> beansList = new ArrayList<ResourcesPlanItem>();

		for ( Object[] entry : data ) {
			ResourcesPlanItem item = new ResourcesPlanItem();

			item.setProcessIdent( (String)entry[ 0 ] );

			List<ResourceItem> exclusives = new ArrayList<ResourceItem>();
			List<ResourceItem> consumables = new ArrayList<ResourceItem>();
			List<ResourceItem> shareables = new ArrayList<ResourceItem>();

			for (Object[] exclusiveArray : (List<Object[]>)entry[ 1 ] ) {
				SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
				ResourceItem exclusive = new ResourceItem();

				exclusive.setName( (String)exclusiveArray[ 0 ] );
				exclusive.setDescription( (String)exclusiveArray[ 1 ] );
				exclusive.setBeginDate( ( exclusiveArray[ 2 ] != null ? formatter.format( (Date)exclusiveArray[ 2 ] ) : "" ) );
				exclusive.setEndDate( ( exclusiveArray[ 3 ] != null ? formatter.format( (Date)exclusiveArray[ 3 ] ) : "" ) );

				exclusives.add( exclusive );
			}

			for (Object[] consumableArray : (List<Object[]>)entry[ 2 ] ) {
				SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
				ResourcesCostConsumableItem consumable = new ResourcesCostConsumableItem();

				consumable.setName( (String)consumableArray[ 0 ] );
				consumable.setDescription( (String)consumableArray[ 1 ] );
				consumable.setBeginDate( ( consumableArray[ 2 ] != null ? formatter.format( (Date)consumableArray[ 2 ] ) : "" ) );
				consumable.setEndDate( ( consumableArray[ 3 ] != null ? formatter.format( (Date)consumableArray[ 3 ] ) : "" ) );

				consumables.add( consumable );
			}

			for (Object[] shareableArray : (List<Object[]>)entry[ 3 ] ) {
				SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
				ResourcesCostShareableItem shareable = new ResourcesCostShareableItem();

				shareable.setName( (String)shareableArray[ 0 ] );
				shareable.setDescription( (String)shareableArray[ 1 ] );
				shareable.setBeginDate( ( shareableArray[ 2 ] != null ? formatter.format( (Date)shareableArray[ 2 ] ) : "" ) );
				shareable.setEndDate( ( shareableArray[ 3 ] != null ? formatter.format( (Date)shareableArray[ 3 ] ) : "" ) );

				shareables.add( shareable );
			}

			item.setExclusives( exclusives );
			item.setConsumables( consumables );
			item.setShareables( shareables );

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<ProjectsBySystemReportItem> generateProjectsBySystemReport(String systemIdent) {
		List<Object[]> data = reportDAO.getProjectsBySystemReportData(systemIdent);

		if ( data.size() == 0 )
			return new ArrayList<ProjectsBySystemReportItem>();

		List<ProjectsBySystemReportItem> beansList = new ArrayList<ProjectsBySystemReportItem>();

		for ( Object[] entry : data ) {
			SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
			ProjectsBySystemReportItem item = new ProjectsBySystemReportItem();

			item.setSystemIdent( (String)entry[ 0 ] );
			item.setOrganizationIdent( (String)entry[ 1 ] );
			item.setProjectIdent( (String)entry[ 2 ] );
			item.setProjectBegin( ( entry[ 3 ] != null ? formatter.format( entry[ 3 ] ) : "" ) );
			item.setProjectEnd( ( entry[ 4 ] != null ? formatter.format( entry[ 4 ] ) : "" ) );

			beansList.add( item );
		}
		return beansList;
	}

	@Override
	public List<WorkBreakdownStructureItem> generateWorkBreakdownStructureReport(String processIdent) {
		List<Object[]> data = reportDAO.getWorkBreakdownStructureData(processIdent);

		if ( data.size() == 0 )
			return new ArrayList<WorkBreakdownStructureItem>();

		List<WorkBreakdownStructureItem> beansList = new ArrayList<WorkBreakdownStructureItem>();

		for ( Object[] entry : data ) {
			WorkBreakdownStructureItem item = new WorkBreakdownStructureItem();

			item.setProcessIdent( (String)entry[ 0 ] );
			item.setActivityName( (String)entry[ 1 ] );

			beansList.add( item );
		}

		return beansList;
	}

	@Override
	public List<ResourcesCostPlanItem> generateResourcesCostPlanReport(String processIdent) {
		List<Object[]> data = reportDAO.getResourcesCostPlanData(processIdent);

		if ( data.size() == 0 )
			return new ArrayList<ResourcesCostPlanItem>();

		List<ResourcesCostPlanItem> beansList = new ArrayList<ResourcesCostPlanItem>();

		for ( Object[] entry : data ) {
			ResourcesCostPlanItem item = new ResourcesCostPlanItem();

			item.setProcessIdent( (String)entry[ 0 ] );

			List<ResourcesCostExclusiveItem> exclusives = new ArrayList<ResourcesCostExclusiveItem>();
			List<ResourcesCostConsumableItem> consumables = new ArrayList<ResourcesCostConsumableItem>();
			List<ResourcesCostShareableItem> shareables = new ArrayList<ResourcesCostShareableItem>();
			List<ResourcesCostHumanItem> humans = new ArrayList<ResourcesCostHumanItem>();
			List<AllocableActivityItem> activities = new ArrayList<AllocableActivityItem>();

			for (Object[] exclusiveArray : (List<Object[]>)entry[ 1 ] ) {
				ResourcesCostExclusiveItem exclusive = new ResourcesCostExclusiveItem();

				exclusive.setName( (String)exclusiveArray[ 0 ] );
				exclusive.setDescription( (String)exclusiveArray[ 1 ] );
				exclusive.setCost( new Double( (Float)exclusiveArray[ 2 ] ) );

				exclusives.add( exclusive );
			}

			for (Object[] consumableArray : (List<Object[]>)entry[ 2 ] ) {
				ResourcesCostConsumableItem consumable = new ResourcesCostConsumableItem();

				consumable.setName( (String)consumableArray[ 0 ] );
				consumable.setDescription( (String)consumableArray[ 1 ] );
				consumable.setAmount( new Double( (Float)consumableArray[ 2 ] ) );
				consumable.setCost( new Double( (Float)consumableArray[ 3 ] ) );

				consumables.add( consumable );
			}

			for (Object[] shareableArray : (List<Object[]>)entry[ 3 ] ) {
				SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" );
				ResourcesCostShareableItem shareable = new ResourcesCostShareableItem();

				shareable.setName( (String)shareableArray[ 0 ] );
				shareable.setDescription( (String)shareableArray[ 1 ] );
				shareable.setBeginDate( ( shareableArray[ 2 ] != null ? formatter.format( (Date)shareableArray[ 2 ] ) : "" ) );
				shareable.setEndDate( ( shareableArray[ 3 ] != null ? formatter.format( (Date)shareableArray[ 3 ] ) : "" ) );

				shareables.add( shareable );
			}

			for (Object[] humanArray : (List<Object[]>)entry[ 4 ] ) {
				ResourcesCostHumanItem human = new ResourcesCostHumanItem();

				human.setName( (String)humanArray[ 0 ] );
				human.setCostHour( (Double)humanArray[ 1 ] );
				human.setWorkingHours( (Double)humanArray[ 2 ] );
				human.setEstimatedHours( (Double)humanArray[ 3 ] );

				humans.add( human );
			}

			for (Object[] activityArray : (List<Object[]>)entry[ 5 ] ) {
				AllocableActivityItem activity = new AllocableActivityItem();

				activity.setActivityIdent( (String)activityArray[ 0 ] );
				activity.setActivityName( (String)activityArray[ 1 ] );
				activity.setEstimatedHours( (Double)activityArray[ 2 ] );

				activities.add( activity );
			}

			item.setExclusives( exclusives );
			item.setConsumables( consumables );
			item.setShareables( shareables );
			item.setHumans( humans );
			item.setActivities(activities);

			beansList.add( item );
		}

		return beansList;
	}
}
