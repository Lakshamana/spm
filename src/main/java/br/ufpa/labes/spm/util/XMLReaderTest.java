package br.ufpa.labes.spm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.hibernate.Session;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.filter.AbstractFilter;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.qrconsult.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.domain.Ability;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.AgentAffinityAgent;
import br.ufpa.labes.spm.domain.AgentHasAbility;
import br.ufpa.labes.spm.domain.AgentPlaysRole;
import br.ufpa.labes.spm.domain.WorkGroup;
import br.ufpa.labes.spm.domain.Role;
import br.ufpa.labes.spm.domain.RoleNeedsAbility;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.ArtifactTask;
import br.ufpa.labes.spm.domain.ArtifactCon;
import br.ufpa.labes.spm.domain.BranchANDCon;
import br.ufpa.labes.spm.domain.BranchConCond;
import br.ufpa.labes.spm.domain.BranchConCondToActivity;
import br.ufpa.labes.spm.domain.BranchConCondToMultipleCon;
import br.ufpa.labes.spm.domain.Connection;
import br.ufpa.labes.spm.domain.Dependency;
import br.ufpa.labes.spm.domain.Feedback;
import br.ufpa.labes.spm.domain.JoinCon;
import br.ufpa.labes.spm.domain.MultipleCon;
import br.ufpa.labes.spm.domain.Sequence;
import br.ufpa.labes.spm.domain.AgendaEvent;
import br.ufpa.labes.spm.domain.CatalogEvents;
import br.ufpa.labes.spm.domain.ConnectionEvent;
import br.ufpa.labes.spm.domain.GlobalActivityEvent;
import br.ufpa.labes.spm.domain.Log;
import br.ufpa.labes.spm.domain.ModelingActivityEvent;
import br.ufpa.labes.spm.domain.ProcessEvent;
import br.ufpa.labes.spm.domain.ProcessModelEvent;
import br.ufpa.labes.spm.domain.ResourceEvent;
import br.ufpa.labes.spm.domain.Company;
import br.ufpa.labes.spm.domain.Project;
import br.ufpa.labes.spm.domain.Organization;
import br.ufpa.labes.spm.domain.ArtifactParam;
import br.ufpa.labes.spm.domain.Automatic;
import br.ufpa.labes.spm.domain.EnactionDescription;
import br.ufpa.labes.spm.domain.InvolvedArtifacts;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.domain.PrimitiveParam;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.domain.ReqAgentRequiresAbility;
import br.ufpa.labes.spm.domain.ReqWorkGroup;
import br.ufpa.labes.spm.domain.RequiredResource;
import br.ufpa.labes.spm.domain.ActivityEstimation;
import br.ufpa.labes.spm.domain.ActivityMetric;
import br.ufpa.labes.spm.domain.AgentEstimation;
import br.ufpa.labes.spm.domain.AgentMetric;
import br.ufpa.labes.spm.domain.ArtifactEstimation;
import br.ufpa.labes.spm.domain.ArtifactMetric;
import br.ufpa.labes.spm.domain.WorkGroupEstimation;
import br.ufpa.labes.spm.domain.WorkGroupMetric;
import br.ufpa.labes.spm.domain.MetricDefinition;
import br.ufpa.labes.spm.domain.OrganizationEstimation;
import br.ufpa.labes.spm.domain.OrganizationMetric;
import br.ufpa.labes.spm.domain.ProcessEstimation;
import br.ufpa.labes.spm.domain.ProcessMetric;
import br.ufpa.labes.spm.domain.ResourceEstimation;
import br.ufpa.labes.spm.domain.ResourceMetric;
import br.ufpa.labes.spm.domain.GraphicCoordinate;
import br.ufpa.labes.spm.domain.WebAPSEEObject;
import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.domain.Template;
import br.ufpa.labes.spm.domain.Consumable;
import br.ufpa.labes.spm.domain.Exclusive;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.domain.Shareable;
import br.ufpa.labes.spm.domain.ProcessAgenda;
import br.ufpa.labes.spm.domain.Task;
import br.ufpa.labes.spm.domain.TaskAgenda;
import br.ufpa.labes.spm.domain.ClassMethodCall;
import br.ufpa.labes.spm.domain.PrimitiveType;
import br.ufpa.labes.spm.domain.Script;
import br.ufpa.labes.spm.domain.Subroutine;
import br.ufpa.labes.spm.domain.ToolDefinition;
import br.ufpa.labes.spm.domain.ToolParameters;
import br.ufpa.labes.spm.domain.AbilityType;
import br.ufpa.labes.spm.domain.ActivityType;
import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.domain.EventType;
import br.ufpa.labes.spm.domain.WorkGroupType;
import br.ufpa.labes.spm.domain.MetricType;
import br.ufpa.labes.spm.domain.PolicyType;
import br.ufpa.labes.spm.domain.ResourceType;
import br.ufpa.labes.spm.domain.RoleType;
import br.ufpa.labes.spm.domain.ToolType;
import br.ufpa.labes.spm.domain.Type;
import br.ufpa.labes.spm.domain.System;
import br.ufpa.labes.spm.domain.Process;

public class XMLReaderTest {

	/*
	 * Type (and subclasses), Ability, Agent, TaskAgenda, WorkGroup, Role,
	 * Artifact, Project, Organization, System, MetricDefinition,
	 * Resource (and subclasses), ToolDefinition,
	 * Subroutine (and classes), ToolParameters, PrimitiveType
	 *
	 */
	private Hashtable<String,Object> organizational; // Organizational objects that must be in the resulting XML file <CanonicalClassName_Oid, Object Reference>

	/*
	 * Process, ProcessModel, Activity (and subclasses), EnactionDescription, Dependency,
	 * RequiredPeople (and subclasses), RequiredResource, Reservation, InvolvedArtifacts,
	 * Connection (and subclasses), Log, Event (and subclasses), CatalogEvent,
	 * Parameters (and subclasses), Metric, Estimation, ProcessAgenda, Task
	 */
	private Hashtable<String,Object> processComponents; // Process objects that must be in the resulting XML file <CanonicalClassName_Oid, Object Reference>

	/*
	 * AgentAffinityAgent, AgentHasAbility, AgentPlaysRole, RoleNeedsAbility,
	 * ArtifactTask, BranchConCondToActivity, BranchConCondToMultipleCon,
	 * ReqAgentRequiresAbility
	 */
	private Hashtable<String,Object> associatives; // Associative objects that must be in the resulting XML file <CanonicalClassName_Oid, Object Reference>

	// Mapping for old and new connections identification
	Properties idCons;

	Process process;

	private InitialContext context;

