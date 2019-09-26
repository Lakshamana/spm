package br.ufpa.labes.spm.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Agent.
 */
@Entity
@Table(name = "agent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ident")
    private String ident;

    @Column(name = "name")
    private String name;

    @Column(name = "e_mail")
    private String eMail;

    @Column(name = "cost_hour")
    private Float costHour;

    @Column(name = "password")
    private String password;

    @Column(name = "tipo_user")
    private Integer tipoUser;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "online")
    private Boolean online;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @Column(name = "upload")
    private String upload;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private TaskAgenda theTaskAgenda;

    @OneToOne
    @JoinColumn(unique = true)
    private SpmConfiguration configuration;

    @OneToMany(mappedBy = "delegatedFrom")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> delegates = new HashSet<>();

    @OneToMany(mappedBy = "delegatedTo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> isDelegatedFors = new HashSet<>();

    @OneToMany(mappedBy = "theAgent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ModelingActivityEvent> theModelingActivityEvents = new HashSet<>();

    @OneToMany(mappedBy = "theAgent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ReqAgent> theReqAgents = new HashSet<>();

    @OneToMany(mappedBy = "agent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentMetric> theAgentMetrics = new HashSet<>();

    @OneToMany(mappedBy = "agent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentEstimation> theAgentEstimations = new HashSet<>();

    @OneToMany(mappedBy = "theAgent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CompanyUnit> theManagedOrgUnits = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("theRequestorAgents")
    private ResourceEvent theResourceEvent;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "agent_the_process",
               joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "the_process_id", referencedColumnName = "id"))
    private Set<Process> theProcesses = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "agent_the_work_group",
               joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "the_work_group_id", referencedColumnName = "id"))
    private Set<WorkGroup> theWorkGroups = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "agent_the_org_units",
               joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "the_org_units_id", referencedColumnName = "id"))
    private Set<CompanyUnit> theOrgUnits = new HashSet<>();

    @OneToOne(mappedBy = "de")
    @JsonIgnore
    private ChatMessage theChatMessage;

    @ManyToOne
    @JsonIgnoreProperties("theManagers")
    private EmailConfiguration theEmailConfiguration;

    @OneToMany(mappedBy = "toAffinity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentAffinityAgent> fromAgentAffinities = new HashSet<>();

    @OneToMany(mappedBy = "fromAffinity")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentAffinityAgent> toAgentAffinities = new HashSet<>();

    @OneToMany(mappedBy = "theAgent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentHasAbility> theAgentHasAbilities = new HashSet<>();

    @OneToMany(mappedBy = "theAgent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentPlaysRole> theAgentPlaysRoles = new HashSet<>();

    @OneToMany(mappedBy = "theAgent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OutOfWorkPeriod> theOutOfWorkPeriods = new HashSet<>();

    @OneToMany(mappedBy = "chosenAgent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentInstSug> theAgentInstSugs = new HashSet<>();

    @OneToMany(mappedBy = "theAgent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentInstSuggestionToAgent> theAgentInstSugToAgents = new HashSet<>();

    @OneToMany(mappedBy = "theAgent")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AgentWorkingLoad> theAgentWorkingLoads = new HashSet<>();

    @ManyToMany(mappedBy = "involvedAgentsInChats")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<ChatLog> theChatLogs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdent() {
        return ident;
    }

    public Agent ident(String ident) {
        this.ident = ident;
        return this;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getName() {
        return name;
    }

    public Agent name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public Agent eMail(String eMail) {
        this.eMail = eMail;
        return this;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Float getCostHour() {
        return costHour;
    }

    public Agent costHour(Float costHour) {
        this.costHour = costHour;
        return this;
    }

    public void setCostHour(Float costHour) {
        this.costHour = costHour;
    }

    public String getPassword() {
        return password;
    }

    public Agent password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTipoUser() {
        return tipoUser;
    }

    public Agent tipoUser(Integer tipoUser) {
        this.tipoUser = tipoUser;
        return this;
    }

    public void setTipoUser(Integer tipoUser) {
        this.tipoUser = tipoUser;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Agent isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isOnline() {
        return online;
    }

    public Agent online(Boolean online) {
        this.online = online;
        return this;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public Agent photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public Agent photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getUpload() {
        return upload;
    }

    public Agent upload(String upload) {
        this.upload = upload;
        return this;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getDescription() {
        return description;
    }

    public Agent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskAgenda getTheTaskAgenda() {
        return theTaskAgenda;
    }

    public Agent theTaskAgenda(TaskAgenda taskAgenda) {
        this.theTaskAgenda = taskAgenda;
        return this;
    }

    public void setTheTaskAgenda(TaskAgenda taskAgenda) {
        this.theTaskAgenda = taskAgenda;
    }

    public SpmConfiguration getConfiguration() {
        return configuration;
    }

    public Agent configuration(SpmConfiguration spmConfiguration) {
        this.configuration = spmConfiguration;
        return this;
    }

    public void setConfiguration(SpmConfiguration spmConfiguration) {
        this.configuration = spmConfiguration;
    }

    public Set<Task> getDelegates() {
        return delegates;
    }

    public Agent delegates(Set<Task> tasks) {
        this.delegates = tasks;
        return this;
    }

    public Agent addDelegates(Task task) {
        this.delegates.add(task);
        task.setDelegatedFrom(this);
        return this;
    }

    public Agent removeDelegates(Task task) {
        this.delegates.remove(task);
        task.setDelegatedFrom(null);
        return this;
    }

    public void setDelegates(Set<Task> tasks) {
        this.delegates = tasks;
    }

    public Set<Task> getIsDelegatedFors() {
        return isDelegatedFors;
    }

    public Agent isDelegatedFors(Set<Task> tasks) {
        this.isDelegatedFors = tasks;
        return this;
    }

    public Agent addIsDelegatedFor(Task task) {
        this.isDelegatedFors.add(task);
        task.setDelegatedTo(this);
        return this;
    }

    public Agent removeIsDelegatedFor(Task task) {
        this.isDelegatedFors.remove(task);
        task.setDelegatedTo(null);
        return this;
    }

    public void setIsDelegatedFors(Set<Task> tasks) {
        this.isDelegatedFors = tasks;
    }

    public Set<ModelingActivityEvent> getTheModelingActivityEvents() {
        return theModelingActivityEvents;
    }

    public Agent theModelingActivityEvents(Set<ModelingActivityEvent> modelingActivityEvents) {
        this.theModelingActivityEvents = modelingActivityEvents;
        return this;
    }

    public Agent addTheModelingActivityEvent(ModelingActivityEvent modelingActivityEvent) {
        this.theModelingActivityEvents.add(modelingActivityEvent);
        modelingActivityEvent.setTheAgent(this);
        return this;
    }

    public Agent removeTheModelingActivityEvent(ModelingActivityEvent modelingActivityEvent) {
        this.theModelingActivityEvents.remove(modelingActivityEvent);
        modelingActivityEvent.setTheAgent(null);
        return this;
    }

    public void setTheModelingActivityEvents(Set<ModelingActivityEvent> modelingActivityEvents) {
        this.theModelingActivityEvents = modelingActivityEvents;
    }

    public Set<ReqAgent> getTheReqAgents() {
        return theReqAgents;
    }

    public Agent theReqAgents(Set<ReqAgent> reqAgents) {
        this.theReqAgents = reqAgents;
        return this;
    }

    public Agent addTheReqAgent(ReqAgent reqAgent) {
        this.theReqAgents.add(reqAgent);
        reqAgent.setTheAgent(this);
        return this;
    }

    public Agent removeTheReqAgent(ReqAgent reqAgent) {
        this.theReqAgents.remove(reqAgent);
        reqAgent.setTheAgent(null);
        return this;
    }

    public void setTheReqAgents(Set<ReqAgent> reqAgents) {
        this.theReqAgents = reqAgents;
    }

    public Set<AgentMetric> getTheAgentMetrics() {
        return theAgentMetrics;
    }

    public Agent theAgentMetrics(Set<AgentMetric> agentMetrics) {
        this.theAgentMetrics = agentMetrics;
        return this;
    }

    public Agent addTheAgentMetric(AgentMetric agentMetric) {
        this.theAgentMetrics.add(agentMetric);
        agentMetric.setAgent(this);
        return this;
    }

    public Agent removeTheAgentMetric(AgentMetric agentMetric) {
        this.theAgentMetrics.remove(agentMetric);
        agentMetric.setAgent(null);
        return this;
    }

    public void setTheAgentMetrics(Set<AgentMetric> agentMetrics) {
        this.theAgentMetrics = agentMetrics;
    }

    public Set<AgentEstimation> getTheAgentEstimations() {
        return theAgentEstimations;
    }

    public Agent theAgentEstimations(Set<AgentEstimation> agentEstimations) {
        this.theAgentEstimations = agentEstimations;
        return this;
    }

    public Agent addTheAgentEstimation(AgentEstimation agentEstimation) {
        this.theAgentEstimations.add(agentEstimation);
        agentEstimation.setAgent(this);
        return this;
    }

    public Agent removeTheAgentEstimation(AgentEstimation agentEstimation) {
        this.theAgentEstimations.remove(agentEstimation);
        agentEstimation.setAgent(null);
        return this;
    }

    public void setTheAgentEstimations(Set<AgentEstimation> agentEstimations) {
        this.theAgentEstimations = agentEstimations;
    }

    public Set<CompanyUnit> getTheManagedOrgUnits() {
        return theManagedOrgUnits;
    }

    public Agent theManagedOrgUnits(Set<CompanyUnit> companyUnits) {
        this.theManagedOrgUnits = companyUnits;
        return this;
    }

    public Agent addTheManagedOrgUnits(CompanyUnit companyUnit) {
        this.theManagedOrgUnits.add(companyUnit);
        companyUnit.setTheAgent(this);
        return this;
    }

    public Agent removeTheManagedOrgUnits(CompanyUnit companyUnit) {
        this.theManagedOrgUnits.remove(companyUnit);
        companyUnit.setTheAgent(null);
        return this;
    }

    public void setTheManagedOrgUnits(Set<CompanyUnit> companyUnits) {
        this.theManagedOrgUnits = companyUnits;
    }

    public ResourceEvent getTheResourceEvent() {
        return theResourceEvent;
    }

    public Agent theResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvent = resourceEvent;
        return this;
    }

    public void setTheResourceEvent(ResourceEvent resourceEvent) {
        this.theResourceEvent = resourceEvent;
    }

    public Set<Process> getTheProcesses() {
        return theProcesses;
    }

    public Agent theProcesses(Set<Process> processes) {
        this.theProcesses = processes;
        return this;
    }

    public Agent addTheProcess(Process process) {
        this.theProcesses.add(process);
        process.getTheAgents().add(this);
        return this;
    }

    public Agent removeTheProcess(Process process) {
        this.theProcesses.remove(process);
        process.getTheAgents().remove(this);
        return this;
    }

    public void setTheProcesses(Set<Process> processes) {
        this.theProcesses = processes;
    }

    public Set<WorkGroup> getTheWorkGroups() {
        return theWorkGroups;
    }

    public Agent theWorkGroups(Set<WorkGroup> workGroups) {
        this.theWorkGroups = workGroups;
        return this;
    }

    public Agent addTheWorkGroup(WorkGroup workGroup) {
        this.theWorkGroups.add(workGroup);
        workGroup.getTheAgents().add(this);
        return this;
    }

    public Agent removeTheWorkGroup(WorkGroup workGroup) {
        this.theWorkGroups.remove(workGroup);
        workGroup.getTheAgents().remove(this);
        return this;
    }

    public void setTheWorkGroups(Set<WorkGroup> workGroups) {
        this.theWorkGroups = workGroups;
    }

    public Set<CompanyUnit> getTheOrgUnits() {
        return theOrgUnits;
    }

    public Agent theOrgUnits(Set<CompanyUnit> companyUnits) {
        this.theOrgUnits = companyUnits;
        return this;
    }

    public Agent addTheOrgUnits(CompanyUnit companyUnit) {
        this.theOrgUnits.add(companyUnit);
        companyUnit.getTheUnitAgents().add(this);
        return this;
    }

    public Agent removeTheOrgUnits(CompanyUnit companyUnit) {
        this.theOrgUnits.remove(companyUnit);
        companyUnit.getTheUnitAgents().remove(this);
        return this;
    }

    public void setTheOrgUnits(Set<CompanyUnit> companyUnits) {
        this.theOrgUnits = companyUnits;
    }

    public ChatMessage getTheChatMessage() {
        return theChatMessage;
    }

    public Agent theChatMessage(ChatMessage chatMessage) {
        this.theChatMessage = chatMessage;
        return this;
    }

    public void setTheChatMessage(ChatMessage chatMessage) {
        this.theChatMessage = chatMessage;
    }

    public EmailConfiguration getTheEmailConfiguration() {
        return theEmailConfiguration;
    }

    public Agent theEmailConfiguration(EmailConfiguration emailConfiguration) {
        this.theEmailConfiguration = emailConfiguration;
        return this;
    }

    public void setTheEmailConfiguration(EmailConfiguration emailConfiguration) {
        this.theEmailConfiguration = emailConfiguration;
    }

    public Set<AgentAffinityAgent> getFromAgentAffinities() {
        return fromAgentAffinities;
    }

    public Agent fromAgentAffinities(Set<AgentAffinityAgent> agentAffinityAgents) {
        this.fromAgentAffinities = agentAffinityAgents;
        return this;
    }

    public Agent addFromAgentAffinity(AgentAffinityAgent agentAffinityAgent) {
        this.fromAgentAffinities.add(agentAffinityAgent);
        agentAffinityAgent.setToAffinity(this);
        return this;
    }

    public Agent removeFromAgentAffinity(AgentAffinityAgent agentAffinityAgent) {
        this.fromAgentAffinities.remove(agentAffinityAgent);
        agentAffinityAgent.setToAffinity(null);
        return this;
    }

    public void setFromAgentAffinities(Set<AgentAffinityAgent> agentAffinityAgents) {
        this.fromAgentAffinities = agentAffinityAgents;
    }

    public Set<AgentAffinityAgent> getToAgentAffinities() {
        return toAgentAffinities;
    }

    public Agent toAgentAffinities(Set<AgentAffinityAgent> agentAffinityAgents) {
        this.toAgentAffinities = agentAffinityAgents;
        return this;
    }

    public Agent addToAgentAffinity(AgentAffinityAgent agentAffinityAgent) {
        this.toAgentAffinities.add(agentAffinityAgent);
        agentAffinityAgent.setFromAffinity(this);
        return this;
    }

    public Agent removeToAgentAffinity(AgentAffinityAgent agentAffinityAgent) {
        this.toAgentAffinities.remove(agentAffinityAgent);
        agentAffinityAgent.setFromAffinity(null);
        return this;
    }

    public void setToAgentAffinities(Set<AgentAffinityAgent> agentAffinityAgents) {
        this.toAgentAffinities = agentAffinityAgents;
    }

    public Set<AgentHasAbility> getTheAgentHasAbilities() {
        return theAgentHasAbilities;
    }

    public Agent theAgentHasAbilities(Set<AgentHasAbility> agentHasAbilities) {
        this.theAgentHasAbilities = agentHasAbilities;
        return this;
    }

    public Agent addTheAgentHasAbility(AgentHasAbility agentHasAbility) {
        this.theAgentHasAbilities.add(agentHasAbility);
        agentHasAbility.setTheAgent(this);
        return this;
    }

    public Agent removeTheAgentHasAbility(AgentHasAbility agentHasAbility) {
        this.theAgentHasAbilities.remove(agentHasAbility);
        agentHasAbility.setTheAgent(null);
        return this;
    }

    public void setTheAgentHasAbilities(Set<AgentHasAbility> agentHasAbilities) {
        this.theAgentHasAbilities = agentHasAbilities;
    }

    public Set<AgentPlaysRole> getTheAgentPlaysRoles() {
        return theAgentPlaysRoles;
    }

    public Agent theAgentPlaysRoles(Set<AgentPlaysRole> agentPlaysRoles) {
        this.theAgentPlaysRoles = agentPlaysRoles;
        return this;
    }

    public Agent addTheAgentPlaysRole(AgentPlaysRole agentPlaysRole) {
        this.theAgentPlaysRoles.add(agentPlaysRole);
        agentPlaysRole.setTheAgent(this);
        return this;
    }

    public Agent removeTheAgentPlaysRole(AgentPlaysRole agentPlaysRole) {
        this.theAgentPlaysRoles.remove(agentPlaysRole);
        agentPlaysRole.setTheAgent(null);
        return this;
    }

    public void setTheAgentPlaysRoles(Set<AgentPlaysRole> agentPlaysRoles) {
        this.theAgentPlaysRoles = agentPlaysRoles;
    }

    public Set<OutOfWorkPeriod> getTheOutOfWorkPeriods() {
        return theOutOfWorkPeriods;
    }

    public Agent theOutOfWorkPeriods(Set<OutOfWorkPeriod> outOfWorkPeriods) {
        this.theOutOfWorkPeriods = outOfWorkPeriods;
        return this;
    }

    public Agent addTheOutOfWorkPeriod(OutOfWorkPeriod outOfWorkPeriod) {
        this.theOutOfWorkPeriods.add(outOfWorkPeriod);
        outOfWorkPeriod.setTheAgent(this);
        return this;
    }

    public Agent removeTheOutOfWorkPeriod(OutOfWorkPeriod outOfWorkPeriod) {
        this.theOutOfWorkPeriods.remove(outOfWorkPeriod);
        outOfWorkPeriod.setTheAgent(null);
        return this;
    }

    public void setTheOutOfWorkPeriods(Set<OutOfWorkPeriod> outOfWorkPeriods) {
        this.theOutOfWorkPeriods = outOfWorkPeriods;
    }

    public Set<AgentInstSug> getTheAgentInstSugs() {
        return theAgentInstSugs;
    }

    public Agent theAgentInstSugs(Set<AgentInstSug> agentInstSugs) {
        this.theAgentInstSugs = agentInstSugs;
        return this;
    }

    public Agent addTheAgentInstSug(AgentInstSug agentInstSug) {
        this.theAgentInstSugs.add(agentInstSug);
        agentInstSug.setChosenAgent(this);
        return this;
    }

    public Agent removeTheAgentInstSug(AgentInstSug agentInstSug) {
        this.theAgentInstSugs.remove(agentInstSug);
        agentInstSug.setChosenAgent(null);
        return this;
    }

    public void setTheAgentInstSugs(Set<AgentInstSug> agentInstSugs) {
        this.theAgentInstSugs = agentInstSugs;
    }

    public Set<AgentInstSuggestionToAgent> getTheAgentInstSugToAgents() {
        return theAgentInstSugToAgents;
    }

    public Agent theAgentInstSugToAgents(Set<AgentInstSuggestionToAgent> agentInstSuggestionToAgents) {
        this.theAgentInstSugToAgents = agentInstSuggestionToAgents;
        return this;
    }

    public Agent addTheAgentInstSugToAgent(AgentInstSuggestionToAgent agentInstSuggestionToAgent) {
        this.theAgentInstSugToAgents.add(agentInstSuggestionToAgent);
        agentInstSuggestionToAgent.setTheAgent(this);
        return this;
    }

    public Agent removeTheAgentInstSugToAgent(AgentInstSuggestionToAgent agentInstSuggestionToAgent) {
        this.theAgentInstSugToAgents.remove(agentInstSuggestionToAgent);
        agentInstSuggestionToAgent.setTheAgent(null);
        return this;
    }

    public void setTheAgentInstSugToAgents(Set<AgentInstSuggestionToAgent> agentInstSuggestionToAgents) {
        this.theAgentInstSugToAgents = agentInstSuggestionToAgents;
    }

    public Set<AgentWorkingLoad> getTheAgentWorkingLoads() {
        return theAgentWorkingLoads;
    }

    public Agent theAgentWorkingLoads(Set<AgentWorkingLoad> agentWorkingLoads) {
        this.theAgentWorkingLoads = agentWorkingLoads;
        return this;
    }

    public Agent addTheAgentWorkingLoad(AgentWorkingLoad agentWorkingLoad) {
        this.theAgentWorkingLoads.add(agentWorkingLoad);
        agentWorkingLoad.setTheAgent(this);
        return this;
    }

    public Agent removeTheAgentWorkingLoad(AgentWorkingLoad agentWorkingLoad) {
        this.theAgentWorkingLoads.remove(agentWorkingLoad);
        agentWorkingLoad.setTheAgent(null);
        return this;
    }

    public void setTheAgentWorkingLoads(Set<AgentWorkingLoad> agentWorkingLoads) {
        this.theAgentWorkingLoads = agentWorkingLoads;
    }

    public Set<ChatLog> getTheChatLogs() {
        return theChatLogs;
    }

    public Agent theChatLogs(Set<ChatLog> chatLogs) {
        this.theChatLogs = chatLogs;
        return this;
    }

    public Agent addTheChatLog(ChatLog chatLog) {
        this.theChatLogs.add(chatLog);
        chatLog.getInvolvedAgentsInChats().add(this);
        return this;
    }

    public Agent removeTheChatLog(ChatLog chatLog) {
        this.theChatLogs.remove(chatLog);
        chatLog.getInvolvedAgentsInChats().remove(this);
        return this;
    }

    public void setTheChatLogs(Set<ChatLog> chatLogs) {
        this.theChatLogs = chatLogs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agent)) {
            return false;
        }
        return id != null && id.equals(((Agent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Agent{" +
            "id=" + getId() +
            ", ident='" + getIdent() + "'" +
            ", name='" + getName() + "'" +
            ", eMail='" + geteMail() + "'" +
            ", costHour=" + getCostHour() +
            ", password='" + getPassword() + "'" +
            ", tipoUser=" + getTipoUser() +
            ", isActive='" + isIsActive() + "'" +
            ", online='" + isOnline() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            ", upload='" + getUpload() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