	public XMLReaderTest() {
		this.organizational = new Hashtable<String,Object>();
		this.associatives = new Hashtable<String,Object>();
		this.processComponents = new Hashtable<String,Object>();

		this.idCons = new Properties();
		try {
			this.context = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}

//		try {
//			File newFile = new File(filePath);
//			FileInputStream xml = new FileInputStream(newFile.getPath());
//			ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//			byte[] buffer = new byte[1024];
//			int len;
//			while ((len = xml.read(buffer)) != -1) {
//				stream.write(buffer, 0, len);
//			}
//
//			stream.toString("ISO-8859-1");
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void importProcess(String processId, String xml) {
		String xmlContent = null;
		try {
			File newFile = new File(xml);
			FileInputStream xmlInputStream = new FileInputStream(newFile.getPath());
			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int len;
			while ((len = xmlInputStream.read(buffer)) != -1) {
				stream.write(buffer, 0, len);
			}

			xmlContent = stream.toString("ISO-8859-1");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes("ISO-8859-1"));

			SAXBuilder sxb = new SAXBuilder();
			Document document = sxb.build(inputStream);
			Element rootElement = document.getRootElement();

			Element processComponents = rootElement.getChild("ProcessComponents");
			Element processElm = processComponents.getChild(Process.class.getSimpleName());
			if(processElm == null){
				processElm = processComponents.getChild(Template.class.getSimpleName());
			}
			processId = processElm.getChildText("Ident");

			Element organizational = rootElement.getChild("Organizational");
			this.loadOrganizational(organizational);

			// Load Associative elements (classes) to Database
			Element associatives = rootElement.getChild("Associatives");
			this.loadOrgAssociatives(associatives);

			// Load Process Components elements to Database
			this.loadProcessIssues(processComponents, organizational);

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadOrganizational(Element organizational) {

		this.loadTypes(organizational);

		this.loadOrganization(organizational);

		// At this point, all organizational objects are saved
		// and related to their organizational relationships settled.
	}

	private void loadTypes(Element organizational) {

		ElementFilter abilityTypeFilter = new ElementFilter(AbilityType.class.getSimpleName());
		ElementFilter activityTypeFilter = new ElementFilter(ActivityType.class.getSimpleName());
		ElementFilter artifactTypeFilter = new ElementFilter(ArtifactType.class.getSimpleName());
		ElementFilter eventTypeFilter = new ElementFilter(EventType.class.getSimpleName());
		ElementFilter WorkGroupTypeFilter = new ElementFilter(WorkGroupType.class.getSimpleName());
		ElementFilter metricTypeFilter = new ElementFilter(MetricType.class.getSimpleName());
		ElementFilter policyTypeFilter = new ElementFilter(PolicyType.class.getSimpleName());
		ElementFilter resourceTypeFilter = new ElementFilter(ResourceType.class.getSimpleName());
		ElementFilter roleTypeFilter = new ElementFilter(RoleType.class.getSimpleName());
		ElementFilter toolTypeFilter = new ElementFilter(ToolType.class.getSimpleName());

		AbstractFilter typeFilter = ((AbstractFilter) abilityTypeFilter.or(activityTypeFilter));
		typeFilter = ((AbstractFilter) typeFilter.or(artifactTypeFilter));
		typeFilter = ((AbstractFilter) typeFilter.or(eventTypeFilter));
		typeFilter = ((AbstractFilter) typeFilter.or(WorkGroupTypeFilter));
		typeFilter = ((AbstractFilter) typeFilter.or(metricTypeFilter));
		typeFilter = ((AbstractFilter) typeFilter.or(policyTypeFilter));
		typeFilter = ((AbstractFilter) typeFilter.or(resourceTypeFilter));
		typeFilter = ((AbstractFilter) typeFilter.or(roleTypeFilter));
		typeFilter = ((AbstractFilter) typeFilter.or(toolTypeFilter));

		Iterator<Element> iterTypes = organizational.getDescendants(typeFilter);
		while (iterTypes.hasNext()) {
			Element typeElement = (Element) iterTypes.next();
			this.buildOrgObject(typeElement);
		}

		iterTypes = organizational.getDescendants(typeFilter);
		while (iterTypes.hasNext()) {
			Element typeElement = (Element) iterTypes.next();

			Element superTypeElm = typeElement.getChild("SuperType");
			if(superTypeElm != null){
				String typeKey = typeElement.getAttributeValue("KEY");
				Type type = (Type) this.organizational.get(typeKey);
				String superKey = superTypeElm.getAttributeValue("REF");
				Type superType = (Type) this.organizational.get(superKey);
				type.insertIntoSuperType(superType);
			}
		}
	}

	private void loadOrganization(Element organizational) {
		Element organization = organizational.getChild(Organization.class.getSimpleName());
		this.buildOrgObject(organization);
	}

	private void loadSystem(Element organizational) {
		Element system = organizational.getChild(System.class.getSimpleName());
		System sys = (System) this.buildOrgObject(system);
		if(sys == null) return;
		// Setting organization...
		Element orgElm = system.getChild("TheOrganization");
		if(orgElm != null){
			String orgKey = orgElm.getAttributeValue("REF");
			Company company = new Company();
			sys.insertIntoTheOrganization(company);
//			sys.insertIntoTheOrganization((Organization) this.organizational.get(orgKey));
		}
	}

	private void loadProject(Element organizational) {
		List<Element> projects = organizational.getChildren(Project.class.getSimpleName());
		Iterator<Element> iter = projects.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Project project = (Project) this.buildOrgObject(element);
			if(project == null) continue;
			// Setting organization...
			Element sysElm = element.getChild("TheSystem");
			if(sysElm != null){
				String sysKey = sysElm.getAttributeValue("REF");
				project.insertIntoTheSystem((System) this.organizational.get(sysKey));
			}
		}
	}

	private void loadArtifact(Element organizational) {
		List<Element> artifacts = organizational.getChildren(Artifact.class.getSimpleName());
		Iterator<Element> iter = artifacts.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Artifact artifact = (Artifact) this.buildOrgObject(element);
			if(artifact == null) continue;
			// Setting type...
			String typeKey = element.getChild("TheArtifactType").getAttributeValue("REF");
			artifact.insertIntoTheArtifactType((ArtifactType) this.organizational.get(typeKey));
		}

		iter = artifacts.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Artifact artifact = (Artifact) this.organizational.get(element.getAttributeValue("KEY"));

			// Setting belongsTo...
			Element belongsToElm = element.getChild("BelongsTo");
			if(belongsToElm != null){
				String belongsToKey = belongsToElm.getAttributeValue("REF");
				artifact.insertIntoBelongsTo((Artifact) this.organizational.get(belongsToKey));
			}

			// Setting derivedFrom...
			Element derivedFromElm = element.getChild("BelongsTo");
			if(derivedFromElm != null){
				String derivedFromKey = derivedFromElm.getAttributeValue("REF");
				artifact.insertIntoDerivedFrom((Artifact) this.organizational.get(derivedFromKey));
			}
		}
	}

	private void loadAbility(Element organizational){
		List<Element> abilities = organizational.getChildren(Ability.class.getSimpleName());
		Iterator<Element> iter = abilities.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Ability ability = (Ability) this.buildOrgObject(element);
			if(ability == null) continue;
			// Setting type...
			String typeKey = element.getChild("TheAbilityType").getAttributeValue("REF");
			ability.insertIntoTheAbilityType((AbilityType) this.organizational.get(typeKey));

			this.organizational.put(element.getAttributeValue("KEY"), ability);
		}
	}

	private void loadRole(Element organizational) {
		List<Element> roles = organizational.getChildren(Role.class.getSimpleName());
		Iterator<Element> iter = roles.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Role role = (Role) this.buildOrgObject(element);
			if(role == null) continue;
			// Setting type...
			String typeKey = element.getChild("TheRoleType").getAttributeValue("REF");
			role.insertIntoTheRoleType((RoleType) this.organizational.get(typeKey));
		}

		iter = roles.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Role role = (Role) this.organizational.get(element.getAttributeValue("KEY"));

			// Setting subordinate
			Element subordElm = element.getChild("Subordinate");
			if(subordElm != null){
				String subordKey = subordElm.getAttributeValue("REF");
				role.insertIntoSubordinate((Role) this.organizational.get(subordKey));
			}
		}
	}

	private void loadAgent(Element organizational) {

		List<Element> agents = organizational.getChildren(Agent.class.getSimpleName());
		Iterator<Element> iter = agents.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			this.buildOrgObject(element);
		}
	}

	private void loadTaskAgenda(Element organizational) {

		List<Element> taskAgendas = organizational.getChildren(TaskAgenda.class.getSimpleName());
		Iterator<Element> iter = taskAgendas.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();

			 String agentREF = element.getChild("TheAgent").getAttributeValue("REF");
			 Agent agent = (Agent) this.organizational.get(agentREF);

			 // At this operation, the database (or default) task agenda is inserted into the organizational hash table.
			 this.organizational.put(element.getAttributeValue("KEY"), agent.getTheTaskAgenda());
		}
	}

	private void loadWorkGroup(Element organizational) {
		List<Element> WorkGroups = organizational.getChildren(WorkGroup.class.getSimpleName());
		Iterator<Element> iter = WorkGroups.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			WorkGroup WorkGroup = (WorkGroup) this.buildOrgObject(element);
			if(WorkGroup == null) continue;
			// Setting type...
			String typeKey = element.getChild("TheWorkGroupType").getAttributeValue("REF");
			WorkGroup.insertIntoTheWorkGroupType((WorkGroupType) this.organizational.get(typeKey));

			// Setting super WorkGroup...
			Element superElm = element.getChild("SuperWorkGroup");
			if(superElm != null){
				String superKey = superElm.getAttributeValue("REF");
				WorkGroup.insertIntoSuperWorkGroup((WorkGroup) this.organizational.get(superKey));
			}

			// Setting agents...
			Element agents = element.getChild("TheAgent");
			if(agents != null){
				List<Element> itens = agents.getChildren("Item");
				Iterator<Element> iterItens = itens.iterator();
				while (iterItens.hasNext()) {
					Element agentElm = (Element) iterItens.next();
					String agentKey = agentElm.getValue();
					WorkGroup.insertIntoTheAgent((Agent) this.organizational.get(agentKey));
				}
			}

			this.organizational.put(element.getAttributeValue("KEY"), WorkGroup);
		}
	}

	private void loadMetricDefinition(Element organizational) {
		List<Element> metricDefinitions = organizational.getChildren(MetricDefinition.class.getSimpleName());
		Iterator<Element> iter = metricDefinitions.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			this.buildOrgObject(element);
		}
	}

	private void loadResource(Element organizational) {

		ElementFilter shareableFilter = new ElementFilter(Shareable.class.getSimpleName());
		ElementFilter exclusiveFilter = new ElementFilter(Exclusive.class.getSimpleName());
		ElementFilter consumableFilter = new ElementFilter(Consumable.class.getSimpleName());

		AbstractFilter resourceFilter = ((AbstractFilter) shareableFilter.or(exclusiveFilter));
		resourceFilter = ((AbstractFilter) resourceFilter.or(consumableFilter));

		Iterator<Element> iter = organizational.getDescendants(resourceFilter);
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Resource resource = (Resource) this.buildOrgObject(element);
			if(resource == null) continue;
			// Setting type...
			String typeKey = element.getChild("TheResourceType").getAttributeValue("REF");
			resource.insertIntoTheResourceType((ResourceType) this.organizational.get(typeKey));

			// Setting belongsTo...
			Element belongsToElm = element.getChild("BelongsTo");
			if(belongsToElm != null){
				String belongsToKey = belongsToElm.getAttributeValue("REF");
				resource.insertIntoBelongsTo((Resource) this.organizational.get(belongsToKey));
			}

			this.organizational.put(element.getAttributeValue("KEY"), resource);
		}
	}

	private void loadToolDefinition(Element organizational) {
		List<Element> toolDefinitions = organizational.getChildren(ToolDefinition.class.getSimpleName());
		Iterator<Element> iter = toolDefinitions.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			ToolDefinition toolDef = (ToolDefinition) this.buildOrgObject(element);
			if(toolDef == null) continue;
			// Setting type...
			String typeKey = element.getChild("TheToolType").getAttributeValue("REF");
			toolDef.insertIntoTheToolType((ToolType) this.organizational.get(typeKey));

			// Setting artifact types...
			Element artTypes = element.getChild("TheArtifactType");
			if(artTypes != null){
				List<Element> itens = element.getChildren("Item");
				Iterator<Element> iterItens = itens.iterator();
				while (iterItens.hasNext()) {
					Element artTypeElm = (Element) iterItens.next();
					String artTypeKey = artTypeElm.getValue();
					toolDef.insertIntoTheArtifactType((ArtifactType) this.organizational.get(artTypeKey));
				}
			}

			this.organizational.put(element.getAttributeValue("KEY"), toolDef);
		}
	}

	private void loadSubroutine(Element organizational) {

		ElementFilter scriptFilter = new ElementFilter(Script.class.getSimpleName());
		ElementFilter classMethodCallFilter = new ElementFilter(ClassMethodCall.class.getSimpleName());
		AbstractFilter subroutineFilter = ((AbstractFilter) classMethodCallFilter.or(scriptFilter));

		Iterator<Element> iter = organizational.getDescendants(subroutineFilter);
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			Subroutine subroutine = (Subroutine) this.buildOrgObject(element);
			if(subroutine == null) continue;
			// Setting type...
			String typeKey = element.getChild("TheArtifactType").getAttributeValue("REF");
			subroutine.insertIntoTheArtifactType((ArtifactType) this.organizational.get(typeKey));

			this.organizational.put(element.getAttributeValue("KEY"), subroutine);
		}
	}

	private void loadPrimitiveType(Element organizational) {
		List<Element> primitiveTypes = organizational.getChildren(PrimitiveType.class.getSimpleName());
		Iterator<Element> iter = primitiveTypes.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			this.buildOrgObject(element);
		}
	}

	private void loadToolParameters(Element organizational) {
		List<Element> toolParameters = organizational.getChildren(ToolParameters.class.getSimpleName());
		Iterator<Element> iter = toolParameters.iterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			ToolParameters toolParams = new ToolParameters();
			toolParams.setLabel(element.getChildText("Label"));
			toolParams.setSeparatorSymbol(element.getChildText("SeparatorSymbol"));

			// Setting artifact type...
			String typeKey = element.getChild("TheArtifactType").getAttributeValue("REF");
			toolParams.insertIntoTheArtifactType((ArtifactType) this.organizational.get(typeKey));

			// Setting primitive type...
			Element primitiveElm = element.getChild("ThePrimitiveType");
			if(primitiveElm != null){
				String primitiveKey = primitiveElm.getAttributeValue("REF");
				toolParams.insertIntoThePrimitiveType((PrimitiveType) this.organizational.get(primitiveKey));
			}

			// Setting subroutine...
			Element subroutineElm = element.getChild("TheSubroutine");
			if(subroutineElm != null){
				String subroutineKey = subroutineElm.getAttributeValue("REF");
				toolParams.insertIntoTheSubroutine((Subroutine) this.organizational.get(subroutineKey));
			}

			this.persistObject(toolParams, null);

			this.organizational.put(element.getAttributeValue("KEY"), toolParams);
		}
	}

	/**
	 * Import Organizational Associative Objects
	 */
	private void loadOrgAssociatives(Element associatives) {
		/*
		 * ArtifactTask, BranchConCondToActivity, BranchConCondToMultipleCon,
		 * ReqAgentRequiresAbility
		 */
		List<Element> aaas = associatives.getChildren(AgentAffinityAgent.class.getSimpleName());
		Iterator<Element> iteraaas = aaas.iterator();
		while (iteraaas.hasNext()) {
			Element aaaElm = (Element) iteraaas.next();

			String key = aaaElm.getAttributeValue("KEY");

			Element toAffinityElm = aaaElm.getChild("ToAffinity");
			String toKey = toAffinityElm.getAttributeValue("REF");

			Agent toAffinity = (Agent) this.organizational.get(toKey);

			Element fromAffinityElm = aaaElm.getChild("FromAffinity");
			String fromKey = fromAffinityElm.getAttributeValue("REF");

			Agent fromAffinity = (Agent) this.organizational.get(fromKey);

			if(!this.isAssociativeExists(AgentAffinityAgent.class, fromAffinity, toAffinity)){

				AgentAffinityAgent aaa = new AgentAffinityAgent();
				aaa.insertIntoToAffinity(toAffinity);
				aaa.insertIntoFromAffinity(fromAffinity);
				aaa.setDegree(Integer.valueOf(aaaElm.getChildText("Degree")));

				aaa = (AgentAffinityAgent) this.persistObject(aaa, null);

				this.associatives.put(key, aaa);
			}
		}

		List<Element> ahas = associatives.getChildren(AgentHasAbility.class.getSimpleName());
		Iterator<Element> iterahas = ahas.iterator();
		while (iterahas.hasNext()) {
			Element ahaElm = (Element) iterahas.next();

			String key = ahaElm.getAttributeValue("KEY");

			Element abilityElm = ahaElm.getChild("TheAbility");
			String abilityKey = abilityElm.getAttributeValue("REF");

			Ability ability = (Ability) this.organizational.get(abilityKey);

			Element agentElm = ahaElm.getChild("TheAgent");
			String agentKey = agentElm.getAttributeValue("REF");

			Agent agent = (Agent) this.organizational.get(agentKey);

			if(!this.isAssociativeExists(AgentHasAbility.class, agent, ability)){

				AgentHasAbility aha = new AgentHasAbility();
				aha.insertIntoTheAbility(ability);
				aha.insertIntoTheAgent(agent);
				aha.setDegree(Integer.valueOf(ahaElm.getChildText("Degree")));

				aha = (AgentHasAbility) this.persistObject(aha, null);

				this.associatives.put(key, aha);
			}
		}

		List<Element> aprs = associatives.getChildren(AgentPlaysRole.class.getSimpleName());
		Iterator<Element> iteraprs = aprs.iterator();
		while (iteraprs.hasNext()) {
			Element aprElm = (Element) iteraprs.next();

			String key = aprElm.getAttributeValue("KEY");

			Element roleElm = aprElm.getChild("TheRole");
			String roleKey = roleElm.getAttributeValue("REF");

			Role role = (Role) this.organizational.get(roleKey);

			Element agentElm = aprElm.getChild("TheAgent");
			String agentKey = agentElm.getAttributeValue("REF");

			Agent agent = (Agent) this.organizational.get(agentKey);

			if(!this.isAssociativeExists(AgentPlaysRole.class, agent, role)){

				AgentPlaysRole apr = new AgentPlaysRole();
				apr.insertIntoTheRole(role);
				apr.insertIntoTheAgent(agent);
				apr.setSince_date((Date) this.buildAttribute(Date.class, aprElm.getChildText("Since_date")));

				apr = (AgentPlaysRole) this.persistObject(apr, null);

				this.associatives.put(key, apr);
			}
		}

		List<Element> rnas = associatives.getChildren(RoleNeedsAbility.class.getSimpleName());
		Iterator<Element> iterrnas = rnas.iterator();
		while (iterrnas.hasNext()) {
			Element rnaElm = (Element) iterrnas.next();

			String key = rnaElm.getAttributeValue("KEY");

			Element roleElm = rnaElm.getChild("TheRole");
			String roleKey = roleElm.getAttributeValue("REF");

			Role role = (Role) this.organizational.get(roleKey);

			Element abilityElm = rnaElm.getChild("TheAbility");
			String abilityKey = abilityElm.getAttributeValue("REF");

			Ability ability = (Ability) this.organizational.get(abilityKey);

			if(!this.isAssociativeExists(RoleNeedsAbility.class, role, ability)){

				RoleNeedsAbility rna = new RoleNeedsAbility();
				rna.insertIntoTheRole(role);
				rna.insertIntoTheAbility(ability);
				rna.setDegree(Integer.valueOf(rnaElm.getChildText("Degree")));

				rna = (RoleNeedsAbility) this.persistObject(rna, null);

				this.associatives.put(key, rna);
			}
		}
	}

	/**
	 * Import Process Components Objects
	 */
	private void loadProcessIssues(Element processComponents, Element organizational) {

		this.loadProcess(processComponents, organizational);

		// Load activities
		this.loadActivity(processComponents);

		// Load enaction descriptions
		this.loadEnactionDescription(processComponents);

		// Load required people
		this.loadRequiredPeople(processComponents, organizational);

		// Load required resource
		this.loadRequiredResource(processComponents, organizational);

		// Load involved artifacts
		this.loadInvolvedArtifacts(processComponents, organizational);

		// Load connections
		this.loadConnection(processComponents);

		// Load dependencies
		this.loadDependency(processComponents);

		// Load process models
		this.loadProcessModel(processComponents);

		// Load metrics and estimations
		this.loadMetricAndEstimation(processComponents, organizational);

		// Load process agendas
		this.loadProcessAgenda(processComponents, organizational);

		// Load tasks
		this.loadTask(processComponents);

		// Load events
		this.loadEvent(processComponents);

		// Load catalog events
		this.loadCatalogEvent(processComponents);

		// Load conditions
		this.loadPolCondition(processComponents);

		// Load parameters
		this.loadParameters(processComponents, organizational);
	}

	private void loadProcess(Element processComponents, Element organizational){

		Element processElm = processComponents.getChild(Process.class.getSimpleName());
		if(processElm == null){
			processElm = processComponents.getChild(Template.class.getSimpleName());
		}

		process = (Process) this.buildProcObject(processElm);

		if(process == null) return;

		// Setting type...
		Element activityType = processElm.getChild("TheActivityType");
		if(activityType != null){
			String typeKey = activityType.getAttributeValue("REF");
			process.insertIntoTheActivityType((ActivityType) this.organizational.get(typeKey));
		}

		// Setting project...
		Element projectElm = organizational.getChild(Project.class.getSimpleName());
		if(projectElm != null){
			String projectKey = projectElm.getAttributeValue("KEY");
			Project project = (Project) this.organizational.get(projectKey);
			project.insertIntoProcessRefered(process);
			this.organizational.put(projectElm.getAttributeValue("KEY"), project);
		}

		// Setting agents...
		Element agents = processElm.getChild("TheAgent");
		if(agents != null){
			List<Element> itens = agents.getChildren("Item");
			Iterator<Element> iterItens = itens.iterator();
			while (iterItens.hasNext()) {
				Element agentElm = (Element) iterItens.next();
				String agentKey = agentElm.getValue();
				process.insertIntoTheAgent((Agent) this.organizational.get(agentKey));
			}
		}

		// Setting Log into the hash table
		String logKey = processComponents.getChild(Log.class.getSimpleName()).getAttributeValue("KEY");
		this.processComponents.put(logKey, process.getTheLog());

		this.processComponents.put(processElm.getAttributeValue("KEY"), process);
	}

	private void loadProcessModel(Element processComponents){

		List<Element> processModels = processComponents.getChildren(ProcessModel.class.getSimpleName());
		Iterator<Element> iter = processModels.iterator();
		while (iter.hasNext()) {
			Element processModelElm = (Element) iter.next();

			Element processElm = processModelElm.getChild("TheProcess");
			ProcessModel processModel = null;
			if(processElm != null){
				String processKey = processElm.getAttributeValue("REF");
				Process process = (Process) this.processComponents.get(processKey);
				processModel = process.getTheProcessModel();
			}
			else{
				Element decomposedElm = processModelElm.getChild("TheDecomposed");
				String decomposedKey = decomposedElm.getAttributeValue("REF");
				Decomposed decomposed = (Decomposed) this.processComponents.get(decomposedKey);
				processModel = decomposed.getTheReferedProcessModel();
			}

			processModel.setPmState(processModelElm.getChildText("PmState"));
			processModel.setRequirements(processModelElm.getChildText("Requirements"));

			Element actsElm = processModelElm.getChild("TheActivity");
			if(actsElm != null){
				Collection<Element> itens = actsElm.getChildren("Item");
				Iterator<Element> iterItens = itens.iterator();
				while (iterItens.hasNext()) {
					Element item = (Element) iterItens.next();
					if(item != null){
						String actKey = item.getValue();
						processModel.insertIntoTheActivity((Activity) this.processComponents.get(actKey));
					}
				}
			}

			Element connsElm = processModelElm.getChild("TheConnection");
			if(connsElm != null){
				Collection<Element> connItens = connsElm.getChildren("Item");
				Iterator<Element> iterConnItens = connItens.iterator();
				while (iterConnItens.hasNext()) {
					Element item = (Element) iterConnItens.next();
					if(item != null){
						String connKey = item.getValue();
						processModel.insertIntoTheConnection((Connection) this.processComponents.get(connKey));
					}
				}
			}

			this.persistObject(processModel, null);
			this.processComponents.put(processModelElm.getAttributeValue("KEY"), processModel);
		}
	}

	private void loadEnactionDescription(Element processComponents){

		List<Element> enactionDescs = processComponents.getChildren(EnactionDescription.class.getSimpleName());
		Iterator<Element> iter = enactionDescs.iterator();
		while (iter.hasNext()) {
			Element enactionDescElm = (Element) iter.next();
			EnactionDescription enactionDesc = new EnactionDescription();
			enactionDesc.setState(enactionDescElm.getChildText("State"));
			enactionDesc.setActualBegin((Date) this.buildAttribute(Date.class, enactionDescElm.getChildText("ActualBegin")));
			enactionDesc.setActualEnd((Date) this.buildAttribute(Date.class, enactionDescElm.getChildText("ActualEnd")));

			Element plainElm = enactionDescElm.getChild("ThePlain");
			if(plainElm != null){
				String plainKey = plainElm.getAttributeValue("REF");
				enactionDesc.insertIntoThePlain((Plain) this.processComponents.get(plainKey));
			}

			this.persistObject(enactionDesc, null);

			this.processComponents.put(enactionDescElm.getAttributeValue("KEY"), enactionDesc);
		}
	}

	private void loadActivity(Element processComponents){

		ElementFilter normalFilter = new ElementFilter(Normal.class.getSimpleName());
		ElementFilter decomposedFilter = new ElementFilter(Decomposed.class.getSimpleName());
		ElementFilter automaticFilter = new ElementFilter(Automatic.class.getSimpleName());

		AbstractFilter activityFilter = ((AbstractFilter) normalFilter.or(decomposedFilter));
		activityFilter = ((AbstractFilter) activityFilter.or(automaticFilter));

		Iterator<Element> iterActivities = processComponents.getDescendants(activityFilter);
		while (iterActivities.hasNext()) {
			Element actElm = (Element) iterActivities.next();

			Activity activity = (Activity) this.buildProcObject(actElm);
			if(activity == null) continue;
			Element actTypeElm = actElm.getChild("TheActivityType");
			if(actTypeElm != null){
				String typeKey = actTypeElm.getAttributeValue("REF");
				ActivityType actType = (ActivityType) this.organizational.get(typeKey);
				activity.insertIntoTheActivityType(actType);
			}

			Element versionElm = actElm.getChild("IsVersion");
			if(versionElm != null){
				String versionKey = versionElm.getAttributeValue("REF");
				Activity actVersion = (Activity) this.organizational.get(versionKey);
				activity.insertIntoIsVersion(actVersion);
			}
		}
	}

	private void loadRequiredPeople(Element processComponents, Element organizational){

		ElementFilter reqAgentFilter = new ElementFilter(ReqAgent.class.getSimpleName());
		ElementFilter reqWorkGroupFilter = new ElementFilter(ReqWorkGroup.class.getSimpleName());

		AbstractFilter requiredPeopleFilter = ((AbstractFilter) reqAgentFilter.or(reqWorkGroupFilter));

		Iterator<Element> iter = processComponents.getDescendants(requiredPeopleFilter);
		while (iter.hasNext()) {
			Element requiredPeopleElm = (Element) iter.next();

			String tagName = requiredPeopleElm.getQualifiedName();
			if(tagName.equals(ReqAgent.class.getSimpleName())){
				ReqAgent reqAgent = new ReqAgent();

				Element roleElm = requiredPeopleElm.getChild("TheRole");
				if(roleElm == null)	continue; // Inconsistency handle

				String roleKey = roleElm.getAttributeValue("REF");
				reqAgent.insertIntoTheRole((Role) this.organizational.get(roleKey));

				String normalKey = requiredPeopleElm.getChild("TheNormal").getAttributeValue("REF");
				reqAgent.insertIntoTheNormal((Normal) this.processComponents.get(normalKey));

				Element agentElm = requiredPeopleElm.getChild("TheAgent");
				if(agentElm != null){
					String agentKey = agentElm.getAttributeValue("REF");
					reqAgent.insertIntoTheAgent((Agent) this.organizational.get(agentKey));
				}

				this.persistObject(reqAgent, null);

				this.processComponents.put(requiredPeopleElm.getAttributeValue("KEY"), reqAgent);
			}
			else{ //tagName.equals(ReqWorkGroup.class.getSimpleName()

				ReqWorkGroup reqWorkGroup = new ReqWorkGroup();

				Element WorkGroupTypeElm = requiredPeopleElm.getChild("TheWorkGroupType");
				if(WorkGroupTypeElm == null) continue; // Inconsistency handle
				String WorkGroupTypeKey = WorkGroupTypeElm.getAttributeValue("REF");
				reqWorkGroup.insertIntoTheWorkGroupType((WorkGroupType) this.organizational.get(WorkGroupTypeKey));

				String normalKey = requiredPeopleElm.getChild("TheNormal").getAttributeValue("REF");
				reqWorkGroup.insertIntoTheNormal((Normal) this.processComponents.get(normalKey));

				Element WorkGroupElm = requiredPeopleElm.getChild("TheWorkGroup");
				if(WorkGroupElm != null){
					String WorkGroupKey = WorkGroupElm.getAttributeValue("REF");
					reqWorkGroup.insertIntoTheWorkGroup((WorkGroup) this.organizational.get(WorkGroupKey));
				}

				this.persistObject(reqWorkGroup, null);

				this.processComponents.put(requiredPeopleElm.getAttributeValue("KEY"), reqWorkGroup);
			}
		}
	}

	private void loadRequiredResource(Element processComponents, Element organizational) {

		List<Element> requiredResources = processComponents.getChildren(RequiredResource.class.getSimpleName());
		Iterator<Element> iter = requiredResources.iterator();
		while (iter.hasNext()) {
			Element requiredResourceElm = (Element) iter.next();

			RequiredResource reqResource = new RequiredResource();

			Element resType = requiredResourceElm.getChild("TheResourceType");
			if(resType == null) continue; // Inconsistency handle

			String resourceTypeKey = resType.getAttributeValue("REF");
			reqResource.insertIntoTheResourceType((ResourceType) this.organizational.get(resourceTypeKey));

			String normalKey = requiredResourceElm.getChild("TheNormal").getAttributeValue("REF");
			reqResource.insertIntoTheNormal((Normal) this.processComponents.get(normalKey));

			Element resourceElm = requiredResourceElm.getChild("TheResource");
			if(resourceElm != null){
				String resourceKey = resourceElm.getAttributeValue("REF");
				reqResource.insertIntoTheResource((Resource) this.organizational.get(resourceKey));
			}

			this.persistObject(reqResource, null);

			this.processComponents.put(requiredResourceElm.getAttributeValue("KEY"), reqResource);
		}
	}

	private void loadInvolvedArtifacts(Element processComponents, Element organizational) {

		List<Element> involvedArtifacts = processComponents.getChildren(InvolvedArtifacts.class.getSimpleName());
		Iterator<Element> iter = involvedArtifacts.iterator();
		while (iter.hasNext()) {
			Element invArtElm = (Element) iter.next();

			InvolvedArtifacts invArt = new InvolvedArtifacts();

			Element artTypeElm = invArtElm.getChild("TheArtifactType");
			if(artTypeElm == null) continue; // Inconsistency handle

			String artifactTypeKey = artTypeElm.getAttributeValue("REF");
			invArt.insertIntoTheArtifactType((ArtifactType) this.organizational.get(artifactTypeKey));

			Element in = invArtElm.getChild("InInvolvedArtifacts");
			if(in != null){
				String normalInKey = in.getAttributeValue("REF");
				invArt.insertIntoInInvolvedArtifacts((Normal) this.processComponents.get(normalInKey));
			}

			Element out = invArtElm.getChild("OutInvolvedArtifacts");
			if(out != null){
				String normalOutKey = out.getAttributeValue("REF");
				invArt.insertIntoOutInvolvedArtifacts((Normal) this.processComponents.get(normalOutKey));
			}

			Element artifactElm = invArtElm.getChild("TheArtifact");
			if(artifactElm != null){
				String artifactKey = artifactElm.getAttributeValue("REF");
				invArt.insertIntoTheArtifact((Artifact) this.organizational.get(artifactKey));
			}

			this.persistObject(invArt, null);

			this.processComponents.put(invArtElm.getAttributeValue("KEY"), invArt);
		}
	}

	private void loadConnection(Element processComponents) {

		ElementFilter artifactConFilter = new ElementFilter(ArtifactCon.class.getSimpleName());
		ElementFilter sequenceFilter = new ElementFilter(Sequence.class.getSimpleName());
		ElementFilter feedbackFilter = new ElementFilter(Feedback.class.getSimpleName());
		ElementFilter branchANDConFilter = new ElementFilter(BranchANDCon.class.getSimpleName());
		ElementFilter branchConCondFilter = new ElementFilter(BranchConCond.class.getSimpleName());
		ElementFilter joinConFilter = new ElementFilter(JoinCon.class.getSimpleName());

		AbstractFilter connectionFilter = ((AbstractFilter) artifactConFilter.or(sequenceFilter));
		connectionFilter = ((AbstractFilter) connectionFilter.or(feedbackFilter));
		connectionFilter = ((AbstractFilter) connectionFilter.or(branchANDConFilter));
		connectionFilter = ((AbstractFilter) connectionFilter.or(branchConCondFilter));
		connectionFilter = ((AbstractFilter) connectionFilter.or(joinConFilter));

		Iterator<Element> iter = processComponents.getDescendants(connectionFilter);
		while (iter.hasNext()) {
			Element connectionElm = (Element) iter.next();
			String key = connectionElm.getAttributeValue("KEY");
			Connection connection = (Connection) this.buildProcObject(connectionElm);
			if(connection == null) continue;

			if(connection instanceof ArtifactCon){
				ArtifactCon artifactCon = (ArtifactCon) connection;

				Element fromActivities = connectionElm.getChild("FromActivity");
				if(fromActivities != null){
					List<Element> itens = fromActivities.getChildren("Item");
					Iterator<Element> iterItens = itens.iterator();
					while (iterItens.hasNext()) {
						Element item = (Element) iterItens.next();
						String itemRef = item.getValue();
						artifactCon.insertIntoFromActivity((Activity) this.processComponents.get(itemRef));
					}
				}

				Element toActivities = connectionElm.getChild("ToActivity");
				if(toActivities != null){
					List<Element> itens = toActivities.getChildren("Item");
					Iterator<Element> iterItens = itens.iterator();
					while (iterItens.hasNext()) {
						Element item = (Element) iterItens.next();
						String itemRef = item.getValue();
						artifactCon.insertIntoToActivity((Activity) this.processComponents.get(itemRef));
					}
				}

				Element artTypeElm = connectionElm.getChild("TheArtifactType");
				artifactCon.insertIntoTheArtifactType((ArtifactType) this.organizational.get(artTypeElm.getAttributeValue("REF")));

				Element artElm = connectionElm.getChild("TheArtifact");
				if(artElm != null){
					artifactCon.insertIntoTheArtifact((Artifact) this.organizational.get(artElm.getAttributeValue("REF")));
				}

				artifactCon = (ArtifactCon) this.persistObject(artifactCon, null);
				this.processComponents.put(key, artifactCon);
			}
			else if(connection instanceof Sequence){
				Sequence sequence = (Sequence) connection;

				Element fromActivityElm = connectionElm.getChild("FromActivity");
				if(fromActivityElm != null){
					String fromRef = fromActivityElm.getAttributeValue("REF");
					sequence.insertIntoFromActivity((Activity) this.processComponents.get(fromRef));
				}

				Element toActivityElm = connectionElm.getChild("ToActivity");
				if(toActivityElm != null){
					String toRef = toActivityElm.getAttributeValue("REF");
					sequence.insertIntoToActivity((Activity) this.processComponents.get(toRef));
				}

				sequence = (Sequence) this.persistObject(sequence, null);
				this.processComponents.put(key, sequence);
			}
			else if(connection instanceof Feedback){
				Feedback feedback = (Feedback) connection;

				Element fromActivityElm = connectionElm.getChild("FromActivity");
				if(fromActivityElm != null){
					String fromRef = fromActivityElm.getAttributeValue("REF");
					feedback.insertIntoFromActivity((Activity) this.processComponents.get(fromRef));
				}

				Element toActivityElm = connectionElm.getChild("ToActivity");
				if(toActivityElm != null){
					String toRef = toActivityElm.getAttributeValue("REF");
					feedback.insertIntoToActivity((Activity) this.processComponents.get(toRef));
				}

				Element conditionElm = connectionElm.getChild("TheCondition");
				this.processComponents.put(conditionElm.getAttributeValue("REF"), feedback.getTheCondition());

				feedback = (Feedback) this.persistObject(feedback, null);
				this.processComponents.put(key, feedback);
			}
			else if(connection instanceof BranchANDCon){
				BranchANDCon branchAND = (BranchANDCon) connection;

				Element fromActivityElm = connectionElm.getChild("FromActivity");
				if(fromActivityElm != null){
					String fromRef = fromActivityElm.getAttributeValue("REF");
					branchANDCon.insertIntoFromActivity((Activity) this.processComponents.get(fromRef));
				}

				Element toActivities = connectionElm.getChild("ToActivity");
				if(toActivities != null){
					List<Element> itens = toActivities.getChildren("Item");
					Iterator<Element> iterItens = itens.iterator();
					while (iterItens.hasNext()) {
						Element item = (Element) iterItens.next();
						String itemRef = item.getValue();
						branchANDCon.insertIntoToActivity((Activity) this.processComponents.get(itemRef));
					}
				}

				// Not needed! It'll be settled by the artifact connection
				// branchANDCon.insertIntoFromArtifactCon(null);

				Element fromMultipleCon = connectionElm.getChild("FromMultipleConnection");
				if(fromMultipleCon != null){
					String fromRef = fromMultipleCon.getAttributeValue("REF");
					branchANDCon.insertIntoFromActivity((Activity) this.processComponents.get(fromRef));
				}

				Element toMultipleCons = connectionElm.getChild("ToMultipleCon");
				if(toMultipleCons != null){
					List<Element> itens = toMultipleCons.getChildren("Item");
					Iterator<Element> iterItens = itens.iterator();
					while (iterItens.hasNext()) {
						Element item = (Element) iterItens.next();
						String itemRef = item.getValue();
						branchANDCon.insertIntoToMultipleCon((MultipleCon) this.processComponents.get(itemRef));
					}
				}

				branchANDCon = (BranchAND) this.persistObject(branchANDCon, null);
				this.processComponents.put(key, branchANDCon);
			}
			else if(connection instanceof BranchConCond){
				BranchConCond branchConCond = (BranchConCond) connection;

				Element fromActivityElm = connectionElm.getChild("FromActivity");
				if(fromActivityElm != null){
					String fromRef = fromActivityElm.getAttributeValue("REF");
					branchConCond.insertIntoFromActivity((Activity) this.processComponents.get(fromRef));
				}

				// Not needed! It'll be settled by the associative class
				//branchConCond.insertIntoTheBranchConCondToActivity(null);

				// Not needed! It'll be settled by the artifact connection
				// branchConCond.insertIntoFromArtifactCon(null);

				Element fromMultipleCon = connectionElm.getChild("FromMultipleConnection");
				if(fromMultipleCon != null){
					String fromRef = fromMultipleCon.getAttributeValue("REF");
					branchConCond.insertIntoFromActivity((Activity) this.processComponents.get(fromRef));
				}

				// Not needed! It'll be settled by the associative class
				// branchConCond.insertIntoTheBranchConCondToMultipleCon(null);

				branchConCond = (BranchConCond) this.persistObject(branchConCond, null);
				this.processComponents.put(key, branchConCond);
			}
			else if(connection instanceof JoinCon){
				JoinCon join = (JoinCon) connection;

				Element fromActivities = connectionElm.getChild("FromActivity");
				if(fromActivities != null){
					List<Element> itens = fromActivities.getChildren("Item");
					Iterator<Element> iterItens = itens.iterator();
					while (iterItens.hasNext()) {
						Element item = (Element) iterItens.next();
						String itemRef = item.getValue();
						joinCon.insertIntoFromActivity((Activity) this.processComponents.get(itemRef));
					}
				}

				Element toActivityElm = connectionElm.getChild("ToActivity");
				if(toActivityElm != null){
					String toRef = toActivityElm.getAttributeValue("REF");
					joinCon.insertIntoToActivity((Activity) this.processComponents.get(toRef));
				}

				// Not needed! It'll be settled by the artifact connection
				// joinCon.insertIntoFromArtifactCon(null);

				Element fromMultipleCons = connectionElm.getChild("FromMultipleCon");
				if(fromMultipleCons != null){
					List<Element> itens = fromMultipleCons.getChildren("Item");
					Iterator<Element> iterItens = itens.iterator();
					while (iterItens.hasNext()) {
						Element item = (Element) iterItens.next();
						String itemRef = item.getValue();
						joinCon.insertIntoFromMultipleCon((MultipleCon) this.processComponents.get(itemRef));
					}
				}

				Element toMultipleConElm = connectionElm.getChild("ToMultipleCon");
				if(toMultipleConElm != null){
					String toRef = toMultipleConElm.getAttributeValue("REF");
					joinCon.insertIntoToMultipleCon((MultipleCon) this.processComponents.get(toRef));
				}

				joinCon = (Join) this.persistObject(joinCon, null);
				this.processComponents.put(key, joinCon);
			}
		}

		List<Element> artCons = processComponents.getChildren(ArtifactCon.class.getSimpleName());
		Iterator<Element> iterArtCons= artCons.iterator();
		while (iterArtCons.hasNext()) {
			Element artConElm = (Element) iterArtCons.next();
			ArtifactCon artifactCon = (ArtifactCon) this.processComponents.get(artConElm.getAttributeValue("KEY"));

			Element toMultipleCon = artConElm.getChild("ToMultipleCon");
			if(toMultipleCon != null){
				List<Element> itens = toMultipleCon.getChildren("Item");
				Iterator<Element> iterItens = itens.iterator();
				while (iterItens.hasNext()) {
					Element item = (Element) iterItens.next();
					String itemRef = item.getValue();
					artifactCon.insertIntoToMultipleCon((MultipleCon) this.processComponents.get(itemRef));
				}
			}
		}
	}

	private void loadDependency(Element processComponents){
		List<Element> dependencies = processComponents.getChildren(Dependency.class.getSimpleName());
		Iterator<Element> iter = dependencies.iterator();
		while (iter.hasNext()) {
			Element depElm = (Element) iter.next();

			Element seqElm = depElm.getChild("TheSequence");
			if(seqElm != null){
				String seqKey = seqElm.getAttributeValue("REF");
				Sequence sequence = (Sequence) this.processComponents.get(seqKey);
				sequence.getTheDependency().setKindDep(depElm.getChildText("KindDep"));
			}
			else{
				Element multipleConElm = depElm.getChild("TheMultipleCon");
				if(multipleConElm != null){
					String multipleConKey = multipleConElm.getAttributeValue("REF");
					MultipleCon multipleCon = (MultipleCon) this.processComponents.get(multipleConKey);
					multipleCon.getTheDependency().setKindDep(depElm.getChildText("KindDep"));
				}
			}
		}
	}

	private void loadProcessAgenda(Element processComponents, Element organizational) {

		List<Element> processAgendas = processComponents.getChildren(ProcessAgenda.class.getSimpleName());
		Iterator<Element> iterProcessAgendas = processAgendas.iterator();
		while (iterProcessAgendas.hasNext()) {
			Element processAgendaElm = (Element) iterProcessAgendas.next();
			String processAgendaKey = processAgendaElm.getAttributeValue("KEY");

			String taskAgendaRef = processAgendaElm.getChild("TheTaskAgenda").getAttributeValue("REF");
			TaskAgenda taskAgenda = (TaskAgenda) this.organizational.get(taskAgendaRef);

			String processRef = processAgendaElm.getChild("TheProcess").getAttributeValue("REF");
			Process process = (Process) this.processComponents.get(processRef);

			ProcessAgenda processAgenda = new ProcessAgenda();
			taskAgenda.insertIntoTheProcessAgenda(processAgenda);

			process.insertIntoTheProcessAgenda(processAgenda);

			this.persistObject(processAgenda, null);
			this.processComponents.put(processAgendaKey, processAgenda);
		}
	}

	private void loadTask(Element processComponents) {
		List<Element> tasks = processComponents.getChildren(Task.class.getSimpleName());
		Iterator<Element> iter = tasks.iterator();
		while (iter.hasNext()) {
			Element taskElm = (Element) iter.next();
			String key = taskElm.getAttributeValue("KEY");

			Task task = new Task();
			task.setBeginDate((Date) this.buildAttribute(Date.class, taskElm.getChildText("BeginDate")));
			task.setEndDate((Date) this.buildAttribute(Date.class, taskElm.getChildText("EndDate")));
			task.setDateDelegatedFrom((Date) this.buildAttribute(Date.class, taskElm.getChildText("DateDelegatedFrom")));
			task.setDateDelegatedFrom((Date) this.buildAttribute(Date.class, taskElm.getChildText("DateDelegatedTo")));
			task.setLocalState(taskElm.getChildText("LocalState"));
			task.setWorkingHours((Float) this.buildAttribute(Float.class, taskElm.getChildText("WorkingHours")));

			Element delegatedToElm = taskElm.getChild("DelegatedTo");
			if(delegatedToElm != null){
				String delegatedToKey = delegatedToElm.getAttributeValue("REF");
				task.insertIntoDelegatedTo((Agent) this.organizational.get(delegatedToKey));
			}

			Element delegatedFromElm = taskElm.getChild("DelegatedFrom");
			if(delegatedFromElm != null){
				String delegatedFromKey = delegatedFromElm.getAttributeValue("REF");
				task.insertIntoDelegatedFrom((Agent) this.organizational.get(delegatedFromKey));
			}

			Element normElm = taskElm.getChild("TheNormal");
			if(normElm != null){
				String normKey = normElm.getAttributeValue("REF");
				task.insertIntoTheNormal((Normal) this.processComponents.get(normKey));
			}

			Element agendaElm = taskElm.getChild("TheProcessAgenda");
			if(agendaElm != null){
				String agendaKey = agendaElm.getAttributeValue("REF");
				task.insertIntoTheProcessAgenda((ProcessAgenda) this.processComponents.get(agendaKey));
			}

			this.persistObject(task, null);

			this.processComponents.put(key, task);
		}
	}

	private void loadEvent(Element processComponents) {

		// Estimation filters
		ElementFilter agendaEvtFilter = new ElementFilter(AgendaEvent.class.getSimpleName());
		ElementFilter connectionEvtFilter = new ElementFilter(ConnectionEvent.class.getSimpleName());
		ElementFilter globalActivityEvtFilter = new ElementFilter(GlobalActivityEvent.class.getSimpleName());
		ElementFilter modelingActivityEvtFilter = new ElementFilter(ModelingActivityEvent.class.getSimpleName());
		ElementFilter processEvtFilter = new ElementFilter(ProcessEvent.class.getSimpleName());
		ElementFilter processModelEvtFilter = new ElementFilter(ProcessModelEvent.class.getSimpleName());
		ElementFilter resourceEvtFilter = new ElementFilter(ResourceEvent.class.getSimpleName());

		AbstractFilter eventFilter = ((AbstractFilter) agendaEvtFilter.or(connectionEvtFilter));
		eventFilter = ((AbstractFilter) eventFilter.or(globalActivityEvtFilter));
		eventFilter = ((AbstractFilter) eventFilter.or(modelingActivityEvtFilter));
		eventFilter = ((AbstractFilter) eventFilter.or(processEvtFilter));
		eventFilter = ((AbstractFilter) eventFilter.or(processModelEvtFilter));
		eventFilter = ((AbstractFilter) eventFilter.or(resourceEvtFilter));

		Iterator<Element> iter = processComponents.getDescendants(eventFilter);
		while (iter.hasNext()) {
			Element eventElm = (Element) iter.next();
			String key = eventElm.getAttributeValue("KEY");

			if(eventElm.getQualifiedName().equals(AgendaEvent.class.getSimpleName())){
				AgendaEvent event = new AgendaEvent();

				event.setIsCreatedByApsee((Boolean) this.buildAttribute(Boolean.class, eventElm.getChildText("IsCreatedByApsee")));
				event.setWhen((Date) this.buildAttribute(Date.class, eventElm.getChildText("When")));
				event.setWhy(eventElm.getChildText("Why"));

				String typeKey = eventElm.getChild("TheEventType").getAttributeValue("REF");
				event.insertIntoTheEventType((EventType) this.organizational.get(typeKey));

				String logKey = eventElm.getChild("TheLog").getAttributeValue("REF");
				event.insertIntoTheLog((Log) this.processComponents.get(logKey));

				String taskKey = eventElm.getChild("TheTask").getAttributeValue("REF");
				event.insertIntoTheTask((Task) this.processComponents.get(taskKey));

				this.persistObject(event, null);
				this.processComponents.put(key, event);
			}
			else if(eventElm.getQualifiedName().equals(ConnectionEvent.class.getSimpleName())){
				ConnectionEvent event = new ConnectionEvent();

				event.setIsCreatedByApsee((Boolean) this.buildAttribute(Boolean.class, eventElm.getChildText("IsCreatedByApsee")));
				event.setWhen((Date) this.buildAttribute(Date.class, eventElm.getChildText("When")));
				event.setWhy(eventElm.getChildText("Why"));

				String typeKey = eventElm.getChild("TheEventType").getAttributeValue("REF");
				event.insertIntoTheEventType((EventType) this.organizational.get(typeKey));

				String logKey = eventElm.getChild("TheLog").getAttributeValue("REF");
				event.insertIntoTheLog((Log) this.processComponents.get(logKey));

				this.persistObject(event, null);
				this.processComponents.put(key, event);
			}
			else if(eventElm.getQualifiedName().equals(GlobalActivityEvent.class.getSimpleName())){
				GlobalActivityEvent event = new GlobalActivityEvent();

				event.setIsCreatedByApsee((Boolean) this.buildAttribute(Boolean.class, eventElm.getChildText("IsCreatedByApsee")));
				event.setWhen((Date) this.buildAttribute(Date.class, eventElm.getChildText("When")));
				event.setWhy(eventElm.getChildText("Why"));

				String typeKey = eventElm.getChild("TheEventType").getAttributeValue("REF");
				event.insertIntoTheEventType((EventType) this.organizational.get(typeKey));

				String logKey = eventElm.getChild("TheLog").getAttributeValue("REF");
				event.insertIntoTheLog((Log) this.processComponents.get(logKey));

				String actKey = eventElm.getChild("ThePlain").getAttributeValue("REF");
				event.insertIntoTheActivity((Plain) this.processComponents.get(actKey));

				this.persistObject(event, null);
				this.processComponents.put(key, event);
			}
			else if(eventElm.getQualifiedName().equals(ModelingActivityEvent.class.getSimpleName())){
				ModelingActivityEvent event = new ModelingActivityEvent();

				event.setIsCreatedByApsee((Boolean) this.buildAttribute(Boolean.class, eventElm.getChildText("IsCreatedByApsee")));
				event.setWhen((Date) this.buildAttribute(Date.class, eventElm.getChildText("When")));
				event.setWhy(eventElm.getChildText("Why"));

				String typeKey = eventElm.getChild("TheEventType").getAttributeValue("REF");
				event.insertIntoTheEventType((EventType) this.organizational.get(typeKey));

				String logKey = eventElm.getChild("TheLog").getAttributeValue("REF");
				event.insertIntoTheLog((Log) this.processComponents.get(logKey));

				String actKey = eventElm.getChild("TheActivity").getAttributeValue("REF");
				event.insertIntoTheActivity((Activity) this.processComponents.get(actKey));

				Element agentElm = eventElm.getChild("TheAgent");
				if(agentElm != null){
					String agentKey = agentElm.getAttributeValue("REF");
					event.insertIntoTheAgent((Agent) this.organizational.get(agentKey));
				}

				this.persistObject(event, null);
				this.processComponents.put(key, event);
			}
			else if(eventElm.getQualifiedName().equals(ProcessEvent.class.getSimpleName())){
				ProcessEvent event = new ProcessEvent();

				event.setIsCreatedByApsee((Boolean) this.buildAttribute(Boolean.class, eventElm.getChildText("IsCreatedByApsee")));
				event.setWhen((Date) this.buildAttribute(Date.class, eventElm.getChildText("When")));
				event.setWhy(eventElm.getChildText("Why"));

				String typeKey = eventElm.getChild("TheEventType").getAttributeValue("REF");
				event.insertIntoTheEventType((EventType) this.organizational.get(typeKey));

				String logKey = eventElm.getChild("TheLog").getAttributeValue("REF");
				event.insertIntoTheLog((Log) this.processComponents.get(logKey));

				String processKey = eventElm.getChild("TheProcess").getAttributeValue("REF");
				event.insertIntoTheProcess((Process) this.processComponents.get(processKey));

				this.persistObject(event, null);
				this.processComponents.put(key, event);
			}
			else if(eventElm.getQualifiedName().equals(ProcessModelEvent.class.getSimpleName())){
				ProcessModelEvent event = new ProcessModelEvent();

				event.setIsCreatedByApsee((Boolean) this.buildAttribute(Boolean.class, eventElm.getChildText("IsCreatedByApsee")));
				event.setWhen((Date) this.buildAttribute(Date.class, eventElm.getChildText("When")));
				event.setWhy(eventElm.getChildText("Why"));

				String typeKey = eventElm.getChild("TheEventType").getAttributeValue("REF");
				event.insertIntoTheEventType((EventType) this.organizational.get(typeKey));

				String logKey = eventElm.getChild("TheLog").getAttributeValue("REF");
				event.insertIntoTheLog((Log) this.processComponents.get(logKey));

				String procModelKey = eventElm.getChild("TheProcessModel").getAttributeValue("REF");
				event.insertIntoTheProcessModel((ProcessModel) this.processComponents.get(procModelKey));

				this.persistObject(event, null);
				this.processComponents.put(key, event);
			}
			else if(eventElm.getQualifiedName().equals(ResourceEvent.class.getSimpleName())){
				ResourceEvent event = new ResourceEvent();

				event.setIsCreatedByApsee((Boolean) this.buildAttribute(Boolean.class, eventElm.getChildText("IsCreatedByApsee")));
				event.setWhen((Date) this.buildAttribute(Date.class, eventElm.getChildText("When")));
				event.setWhy(eventElm.getChildText("Why"));

				String typeKey = eventElm.getChild("TheEventType").getAttributeValue("REF");
				event.insertIntoTheEventType((EventType) this.organizational.get(typeKey));

				String logKey = eventElm.getChild("TheLog").getAttributeValue("REF");
				event.insertIntoTheLog((Log) this.processComponents.get(logKey));

				Element normElm = eventElm.getChild("TheNormal");
				if(normElm != null){
					String actKey = normElm.getAttributeValue("REF");
					event.insertIntoTheNormal((Normal) this.processComponents.get(actKey));
				}

				String resourceKey = eventElm.getChild("TheResource").getAttributeValue("REF");
				event.insertIntoTheResource((Resource) this.processComponents.get(resourceKey));

				this.persistObject(event, null);
				this.processComponents.put(key, event);
			}
		}
	}

	private void loadCatalogEvent(Element processComponents) {

		List<Element> catalogs = processComponents.getChildren(CatalogEvents.class.getSimpleName());
		Iterator<Element> iter = catalogs.iterator();
		while (iter.hasNext()) {
			Element catalogElm = (Element) iter.next();
			CatalogEvents catalog = null;

			Element evtElm = catalogElm.getChild("TheGlobalActivityEvent");
			if(evtElm != null){
				String key = evtElm.getAttributeValue("REF");
				GlobalActivityEvent event = (GlobalActivityEvent) this.processComponents.get(key);
				catalog = event.getTheCatalogEvents();
				catalog.setDescription(catalogElm.getChildText("Description"));

				this.persistObject(catalog, null);
				continue;
			}

			evtElm = catalogElm.getChild("TheProcessModelEvent");
			if(evtElm != null){
				String key = evtElm.getAttributeValue("REF");
				ProcessModelEvent event = (ProcessModelEvent) this.processComponents.get(key);
				catalog = event.getTheCatalogEvents();
				catalog.setDescription(catalogElm.getChildText("Description"));

				this.persistObject(catalog, null);
				continue;
			}

			evtElm = catalogElm.getChild("TheResourceEvent");
			if(evtElm != null){
				String key = evtElm.getAttributeValue("REF");
				ResourceEvent event = (ResourceEvent) this.processComponents.get(key);
				catalog = event.getTheCatalogEvents();
				catalog.setDescription(catalogElm.getChildText("Description"));

				this.persistObject(catalog, null);
				continue;
			}

			evtElm = catalogElm.getChild("TheAgendaEvent");
			if(evtElm != null){
				String key = evtElm.getAttributeValue("REF");
				AgendaEvent event = (AgendaEvent) this.processComponents.get(key);
				catalog = event.getTheCatalogEvents();
				catalog.setDescription(catalogElm.getChildText("Description"));

				this.persistObject(catalog, null);
				continue;
			}

			evtElm = catalogElm.getChild("TheProcessEvent");
			if(evtElm != null){
				String key = evtElm.getAttributeValue("REF");
				ProcessEvent event = (ProcessEvent) this.processComponents.get(key);
				catalog = event.getTheCatalogEvents();
				catalog.setDescription(catalogElm.getChildText("Description"));

				this.persistObject(catalog, null);
				continue;
			}

			evtElm = catalogElm.getChild("TheModelingActivityEvent");
			if(evtElm != null){
				String key = evtElm.getAttributeValue("REF");
				ModelingActivityEvent event = (ModelingActivityEvent) this.processComponents.get(key);
				catalog = event.getTheCatalogEvents();
				catalog.setDescription(catalogElm.getChildText("Description"));

				this.persistObject(catalog, null);
				continue;
			}

			evtElm = catalogElm.getChild("TheConnectionEvent");
			if(evtElm != null){
				String key = evtElm.getAttributeValue("REF");
				ConnectionEvent event = (ConnectionEvent) this.processComponents.get(key);
				catalog = event.getTheCatalogEvents();
				catalog.setDescription(catalogElm.getChildText("Description"));

				this.persistObject(catalog, null);
				continue;
			}
		}
	}

	private void loadMetricAndEstimation(Element processComponents,	Element organizational) {

		// Estimation filters
		ElementFilter activityEstFilter = new ElementFilter(ActivityEstimation.class.getSimpleName());
		ElementFilter agentEstFilter = new ElementFilter(AgentEstimation.class.getSimpleName());
		ElementFilter artifactEstFilter = new ElementFilter(ArtifactEstimation.class.getSimpleName());
		ElementFilter WorkGroupEstFilter = new ElementFilter(WorkGroupEstimation.class.getSimpleName());
		ElementFilter organizationEstFilter = new ElementFilter(OrganizationEstimation.class.getSimpleName());
		ElementFilter processEstFilter = new ElementFilter(ProcessEstimation.class.getSimpleName());
		ElementFilter resourceEstFilter = new ElementFilter(ResourceEstimation.class.getSimpleName());

		AbstractFilter estimationFilter = ((AbstractFilter) activityEstFilter.or(agentEstFilter));
		estimationFilter = ((AbstractFilter) estimationFilter.or(artifactEstFilter));
		estimationFilter = ((AbstractFilter) estimationFilter.or(WorkGroupEstFilter));
		estimationFilter = ((AbstractFilter) estimationFilter.or(organizationEstFilter));
		estimationFilter = ((AbstractFilter) estimationFilter.or(processEstFilter));
		estimationFilter = ((AbstractFilter) estimationFilter.or(resourceEstFilter));

		// Metric filters
		ElementFilter activityMetFilter = new ElementFilter(ActivityMetric.class.getSimpleName());
		ElementFilter agentMetFilter = new ElementFilter(AgentMetric.class.getSimpleName());
		ElementFilter artifactMetFilter = new ElementFilter(ArtifactMetric.class.getSimpleName());
		ElementFilter WorkGroupMetFilter = new ElementFilter(WorkGroupMetric.class.getSimpleName());
		ElementFilter organizationMetFilter = new ElementFilter(OrganizationMetric.class.getSimpleName());
		ElementFilter processMetFilter = new ElementFilter(ProcessMetric.class.getSimpleName());
		ElementFilter resourceMetFilter = new ElementFilter(ResourceMetric.class.getSimpleName());

		AbstractFilter metricFilter = ((AbstractFilter) activityMetFilter.or(agentMetFilter));
		metricFilter = ((AbstractFilter) metricFilter.or(artifactMetFilter));
		metricFilter = ((AbstractFilter) metricFilter.or(WorkGroupMetFilter));
		metricFilter = ((AbstractFilter) metricFilter.or(organizationMetFilter));
		metricFilter = ((AbstractFilter) metricFilter.or(processMetFilter));
		metricFilter = ((AbstractFilter) metricFilter.or(resourceMetFilter));

		Iterator<Element> iterEst = processComponents.getDescendants(estimationFilter);
		while (iterEst.hasNext()) {
			Element estElm = (Element) iterEst.next();

			String key = estElm.getAttributeValue("KEY");
			if(estElm.getQualifiedName().equals(ActivityEstimation.class.getSimpleName())){
				ActivityEstimation est = new ActivityEstimation();
				est.setValue((Float) this.buildAttribute(Float.class, estElm.getChildText("Value")));
				est.setUnit((String) this.buildAttribute(String.class, estElm.getChildText("Unit")));

				Element actElm = estElm.getChild("Activity");
				if(actElm == null) continue; // Inconsistency handle
				String actKey = actElm.getAttributeValue("REF");
				est.insertIntoActivity((Activity) this.processComponents.get(actKey));

				String metDefKey = estElm.getChild("MetricDefinition").getAttributeValue("REF");
				est.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(est, null);

				this.processComponents.put(key, est);
			}
			else if(estElm.getQualifiedName().equals(AgentEstimation.class.getSimpleName())){
				AgentEstimation est = new AgentEstimation();
				est.setValue((Float) this.buildAttribute(Float.class, estElm.getChildText("Value")));
				est.setUnit((String) this.buildAttribute(String.class, estElm.getChildText("Unit")));

				String agKey = estElm.getChild("Agent").getAttributeValue("REF");
				est.insertIntoAgent((Agent) this.organizational.get(agKey));

				String metDefKey = estElm.getChild("MetricDefinition").getAttributeValue("REF");
				est.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(est, null);

				this.processComponents.put(key, est);
			}
			else if(estElm.getQualifiedName().equals(ArtifactEstimation.class.getSimpleName())){
				ArtifactEstimation est = new ArtifactEstimation();
				est.setValue((Float) this.buildAttribute(Float.class, estElm.getChildText("Value")));
				est.setUnit((String) this.buildAttribute(String.class, estElm.getChildText("Unit")));

				String artKey = estElm.getChild("Artifact").getAttributeValue("REF");
				est.insertIntoArtifact((Artifact) this.organizational.get(artKey));

				String metDefKey = estElm.getChild("MetricDefinition").getAttributeValue("REF");
				est.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(est, null);

				this.processComponents.put(key, est);
			}
			else if(estElm.getQualifiedName().equals(WorkGroupEstimation.class.getSimpleName())){
				WorkGroupEstimation est = new WorkGroupEstimation();
				est.setValue((Float) this.buildAttribute(Float.class, estElm.getChildText("Value")));
				est.setUnit((String) this.buildAttribute(String.class, estElm.getChildText("Unit")));

				String WorkGroupKey = estElm.getChild("WorkGroup").getAttributeValue("REF");
				est.insertIntoWorkGroup((WorkGroup) this.organizational.get(WorkGroupKey));

				String metDefKey = estElm.getChild("MetricDefinition").getAttributeValue("REF");
				est.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(est, null);

				this.processComponents.put(key, est);
			}
			else if(estElm.getQualifiedName().equals(OrganizationEstimation.class.getSimpleName())){
				OrganizationEstimation est = new OrganizationEstimation();
				est.setValue((Float) this.buildAttribute(Float.class, estElm.getChildText("Value")));
				est.setUnit((String) this.buildAttribute(String.class, estElm.getChildText("Unit")));

				String orgKey = estElm.getChild("Organization").getAttributeValue("REF");
				Company company = new Company();
				est.insertIntoOrganization(company);
//				est.insertIntoOrganization((Organization) this.organizational.get(orgKey));

				String metDefKey = estElm.getChild("MetricDefinition").getAttributeValue("REF");
				est.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(est, null);

				this.processComponents.put(key, est);
			}
			else if(estElm.getQualifiedName().equals(ProcessEstimation.class.getSimpleName())){
				ProcessEstimation est = new ProcessEstimation();
				est.setValue((Float) this.buildAttribute(Float.class, estElm.getChildText("Value")));
				est.setUnit((String) this.buildAttribute(String.class, estElm.getChildText("Unit")));

				String procKey = estElm.getChild("Process").getAttributeValue("REF");
				est.insertIntoProcess((Process) this.organizational.get(procKey));

				String metDefKey = estElm.getChild("MetricDefinition").getAttributeValue("REF");
				est.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(est, null);

				this.processComponents.put(key, est);
			}
			else if(estElm.getQualifiedName().equals(ResourceEstimation.class.getSimpleName())){
				ResourceEstimation est = new ResourceEstimation();
				est.setValue((Float) this.buildAttribute(Float.class, estElm.getChildText("Value")));
				est.setUnit((String) this.buildAttribute(String.class, estElm.getChildText("Unit")));

				String resKey = estElm.getChild("Resource").getAttributeValue("REF");
				est.insertIntoResource((Resource) this.organizational.get(resKey));

				String metDefKey = estElm.getChild("MetricDefinition").getAttributeValue("REF");
				est.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(est, null);

				this.processComponents.put(key, est);
			}
		}

		// Create Metrics
		Iterator<Element> iterMet = processComponents.getDescendants(metricFilter);
		while (iterMet.hasNext()) {
			Element metElm = (Element) iterEst.next();

			String key = metElm.getAttributeValue("KEY");
			if(metElm.getQualifiedName().equals(ActivityMetric.class.getSimpleName())){
				ActivityMetric met = new ActivityMetric();
				met.setValue((Float) this.buildAttribute(Float.class, metElm.getChildText("Value")));
				met.setUnit((String) this.buildAttribute(String.class, metElm.getChildText("Unit")));
				met.setPeriodBegin((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodBegin")));
				met.setPeriodEnd((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodEnd")));

				String actKey = metElm.getChild("Activity").getAttributeValue("REF");
				met.insertIntoActivity((Activity) this.processComponents.get(actKey));

				String metDefKey = metElm.getChild("MetricDefinition").getAttributeValue("REF");
				met.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(met, null);

				this.processComponents.put(key, met);
			}
			else if(metElm.getQualifiedName().equals(AgentMetric.class.getSimpleName())){
				AgentMetric met = new AgentMetric();
				met.setValue((Float) this.buildAttribute(Float.class, metElm.getChildText("Value")));
				met.setUnit((String) this.buildAttribute(String.class, metElm.getChildText("Unit")));
				met.setPeriodBegin((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodBegin")));
				met.setPeriodEnd((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodEnd")));

				String agKey = metElm.getChild("Agent").getAttributeValue("REF");
				met.insertIntoAgent((Agent) this.organizational.get(agKey));

				String metDefKey = metElm.getChild("MetricDefinition").getAttributeValue("REF");
				met.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(met, null);

				this.processComponents.put(key, met);
			}
			else if(metElm.getQualifiedName().equals(ArtifactMetric.class.getSimpleName())){
				ArtifactMetric met = new ArtifactMetric();
				met.setValue((Float) this.buildAttribute(Float.class, metElm.getChildText("Value")));
				met.setUnit((String) this.buildAttribute(String.class, metElm.getChildText("Unit")));
				met.setPeriodBegin((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodBegin")));
				met.setPeriodEnd((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodEnd")));

				String artKey = metElm.getChild("Artifact").getAttributeValue("REF");
				met.insertIntoArtifact((Artifact) this.organizational.get(artKey));

				String metDefKey = metElm.getChild("MetricDefinition").getAttributeValue("REF");
				met.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(met, null);

				this.processComponents.put(key, met);
			}
			else if(metElm.getQualifiedName().equals(WorkGroupMetric.class.getSimpleName())){
				WorkGroupMetric met = new WorkGroupMetric();
				met.setValue((Float) this.buildAttribute(Float.class, metElm.getChildText("Value")));
				met.setUnit((String) this.buildAttribute(String.class, metElm.getChildText("Unit")));
				met.setPeriodBegin((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodBegin")));
				met.setPeriodEnd((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodEnd")));

				String WorkGroupKey = metElm.getChild("WorkGroup").getAttributeValue("REF");
				met.insertIntoWorkGroup((WorkGroup) this.organizational.get(WorkGroupKey));

				String metDefKey = metElm.getChild("MetricDefinition").getAttributeValue("REF");
				met.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(met, null);

				this.processComponents.put(key, met);
			}
			else if(metElm.getQualifiedName().equals(OrganizationMetric.class.getSimpleName())){
				OrganizationMetric met = new OrganizationMetric();
				met.setValue((Float) this.buildAttribute(Float.class, metElm.getChildText("Value")));
				met.setUnit((String) this.buildAttribute(String.class, metElm.getChildText("Unit")));
				met.setPeriodBegin((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodBegin")));
				met.setPeriodEnd((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodEnd")));

				String orgKey = metElm.getChild("Organization").getAttributeValue("REF");
				Company company = new Company();
				met.insertIntoOrganization(company);
//				met.insertIntoOrganization((Organization) this.organizational.get(orgKey));

				String metDefKey = metElm.getChild("MetricDefinition").getAttributeValue("REF");
				met.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(met, null);

				this.processComponents.put(key, met);
			}
			else if(metElm.getQualifiedName().equals(ProcessMetric.class.getSimpleName())){
				ProcessMetric met = new ProcessMetric();
				met.setValue((Float) this.buildAttribute(Float.class, metElm.getChildText("Value")));
				met.setUnit((String) this.buildAttribute(String.class, metElm.getChildText("Unit")));
				met.setPeriodBegin((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodBegin")));
				met.setPeriodEnd((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodEnd")));

				String procKey = metElm.getChild("Process").getAttributeValue("REF");
				met.insertIntoProcess((Process) this.organizational.get(procKey));

				String metDefKey = metElm.getChild("MetricDefinition").getAttributeValue("REF");
				met.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(met, null);

				this.processComponents.put(key, met);
			}
			else if(metElm.getQualifiedName().equals(ResourceMetric.class.getSimpleName())){
				ResourceMetric met = new ResourceMetric();
				met.setValue((Float) this.buildAttribute(Float.class, metElm.getChildText("Value")));
				met.setUnit((String) this.buildAttribute(String.class, metElm.getChildText("Unit")));
				met.setPeriodBegin((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodBegin")));
				met.setPeriodEnd((Date) this.buildAttribute(Date.class, metElm.getChildText("PeriodEnd")));

				String resKey = metElm.getChild("Resource").getAttributeValue("REF");
				met.insertIntoResource((Resource) this.organizational.get(resKey));

				String metDefKey = metElm.getChild("MetricDefinition").getAttributeValue("REF");
				met.insertIntoMetricDefinition((MetricDefinition) this.organizational.get(metDefKey));

				this.persistObject(met, null);

				this.processComponents.put(key, met);
			}
		}
	}

	private void loadPolCondition(Element processComponents){

		List<Element> conditions = processComponents.getChildren(PolCondition.class.getSimpleName());
		Iterator<Element> iter = conditions.iterator();
		while (iter.hasNext()) {
			Element condElm = (Element) iter.next();
			Element exprElm = condElm.getChild("ThePolExpression");

			PolCondition cond = (PolCondition) this.processComponents.get(condElm.getAttributeValue("KEY"));
			PolExpression expression = cond.getThePolExpression();

			this.processComponents.put(exprElm.getAttributeValue("KEY"), expression);
		}

		this.loadPolObject(processComponents);

		this.loadPolOperand(processComponents);

		this.loadPolRelation(processComponents);

		List<Element> expressions = processComponents.getChildren(PolExpression.class.getSimpleName());
		Iterator<Element> iterExpr = expressions.iterator();
		while (iterExpr.hasNext()) {
			Element exprElm = (Element) iter.next();
			PolExpression expression = (PolExpression) this.processComponents.get(exprElm.getAttributeValue("KEY"));
			expression.setNotExpression((Boolean) this.buildAttribute(Boolean.class, exprElm.getChildText("NotExpression")));

			Element operandElm = exprElm.getChild("ThePolOperand");
			if(operandElm != null){
				String operandKey = operandElm.getAttributeValue("REF");
				expression.insertIntoThePolOperand((PolOperand) this.processComponents.get(operandKey));
			}

			this.persistObject(expression, null);

			this.processComponents.put(exprElm.getAttributeValue("KEY"), expression);
		}

		this.loadPolConnection(processComponents);

		// Last one to be loaded
		this.loadPolRequiredOperand(processComponents);
	}

	private void loadPolOperand(Element processComponents){
		List<Element> operands = processComponents.getChildren(PolOperand.class.getSimpleName());
		Iterator<Element> iterOperands = operands.iterator();
		while (iterOperands.hasNext()) {
			Element operandElm = (Element) iterOperands.next();
			PolOperand polOperand = new PolOperand();
			Element objElm = operandElm.getChild("ThePolObject");
			if(objElm != null){
				polOperand.insertIntoThePolObject((PolObject) this.processComponents.get(objElm.getAttributeValue("REF")));
			}

			this.persistObject(polOperand, null);

			this.processComponents.put(operandElm.getAttributeValue("KEY"), polOperand);
		}

		this.loadPolAssociation(processComponents);

		this.loadPolOperator(processComponents);

		iterOperands = operands.iterator();
		while (iterOperands.hasNext()) {
			Element operandElm = (Element) iterOperands.next();
			PolOperand polOperand = (PolOperand) this.processComponents.get(operandElm.getAttributeValue("KEY"));

			Element assocElm = operandElm.getChild("TheAssociation");
			if(assocElm != null){
				polOperand.insertIntoTheAssociation((PolAssociation) this.processComponents.get(assocElm.getAttributeValue("REF")));
			}

			Element operElm = operandElm.getChild("ThePolOperator");
			if(operElm != null){
				polOperand.insertIntoThePolOperator((PolOperator) this.processComponents.get(operElm.getAttributeValue("REF")));
			}

			this.persistObject(polOperand, null);
		}
	}

	private void loadPolAssociation(Element processComponents){
		List<Element> associations = processComponents.getChildren(PolAssociation.class.getSimpleName());
		Iterator<Element> iterAssociations = associations.iterator();
		while (iterAssociations.hasNext()) {
			Element assocElm = (Element) iterAssociations.next();
			PolAssociation polAssociation = new PolAssociation();
			polAssociation.setKindComparison(assocElm.getChildText("KindComparison"));

			Element operandElm = assocElm.getChild("ThePolOperand");
			if(operandElm != null){
				String operandKey = operandElm.getAttributeValue("REF");
				polAssociation.insertIntoThePolOperand((PolOperand) this.processComponents.get(operandKey));
			}

			this.persistObject(polAssociation, null);

			this.processComponents.put(assocElm.getAttributeValue("KEY"), polAssociation);
		}
	}

	private void loadPolConnection(Element processComponents){
		List<Element> connections = processComponents.getChildren(PolConnection.class.getSimpleName());
		Iterator<Element> iter = connections.iterator();
		while (iter.hasNext()) {
			Element connElm = (Element) iter.next();
			PolConnection polConn = new PolConnection();
			polConn.setConType(connElm.getChildText("ConType"));

			Element exprElm = connElm.getChild("ThePolExpression");
			PolExpression expression = (PolExpression) this.processComponents.get(exprElm.getAttributeValue("REF"));
			polConn.insertIntoThePolExpression(expression);

			this.persistObject(polConn, null);

			this.processComponents.put(connElm.getAttributeValue("KEY"), polConn);
		}
	}

	private void loadPolRelation(Element processComponents){
		List<Element> relations = processComponents.getChildren(PolRelation.class.getSimpleName());
		Iterator<Element> iterRelations = relations.iterator();
		while (iterRelations.hasNext()) {
			Element relElm = (Element) iterRelations.next();
			PolRelation polRelation = new PolRelation();
			polRelation.setKindComparison(relElm.getChildText("KindComparison"));

			Element operandElm = relElm.getChild("ThePolOperand");
			if(operandElm != null){
				String operandKey = operandElm.getAttributeValue("REF");
				polRelation.insertIntoThePolOperand((PolOperand) this.processComponents.get(operandKey));
			}

			this.persistObject(polRelation, null);

			this.processComponents.put(relElm.getAttributeValue("KEY"), polRelation);
		}
	}

	private void loadPolOperator(Element processComponents){

		ElementFilter methodOperator = new ElementFilter(PolMethodOperator.class.getSimpleName());
		ElementFilter reservedWordOperator = new ElementFilter(PolReservedWordOperator.class.getSimpleName());

		AbstractFilter operator = (AbstractFilter) methodOperator.or(reservedWordOperator);

		Iterator<Element> iter = processComponents.getDescendants(operator);
		while (iter.hasNext()) {
			Element operElm = (Element) iter.next();

			if(operElm.getQualifiedName().equals(PolMethodOperator.class.getSimpleName())){
				PolMethodOperator polMethodOperator = new PolMethodOperator();
				polMethodOperator.setIdent(operElm.getChildText("Ident"));
				polMethodOperator.setType(operElm.getChildText("Type"));
				polMethodOperator.setResult(operElm.getChildText("Result"));

				this.persistObject(polMethodOperator, null);

				this.processComponents.put(operElm.getAttributeValue("KEY"), polMethodOperator);
			}
			else{ //operElm.getQualifiedName().equals(PolReservedWordOperator.class.getSimpleName())
				PolReservedWordOperator polReservedWordOperator = new PolReservedWordOperator();
				polReservedWordOperator.setKindReservedWord(operElm.getChildText("KindReservedWord"));

				this.persistObject(polReservedWordOperator, null);

				this.processComponents.put(operElm.getAttributeValue("KEY"), polReservedWordOperator);
			}
		}

		iter = processComponents.getDescendants(operator);
		while (iter.hasNext()) {
			Element operElm = (Element) iter.next();
			String key = operElm.getAttributeValue("KEY");
			PolOperator polOperator = (PolOperator) this.processComponents.get(key);

			Element next = operElm.getChild("NextOperator");
			if(next != null){
				polOperator.insertIntoNextOperator((PolOperator) this.processComponents.get(next.getAttributeValue("REF")));
			}
		}
	}

	private void loadPolObject(Element processComponents){

		List<Element> interfaces = processComponents.getChildren(PolInterface.class.getSimpleName());
		Iterator<Element> iterInterfaces = interfaces.iterator();
		while (iterInterfaces.hasNext()) {
			Element interfaceElm = (Element) iterInterfaces.next();
			PolInterface polInterface = new PolInterface();
			polInterface.setLabel(interfaceElm.getChildText("Label"));
			polInterface.setPolApseeType(interfaceElm.getChildText("PolApseeType"));
			polInterface.setPolApseeIdent(interfaceElm.getChildText("PolApseeIdent"));

			this.persistObject(polInterface, null);

			this.processComponents.put(interfaceElm.getAttributeValue("KEY"), polInterface);
		}

		ElementFilter objValueFilter = new ElementFilter(PolObjValue.class.getSimpleName());
		ElementFilter objInterfaceFilter = new ElementFilter(PolObjectInterface.class.getSimpleName());
		AbstractFilter objFilter = (AbstractFilter) objValueFilter.or(objInterfaceFilter);

		Iterator<Element> iter = processComponents.getDescendants(objFilter);
		while (iter.hasNext()) {
			Element objElm = (Element) iter.next();
			if(objElm.getQualifiedName().equals(PolObjValue.class.getSimpleName())){
				PolObjValue value = new PolObjValue();
				value.setObjectType(objElm.getChildText("ObjectType"));
				value.setObjectValue(objElm.getChildText("ObjectValue"));

				this.persistObject(value, null);

				this.processComponents.put(objElm.getAttributeValue("KEY"), value);
			}
			else{ //objElm.getQualifiedName().equals(PolObjInterface.class.getSimpleName())
				PolObjectInterface objInterface = new PolObjectInterface();
				String interfaceKey = objElm.getChild("ThePolInterface").getAttributeValue("REF");
				objInterface.insertIntoThePolInterface((PolInterface) this.processComponents.get(interfaceKey));
				this.persistObject(objInterface, null);

				this.processComponents.put(objElm.getAttributeValue("KEY"), objInterface);
			}
		}
	}

	private void loadPolRequiredOperand(Element processComponents){
		List<Element> requireds = processComponents.getChildren(PolRequiredOperand.class.getSimpleName());
		Iterator<Element> iter = requireds.iterator();
		while (iter.hasNext()) {
			Element reqElm = (Element) iter.next();
			PolRequiredOperand polReq = new PolRequiredOperand();
			polReq.setLabel(reqElm.getChildText("Label"));
			polReq.setRequiredType(reqElm.getChildText("RequiredType"));

			Element isRequiredByElm = reqElm.getChild("IsRequiredBy");
			if(isRequiredByElm != null){
				String isRequiredByKey = isRequiredByElm.getAttributeValue("REF");
				polReq.insertIntoIsRequiredBy((PolMethodOperator) this.processComponents.get(isRequiredByKey));
			}

			Element operandElm = reqElm.getChild("ThePolOperand");
			if(operandElm != null){
				String operandKey = operandElm.getAttributeValue("REF");
				polReq.insertIntoThePolOperand((PolOperand) this.processComponents.get(operandKey));
			}

			this.persistObject(polReq, null);

			this.processComponents.put(reqElm.getAttributeValue("KEY"), polReq);
		}
	}

	private void loadParameters(Element processComponents, Element organizational) {

		ElementFilter artifactParam = new ElementFilter(ArtifactParam.class.getSimpleName());
		ElementFilter primitiveParam = new ElementFilter(PrimitiveParam.class.getSimpleName());

		AbstractFilter parameters = (AbstractFilter) artifactParam.or(primitiveParam);
		Iterator<Element> iter = processComponents.getDescendants(parameters);
		while (iter.hasNext()) {
			Element paramElm = (Element) iter.next();
			String key = paramElm.getAttributeValue("KEY");
			if(paramElm.getQualifiedName().equals(ArtifactParam.class.getSimpleName())){

				ArtifactParam artParam = new ArtifactParam();
				artParam.setDescription(paramElm.getChildText("Description"));

				Element autoElm = paramElm.getChild("TheAutomatic");
				if(autoElm != null){
					String autoKey = autoElm.getAttributeValue("REF");
					artParam.insertIntoTheAutomatic((Automatic) this.processComponents.get(autoKey));
				}

				Element artElm = paramElm.getChild("TheArtifact");
				if(artElm != null){
					String artKey = artElm.getAttributeValue("REF");
					artParam.insertIntoTheArtifact((Artifact) this.organizational.get(artKey));
				}

				this.persistObject(artParam, null);

				this.processComponents.put(key, artParam);
			}
			else{ //paramElm.getQualifiedName().equals(PrimitiveParam.class.getSimpleName())

				PrimitiveParam priParam = new PrimitiveParam();
				priParam.setDescription(paramElm.getChildText("Description"));

				Element autoElm = paramElm.getChild("TheAutomatic");
				if(autoElm != null){
					String autoKey = autoElm.getAttributeValue("REF");
					priParam.insertIntoTheAutomatic((Automatic) this.processComponents.get(autoKey));
				}

				this.persistObject(priParam, null);

				this.processComponents.put(key, priParam);
			}
		}
	}

	private void loadProcessModelGraphicalDescriptor(String processId, Element coordinatesXML, Session session) {
		List<Element> graphCoords = coordinatesXML.getChildren();
		Iterator<Element> iterGraphCoords = graphCoords.iterator();
		while (iterGraphCoords.hasNext()) {
			Element graphCoordXML = (Element) iterGraphCoords.next();
			GraphicCoordinate gc = new GraphicCoordinate();

			Element webapObjXML = graphCoordXML.getChild("WebAPSEEObject");

			WebAPSEEObject wobj = new WebAPSEEObject();
			wobj.setClassName(webapObjXML.getChildText("ClassName"));

			String ref = webapObjXML.getChildText("ReferredOid");
			Object obj = this.processComponents.get(ref);

			Integer newOid = this.getOid(obj);
			if(newOid == null) continue;

			wobj.setTheReferredOid(newOid);

			gc.insertIntoTheObjectReference(wobj);
			gc.setTheProcess(processId);
			gc.setVisible(Boolean.valueOf(graphCoordXML.getChildText("Visible")));
			gc.setX(Integer.valueOf(graphCoordXML.getChildText("X")));
			gc.setY(Integer.valueOf(graphCoordXML.getChildText("Y")));

			// Persistence operations
//				GraphicCoordinateDAO gcdao = new GraphicCoordinateDAO();
//				gcdao.setSession(session);
//				gcdao.save(gc);
		}
	}

	/**
	 * Import Process Associative Objects
	 */
	private void loadProcessAssociatives(Element associatives) {

		List<Element> artTasks = associatives.getChildren(ArtifactTask.class.getSimpleName());
		Iterator<Element> iterArtTasks = artTasks.iterator();
		while (iterArtTasks.hasNext()) {
			Element artTaskElm = (Element) iterArtTasks.next();

			String key = artTaskElm.getAttributeValue("KEY");

			Element taskElm = artTaskElm.getChild("TheTask");
			String toKey = taskElm.getAttributeValue("REF");

			Task task = (Task) this.processComponents.get(toKey);

			Element artElm = artTaskElm.getChild("TheArtifact");
			String fromKey = artElm.getAttributeValue("REF");

			Artifact artifact = (Artifact) this.organizational.get(fromKey);

			if(!this.isAssociativeExists(ArtifactTask.class, artifact, task)){

				ArtifactTask artTask = new ArtifactTask();
				artTask.insertIntoTheArtifact(artifact);
				artTask.insertIntoTheTask(task);
				artTask.setInWorkspaceVersion(artTaskElm.getChildText("InWorkspaceVersion"));
				artTask.setOutWorkspaceVersion(artTaskElm.getChildText("OutWorkspaceVersion"));

				artTask = (ArtifactTask) this.persistObject(artTask, null);

				this.associatives.put(key, artTask);
			}
		}

		List<Element> bctas = associatives.getChildren(BranchConCondToActivity.class.getSimpleName());
		Iterator<Element> iterbctas = bctas.iterator();
		while (iterbctas.hasNext()) {
			Element bctaElm = (Element) iterbctas.next();

			String key = bctaElm.getAttributeValue("KEY");

			Element branchConCondElm = bctaElm.getChild("TheBranchConCond");
			String branchConCondKey = branchConCondElm.getAttributeValue("REF");

			BranchConCond branchConCond = (BranchConCond) this.processComponents.get(branchConCondKey);

			Element actElm = bctaElm.getChild("TheActivity");
			String actKey = actElm.getAttributeValue("REF");

			Activity activity = (Activity) this.processComponents.get(actKey);

			if(!this.isAssociativeExists(BranchConCondToActivity.class, branchConCond, activity)){

				BranchConCondToActivity bcta = new BranchConCondToActivity();
				bcta.insertIntoTheBranchConCond(branchConCond);
				bcta.insertIntoTheActivity(activity);

				this.processComponents.put(bctaElm.getChild("TheCondition").getAttributeValue("REF"), bcta.getTheCondition());

				bcta = (BranchConCondToActivity) this.persistObject(bcta, null);

				this.associatives.put(key, bcta);
			}
		}

		List<Element> bctms = associatives.getChildren(BranchConCondToMultipleCon.class.getSimpleName());
		Iterator<Element> iterbctms = bctas.iterator();
		while (iterbctms.hasNext()) {
			Element bctmElm = (Element) iterbctms.next();

			String key = bctmElm.getAttributeValue("KEY");

			Element branchConCondElm = bctmElm.getChild("TheBranchConCond");
			String branchConCondKey = branchConCondElm.getAttributeValue("REF");

			BranchConCond branchConCond = (BranchConCond) this.processComponents.get(branchConCondKey);

			Element mcElm = bctmElm.getChild("TheMultipleCon");
			String mcKey = mcElm.getAttributeValue("REF");

			MultipleCon multipleCon = (MultipleCon) this.processComponents.get(mcKey);

			if(!this.isAssociativeExists(BranchConCondToMultipleCon.class, branchConCond, multipleCon)){

				BranchConCondToMultipleCon bctm = new BranchConCondToMultipleCon();
				bctm.insertIntoTheBranchConCond(branchConCond);
				bctm.insertIntoTheMultipleCon(multipleCon);

				this.processComponents.put(bctmElm.getChild("TheCondition").getAttributeValue("REF"), bctm.getTheCondition());

				bctm = (BranchConCondToMultipleCon) this.persistObject(bctm, null);

				this.associatives.put(key, bctm);
			}
		}

		List<Element> rras = associatives.getChildren(ReqAgentRequiresAbility.class.getSimpleName());
		Iterator<Element> iterrras = rras.iterator();
		while (iterrras.hasNext()) {
			Element rraElm = (Element) iterrras.next();

			String key = rraElm.getAttributeValue("KEY");

			Element reqElm = rraElm.getChild("TheReqAgent");
			String reqKey = reqElm.getAttributeValue("REF");

			ReqAgent reqAgent = (ReqAgent) this.processComponents.get(reqKey);

			Element abilityElm = rraElm.getChild("TheAbility");
			String abilityKey = abilityElm.getAttributeValue("REF");

			Ability ability = (Ability) this.organizational.get(abilityKey);

			if(!this.isAssociativeExists(ReqAgentRequiresAbility.class, reqAgent, ability)){

				ReqAgentRequiresAbility rra = new ReqAgentRequiresAbility();
				rra.insertIntoTheReqAgent(reqAgent);
				rra.insertIntoTheAbility(ability);
				rra.setDegree(Integer.valueOf(rraElm.getChildText("Degree")));

				rra = (ReqAgentRequiresAbility) this.persistObject(rra, null);

				this.associatives.put(key, rra);
			}
		}
	}

	/*
	 * Auxiliary Methods
	 */

	private Integer getOid(Object obj){
		if(obj == null) return null;

		try {
			Class[] types = null;
			Method get = obj.getClass().getMethod("getOid", types);
			Object[] params = null;
			Integer oid = (Integer) get.invoke(obj, params);
			return oid;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Object setAttributes(Element e, Object obj) throws WebapseeException {

		String key = new String(e.getAttribute("KEY").getValue());
		String ident = e.getChildText("Ident");
		if (ident == null) {
			if (obj instanceof MetricDefinition)
				ident = e.getChildText("Name");
		}

		if (!(obj instanceof Connection)) {
			Object internalObj = this.getObjectFromDatabase(key, ident);
			if (internalObj != null)
				obj = internalObj; // Take care, this attribution will bring
									// relationships together!
		}

		for (int i = 0; i < e.getChildren().size(); i++) {
			Element element = (Element) e.getChildren().get(i);

			if (!this.isElementARelationship(element)) {
				// So it is an attribute! Not a relationship...

				Class[] parameters = {};
				Method metodo;
				try {
					Class objClass = obj.getClass();
					metodo = objClass.getMethod("get" + element.getQualifiedName(), parameters);

					if (!element.getChildren("Item").isEmpty()) { // Build collection of primitives!

						Collection aux = this.buildAttributeCollectionForElement(element);

						// Setting attribute
						Object[] param = { aux };
						UtilReflection.invokeMethod(obj, "set" + element.getQualifiedName(), param);
					} else {
						Object att = this.buildAttribute(metodo.getReturnType(), element.getValue());
						if (att != null) {
							if (obj instanceof Connection
								&& element.getQualifiedName().equals("Ident")) {
								att = ident.substring(0, ident.lastIndexOf("."));
							}

							Object[] param = { att };
							UtilReflection.invokeMethod(obj, "set" + element.getQualifiedName(), param);
						}
					}
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
					continue;
				}
			}
		}
		return obj;
	}

	/**
	*  Create the collection with the elements (it must be primitive/Java type)
	*/
	private Collection buildAttributeCollectionForElement(Element element) {

		Element item = (Element) element.getChildren().get(0);
		String className = item.getAttributeValue("Class");

		// Auxiliary collection for set operation
		Collection aux = new HashSet();

		Class collectionGenerics = null;

		if(this.isPrimitive(className)){

			try {
				collectionGenerics = Class.forName("java.lang."+className);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				return null;
			}
		}
		else if(className.equals(Date.class.getSimpleName())){
			collectionGenerics = Date.class;
		}
		else{
			return null;
		}

		// Adding values in the auxiliary collection
		for (int j = 0; j < element.getChildren().size(); j++) {
			item = (Element) element.getChildren().get(j);

			Object att = this.buildAttribute(collectionGenerics, item.getValue());
			if(att != null){
				aux.add(att);
			}
		}
		return aux;
	}

	private Object persistObject(Object obj, String oldIdent){

		String className = obj.getClass().getName();
		String daoName = className.replace("org.qrconsult.spm.model", "org.qrconsult.spm.dataAccess.impl").concat("DAO");
//		String serviceName = className.replace("org.qrconsult.spm.model", "org.qrconsult.spm.services.interfaces").concat("Services");
		Object dao = null;

		try {
			dao = Class.forName(daoName).newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			return null;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			return null;
		}

		Object[] parameter = { obj };

		Object[] nullparam = null;
		Integer oid = (Integer) UtilReflection.invokeMethod(obj, "getOid", nullparam);
//		if(oid == null)
//			UtilReflection.invokeMethod(dao, "save", parameter);
//		else
//			UtilReflection.invokeMethod(dao, "update", parameter); // It must be the save operation since ConnectionDAO.save() is needed
//
		if(obj instanceof Connection){
			String ident = ((Connection)obj).getIdent();
			if(oldIdent != null)
				this.idCons.put(oldIdent, ident); // Useful for adjust coordinates later...
		}

		return obj;
	}

	private boolean isElementARelationship(Element element){

		if (element.getAttribute("REF") != null) return true;

		List<Element> children = element.getChildren("Item");
		if (!children.isEmpty()){

			Element firstChild = children.get(0);
			String attClass = firstChild.getAttribute("Class").getValue();
			if(!this.isPrimitive(attClass)) return true;
		}

		return false;
	}

	private Object buildOrgObject(Element element){
		if(element == null) return null;
		Attribute att = element.getAttribute("KEY");
		if(att == null) return null;
		String key = new String(att.getValue());
		String className = key.substring(0, key.indexOf("_"));

		// Create instance for organizational objects
		Object obj = null;
		try {
			obj = Class.forName(className).newInstance();

			// Setting object attributes
			obj = this.setAttributes(element, obj);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WebapseeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Persisting object before establish relationships
		obj = this.persistObject(obj, null);
		// Caching objects
		this.organizational.put(key, obj);

		return obj;
	}

	private Object buildProcObject(Element element){
		if(element == null) return null;
		Attribute att = element.getAttribute("KEY");
		if(att == null) return null;
		String key = new String(att.getValue());
		String className = key.substring(0, key.indexOf("_"));

		// Create instance for organizational objects
		Object obj = null;
		try {
			obj = Class.forName(className).newInstance();

			// Setting object attributes
			obj = this.setAttributes(element, obj);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (WebapseeException e) {
			e.printStackTrace();
		}

		String ident = null;
		if(obj instanceof Connection) ident = element.getChildText("Ident");
		// Persisting object before establish relationships
		obj = this.persistObject(obj, ident); // Is it necessary since some objects such as Connection
											  // (and subclasses) need to be saved by their DAO operations.
		// Caching objects
		this.processComponents.put(key, obj);

		return obj;
	}

	private void adjustCoordinatesXML(Element coordinate, Element parentOut) {

		Content[] list = (Content[]) coordinate.getContent().toArray(new Content[] {});

		for (int i = 0; i < list.length; i++) {
			Content content = list[i];
			if (content instanceof Element) {
				Element element = (Element) content;
				Element newElement = new Element(element.getName(), element.getNamespacePrefix(), element.getNamespaceURI());

				addAttributes(newElement, element);
				adjustCoordinatesXML(element, newElement);

				parentOut.addContent(newElement);

			} else if (content instanceof Text) {
				boolean ok = false;
				if (content.getParentElement().getParentElement() != null) {

					if (((Element) content.getParent().getParent()).getQualifiedName().equals("SEQUENCENODE")
						|| ((Element) content.getParent().getParent()).getQualifiedName().equals("BRANCHConNODE")
						|| ((Element) content.getParent().getParent()).getQualifiedName().equals("JOINConNODE")
						|| ((Element) content.getParent().getParent()).getQualifiedName().equals("ARTIFACTCONNODE")) {

						if(((Element) content.getParent()).getQualifiedName().equals("IDENT")){
							String id = content.getValue();
							String newId = idCons.getProperty(id);
							parentOut.addContent(newId);
							ok = true;
						}
					}
				}
				if (!ok)
					parentOut.addContent((Content) content.clone());
			}
		}
	}

	private void addAttributes(Element out, Element in) {

        LinkedHashMap allAttributes = new LinkedHashMap();

        List outAttributes = new ArrayList(out.getAttributes());
        List inAttributes = new ArrayList(in.getAttributes());

        for (int i = 0; i < outAttributes.size(); i++) {
            Attribute attr = (Attribute) outAttributes.get(i);
            attr.detach();
            allAttributes.put(attr.getQualifiedName(), attr);
        }

        for (int i = 0; i < inAttributes.size(); i++) {
            Attribute attr = (Attribute) inAttributes.get(i);
            attr.detach();
            allAttributes.put(attr.getQualifiedName(), attr);
        }

        out.setAttributes(new ArrayList(allAttributes.values()));
    }

	private Object getObjectFromDatabase(String key, String ident) throws WebapseeException {

		if(ident == null || ident.equals(""))
			return null;

		String className = key.substring(0, key.indexOf("_"));
		String daoName = className.replace("org.qrconsult.spm.model", "org.qrconsult.spm.dataAccess.impl").concat("DAO");
		Object dao = null;

//		try {
//			dao = Class.forName(daoName).newInstance();
//			((BaseDAO) dao).setSession(this.currentSession);
//		} catch (InstantiationException e1) {
//			e1.printStackTrace();
//			return null;
//		} catch (IllegalAccessException e1) {
//			e1.printStackTrace();
//			return null;
//		} catch (ClassNotFoundException e1) {
//			e1.printStackTrace();
//			return null;
//		}

		Object internalObj = null;
		Object[] array_parameters = { ident };

		if (ident != null) { // When this conditions is true, it means that the object has ident attribute.
			internalObj = UtilReflection.invokeMethod(dao, "findBySecondaryKey", array_parameters);
		}
		return internalObj;
	}

	/**
	 * This method works fine for he following attribute types
	 * String, Boolean (boolean), Long (long), Integer (int),
	 * Double (double), Float (float), Date
	 *
	 */
	private Object buildAttribute(Class classe, String value) {

		if(value == null || value.equals(""))
			return null;

		Object att = null;

		if (classe.equals(String.class)) {
			att = value;
		} else if (classe.equals(Boolean.class) || classe.equals(boolean.class)) {
			att = Boolean.valueOf(value);
		} else if (classe.equals(Long.class) || classe.equals(long.class)) {
			att = Long.valueOf(value);
		} else if (classe.equals(Integer.class) || classe.equals(int.class)) {
			att = Integer.valueOf(value);
		} else if (classe.equals(Double.class) || classe.equals(double.class)) {
			att = Double.valueOf(value);
		} else if (classe.equals(Float.class) || classe.equals(float.class)) {
			att = Float.valueOf(value);
		} else if (classe.equals(Date.class)) {

			Calendar date = new GregorianCalendar();
			// 2007-12-09 00:00:00.0 date format
			date.set(Calendar.DAY_OF_MONTH,Integer.valueOf(value.substring(8, 10)));
			date.set(Calendar.MONTH, Integer.valueOf(value.substring(5, 7))+1);
			date.set(Calendar.YEAR, Integer.valueOf(value.substring(0, 4)));
			date.set(Calendar.HOUR, Integer.valueOf(value.substring(11, 13)));
			date.set(Calendar.MINUTE, Integer.valueOf(value.substring(14, 16)));
			date.set(Calendar.SECOND, Integer.valueOf(value.substring(17, 19)));

			att = date.getTime();
		}
		return att;
	}

	/**
	 * @param classe means the class simple name
	 *
	 */
	private boolean isPrimitive(String classe){

		if (classe.equals(String.class.getSimpleName())) {
			return true;
		} else if (classe.equals(Boolean.class.getSimpleName()) || classe.equals(boolean.class.getSimpleName())) {
			return true;
		} else if (classe.equals(Long.class.getSimpleName()) || classe.equals(long.class.getSimpleName())) {
			return true;
		} else if (classe.equals(Integer.class.getSimpleName()) || classe.equals(int.class.getSimpleName())) {
			return true;
		} else if (classe.equals(Double.class.getSimpleName()) || classe.equals(double.class.getSimpleName())) {
			return true;
		} else if (classe.equals(Float.class.getSimpleName()) || classe.equals(float.class.getSimpleName())) {
			return true;
		}
		return false;
	}

	private boolean isAssociativeExists(Class classe, Object from, Object to){

		String hql = "FROM "+ classe.getName() +" as obj WHERE ";
		if(classe.equals(AgentAffinityAgent.class))
			hql += "obj.fromAffinity.ident = '"+ ((Agent)from).getIdent() + "' AND obj.toAffinity.ident = '"+ ((Agent)to).getIdent() + "'";
		else if(classe.equals(AgentHasAbility.class))
			hql += "obj.theAgent.ident = '"+ ((Agent)from).getIdent() + "' AND obj.theAbility.ident = '"+ ((Ability)to).getIdent() + "'";
		else if(classe.equals(AgentPlaysRole.class))
			hql += "obj.theAgent.ident = '"+ ((Agent)from).getIdent() + "' AND obj.theRole.ident = '"+ ((Role)to).getIdent() + "'";
		else if(classe.equals(RoleNeedsAbility.class))
			hql += "obj.theRole.ident = '"+ ((Role)from).getIdent() + "' AND obj.theAbility.ident = '"+ ((Ability)to).getIdent() + "'";
		else if(classe.equals(ArtifactTask.class))
			hql += "obj.theArtifact.ident = '"+ ((Artifact)from).getIdent() + "' AND obj.theTask.theNormal.ident = '"+ ((Task)to).getTheNormal().getIdent() + "'";
		else if(classe.equals(BranchConCondToActivity.class))
			hql += "obj.theBranchConCond.ident = '"+ ((BranchConCond)from).getIdent() + "' AND obj.theActivity.ident = '"+ ((Activity)to).getIdent() + "'";
		else if(classe.equals(BranchConCondToMultipleCon.class))
			hql += "obj.theBranchConCond.ident = '"+ ((BranchConCond)from).getIdent() + "' AND obj.theMultipleCon.ident = '"+ ((MultipleCon)to).getIdent() + "'";
		else if(classe.equals(ReqAgentRequiresAbility.class))
			hql += "obj.theReqAgent.theNormal.ident = '"+ ((ReqAgent)from).getTheNormal().getIdent() +"' " +
					"AND obj.theReqAgent.theRole.ident = '"+ ((ReqAgent)from).getTheRole().getIdent() +"' " +
					"AND obj.theReqAgent.theAgent.ident = '"+ ((ReqAgent)from).getTheAgent().getIdent() +"' " +
					"AND obj.theAbility.ident = '"+ ((Ability)to).getIdent() + "'";
		else return false;

//		Query query = this.currentSession.createQuery(hql);
//		List result = query.list();
//
//		if(result.isEmpty()) return false;
		return true;
	}

	public static void main(String[] args) {

		XMLReaderTest rt = new XMLReaderTest();
		rt.importProcess("829", "ejbModule/PCDP.xml");
		java.lang.System.out.println(rt.process);

//		try {
//			File xmlFile = new File("ejbModule/PCDP.xml");
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder;
//			dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(xmlFile);
//			doc.getDocumentElement().normalize();
//
//			System.out.println("Elemento Root: " + doc.getDocumentElement().getNodeName());
//
//			NodeList agentNode = doc.getElementsByTagName("Agent");
//			for(int i = 0; i < agentNode.getLength(); i++) {
//				Node agent = agentNode.item(i);
//				System.out.println("\nElemento: " + agent.getNodeName());
//
//				if(agent.getNodeType() == Node.ELEMENT_NODE) {
//					Element agentElement = (Element) agent;
//
//					System.out.println("Nome: " + agentElement.getElementsByTagName("Name").item(0).getTextContent());
//					System.out.println("Email: " + agentElement.getElementsByTagName("EMail").item(0).getTextContent());
//					System.out.println("Custo\\Hora: " + agentElement.getElementsByTagName("CostHour").item(0).getTextContent());
//					System.out.println("Descri��o: " + agentElement.getElementsByTagName("Description").item(0).getTextContent());
//				}
//			}
//
//
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
