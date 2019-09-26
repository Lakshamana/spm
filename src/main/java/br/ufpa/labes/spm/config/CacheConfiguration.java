package br.ufpa.labes.spm.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

  private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

  public CacheConfiguration(JHipsterProperties jHipsterProperties) {
    JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

    jcacheConfiguration =
        Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                    Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(
                    ExpiryPolicyBuilder.timeToLiveExpiration(
                        Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
  }

  @Bean
  public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
      javax.cache.CacheManager cacheManager) {
    return hibernateProperties ->
        hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
  }

  @Bean
  public JCacheManagerCustomizer cacheManagerCustomizer() {
    return cm -> {
      createCache(cm, br.ufpa.labes.spm.repository.UserRepository.USERS_BY_LOGIN_CACHE);
      createCache(cm, br.ufpa.labes.spm.repository.UserRepository.USERS_BY_EMAIL_CACHE);
      createCache(cm, br.ufpa.labes.spm.domain.User.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Authority.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.User.class.getName() + ".authorities");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theModelingActivityEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".hasVersions");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".fromSimpleCons");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".toSimpleCons");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".fromJoinCons");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".toBranchCons");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".activityMetrics");
      createCache(
          cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theActivityEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".toJoinCons");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".toBranchANDCons");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".fromArtifactCons");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".toArtifactCons");
      createCache(
          cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theActivityInstantiateds");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theActivityMetrics");
      createCache(cm, br.ufpa.labes.spm.domain.Decomposed.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Plain.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Plain.class.getName() + ".theGlobalActivityEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Plain.class.getName() + ".theCatalogEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Ability.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.Ability.class.getName() + ".theReqAgentRequiresAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.Ability.class.getName() + ".theAgentHasAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.Ability.class.getName() + ".theRoleNeedsAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".delegates");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".isDelegatedFors");
      createCache(
          cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theModelingActivityEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theReqAgents");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theAgentMetrics");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theAgentEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theManagedOrgUnits");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theProcesses");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theWorkGroups");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theOrgUnits");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".fromAgentAffinities");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".toAgentAffinities");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theAgentHasAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theAgentPlaysRoles");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theOutOfWorkPeriods");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theAgentInstSugs");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theAgentInstSugToAgents");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theAgentWorkingLoads");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theChatLogs");
      createCache(cm, br.ufpa.labes.spm.domain.AgentAffinityAgent.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.AgentHasAbility.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.AgentPlaysRole.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.EmailConfiguration.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.EmailConfiguration.class.getName() + ".theProcesses");
      createCache(cm, br.ufpa.labes.spm.domain.EmailConfiguration.class.getName() + ".theManagers");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".theReqWorkGroups");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".theWorkGroupMetrics");
      createCache(
          cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".theWorkGroupEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".subWorkGroups");
      createCache(
          cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".sugToChosenWorkGroups");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".theAgents");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".theWorkGroupInstSugs");
      createCache(cm, br.ufpa.labes.spm.domain.OutOfWorkPeriod.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Role.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Role.class.getName() + ".theReqAgents");
      createCache(cm, br.ufpa.labes.spm.domain.Role.class.getName() + ".theAgentPlaysRoles");
      createCache(cm, br.ufpa.labes.spm.domain.Role.class.getName() + ".theRoles");
      createCache(cm, br.ufpa.labes.spm.domain.Role.class.getName() + ".theRoleNeedsAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.Role.class.getName() + ".theAgentInstSugs");
      createCache(cm, br.ufpa.labes.spm.domain.RoleNeedsAbility.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".theInvolvedArtifacts");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".theArtifactParams");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".theAutomatics");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".theArtifactMetrics");
      createCache(
          cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".theArtifactEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".theVCSRepositories");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".possesses");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".theArtifactTasks");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".theArtifactCons");
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactTask.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName() + ".authorStats");
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName() + ".tagStats");
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName() + ".lessonsLearneds");
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName() + ".relatedAssets");
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName() + ".relatedByAssets");
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName() + ".comments");
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName() + ".favoritedBies");
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName() + ".followers");
      createCache(cm, br.ufpa.labes.spm.domain.Asset.class.getName() + ".collaborators");
      createCache(cm, br.ufpa.labes.spm.domain.AssetRelationship.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.AssetStat.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.AuthorStat.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.LessonLearned.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Message.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.RelationshipKind.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.RelationshipKind.class.getName() + ".theAssetRelationships");
      createCache(cm, br.ufpa.labes.spm.domain.Tag.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Tag.class.getName() + ".theTagStats");
      createCache(cm, br.ufpa.labes.spm.domain.TagStats.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.CalendarDay.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Calendar.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Calendar.class.getName() + ".notWorkingDays");
      createCache(cm, br.ufpa.labes.spm.domain.Calendar.class.getName() + ".projects");
      createCache(cm, br.ufpa.labes.spm.domain.ChatMessage.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.SpmConfiguration.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactCon.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactCon.class.getName() + ".toMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactCon.class.getName() + ".toActivities");
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactCon.class.getName() + ".fromActivities");
      createCache(cm, br.ufpa.labes.spm.domain.SimpleCon.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Feedback.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Sequence.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Connection.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Dependency.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Dependency.class.getName() + ".theMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.Dependency.class.getName() + ".theSequences");
      createCache(cm, br.ufpa.labes.spm.domain.JoinCon.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.JoinCon.class.getName() + ".fromMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.JoinCon.class.getName() + ".fromActivities");
      createCache(cm, br.ufpa.labes.spm.domain.MultipleCon.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".toBranchCons");
      createCache(cm, br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".theArtifactCons");
      createCache(cm, br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".fromMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".theJoinCons");
      createCache(cm, br.ufpa.labes.spm.domain.BranchCon.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.BranchANDCon.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.BranchANDCon.class.getName() + ".toMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.BranchANDCon.class.getName() + ".fromActivities");
      createCache(cm, br.ufpa.labes.spm.domain.Driver.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Email.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.CatalogEvent.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.CatalogEvent.class.getName() + ".theAgendaEvents");
      createCache(
          cm, br.ufpa.labes.spm.domain.CatalogEvent.class.getName() + ".theCatalogEventToCatalogs");
      createCache(cm, br.ufpa.labes.spm.domain.ChatLog.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ChatLog.class.getName() + ".involvedAgentsInChats");
      createCache(cm, br.ufpa.labes.spm.domain.Event.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Event.class.getName() + ".theRequestorAgents");
      createCache(cm, br.ufpa.labes.spm.domain.AgendaEvent.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.AgendaEvent.class.getName() + ".theCatalogEventToAgenda");
      createCache(cm, br.ufpa.labes.spm.domain.ConnectionEvent.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ConnectionEvent.class.getName()
              + ".theCatalogEventToConnections");
      createCache(cm, br.ufpa.labes.spm.domain.GlobalActivityEvent.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.GlobalActivityEvent.class.getName()
              + ".theCatalogEventToGlobalActivities");
      createCache(cm, br.ufpa.labes.spm.domain.ModelingActivityEvent.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ModelingActivityEvent.class.getName()
              + ".theCatalogEventToModelingActivities");
      createCache(cm, br.ufpa.labes.spm.domain.ProcessEvent.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ProcessEvent.class.getName() + ".theCatalogEventToProcesses");
      createCache(cm, br.ufpa.labes.spm.domain.ProcessModelEvent.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ProcessModelEvent.class.getName()
              + ".theCatalogEventToProcessModels");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceEvent.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ResourceEvent.class.getName() + ".theCatalogEventToResources");
      createCache(cm, br.ufpa.labes.spm.domain.SpmLog.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.SpmLog.class.getName() + ".theEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Company.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Company.class.getName() + ".organizationMetrics");
      createCache(cm, br.ufpa.labes.spm.domain.Company.class.getName() + ".theCompanyEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Company.class.getName() + ".theCompanyUnits");
      createCache(cm, br.ufpa.labes.spm.domain.Company.class.getName() + ".theDevelopingSystems");
      createCache(cm, br.ufpa.labes.spm.domain.CompanyUnit.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.CompanyUnit.class.getName() + ".theCompanyUnits");
      createCache(cm, br.ufpa.labes.spm.domain.CompanyUnit.class.getName() + ".theAgents");
      createCache(cm, br.ufpa.labes.spm.domain.Node.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Node.class.getName() + ".children");
      createCache(cm, br.ufpa.labes.spm.domain.Node.class.getName() + ".theStructures");
      createCache(cm, br.ufpa.labes.spm.domain.Project.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Project.class.getName() + ".finalArtifacts");
      createCache(cm, br.ufpa.labes.spm.domain.VCSRepository.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Structure.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Structure.class.getName() + ".theVCSRepositories");
      createCache(cm, br.ufpa.labes.spm.domain.DevelopingSystem.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.DevelopingSystem.class.getName() + ".theProjects");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".authorsFolloweds");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".theAssets");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".theAuthorStats");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".theLessonLearneds");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".sentMessages");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".receivedMessages");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".favorites");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".assetsFolloweds");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".collaborateOnAssets");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".theAuthors");
      createCache(cm, br.ufpa.labes.spm.domain.Organization.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.Organization.class.getName() + ".theOrganizationMetrics");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.Organization.class.getName() + ".theOrganizationEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Person.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Automatic.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Automatic.class.getName() + ".theParameters");
      createCache(cm, br.ufpa.labes.spm.domain.EnactionDescription.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.InvolvedArtifact.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theTasks");
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theReservations");
      createCache(
          cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theInvolvedArtifactToNormals");
      createCache(
          cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theInvolvedArtifactsFromNormals");
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theRequiredPeople");
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theRequiredResources");
      createCache(cm, br.ufpa.labes.spm.domain.Parameter.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactParam.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.PrimitiveParam.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ReqAgentRequiresAbility.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.RequiredPeople.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.RequiredPeople.class.getName()
              + ".theReqAgentRequiresAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.ReqAgent.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ReqWorkGroup.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.RequiredResource.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ActivityInstantiated.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.ActivityInstantiated.class.getName() + ".theInstSugs");
      createCache(cm, br.ufpa.labes.spm.domain.AgentInstSuggestionToAgent.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.AgentWorkingLoad.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.InstantiationSuggestion.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.InstantiationSuggestion.class.getName() + ".sugRsrcs");
      createCache(cm, br.ufpa.labes.spm.domain.PeopleInstSug.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.PeopleInstSug.class.getName() + ".theAgentInstSugToAgents");
      createCache(cm, br.ufpa.labes.spm.domain.AgentInstSug.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupInstSug.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupInstSug.class.getName() + ".sugWorkGroups");
      createCache(cm, br.ufpa.labes.spm.domain.ResourcePossibleUse.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Estimation.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ActivityEstimation.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.AgentEstimation.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactEstimation.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupEstimation.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.OrganizationEstimation.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ProcessEstimation.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ResourceEstimation.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Metric.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ActivityMetric.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.AgentMetric.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactMetric.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupMetric.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.OrganizationMetric.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ProcessMetric.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ResourceMetric.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.MetricDefinitionUnit.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.MetricDefinition.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.MetricDefinition.class.getName() + ".units");
      createCache(
          cm, br.ufpa.labes.spm.domain.MetricDefinition.class.getName() + ".theEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.MetricDefinition.class.getName() + ".theMetrics");
      createCache(cm, br.ufpa.labes.spm.domain.GraphicCoordinate.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.WebAPSEEObject.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Description.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.Description.class.getName() + ".descTemplateOriginalVersions");
      createCache(cm, br.ufpa.labes.spm.domain.Process.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Process.class.getName() + ".theProcessAgendas");
      createCache(cm, br.ufpa.labes.spm.domain.Process.class.getName() + ".theProcessEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Process.class.getName() + ".theProjects");
      createCache(cm, br.ufpa.labes.spm.domain.Process.class.getName() + ".theProcessMetrics");
      createCache(cm, br.ufpa.labes.spm.domain.Process.class.getName() + ".theProcessEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Process.class.getName() + ".theAgents");
      createCache(cm, br.ufpa.labes.spm.domain.ProcessModel.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ProcessModel.class.getName() + ".theActivities");
      createCache(cm, br.ufpa.labes.spm.domain.ProcessModel.class.getName() + ".theConnections");
      createCache(
          cm, br.ufpa.labes.spm.domain.ProcessModel.class.getName() + ".theProcessModelEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Template.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Template.class.getName() + ".theProcessModels");
      createCache(
          cm, br.ufpa.labes.spm.domain.Template.class.getName() + ".theDerivedVersionDescriptions");
      createCache(
          cm, br.ufpa.labes.spm.domain.Template.class.getName() + ".theTemplateNewDescriptions");
      createCache(cm, br.ufpa.labes.spm.domain.Consumable.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Exclusive.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Exclusive.class.getName() + ".theReservations");
      createCache(cm, br.ufpa.labes.spm.domain.Reservation.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Shareable.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResourceEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theRequiredResources");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".instSugToResources");
      createCache(
          cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResourcePossibleUses");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResourceMetrics");
      createCache(
          cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResourceEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResources");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".instSuggestions");
      createCache(cm, br.ufpa.labes.spm.domain.Credential.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.LogEntry.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Ocurrence.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ProcessAgenda.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ProcessAgenda.class.getName() + ".theTasks");
      createCache(cm, br.ufpa.labes.spm.domain.Task.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Task.class.getName() + ".theArtifactTasks");
      createCache(cm, br.ufpa.labes.spm.domain.Task.class.getName() + ".theAgendaEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Task.class.getName() + ".theOcurrences");
      createCache(cm, br.ufpa.labes.spm.domain.TaskAgenda.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.TaskAgenda.class.getName() + ".theProcessAgenda");
      createCache(cm, br.ufpa.labes.spm.domain.ClassMethodCall.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.PrimitiveType.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.PrimitiveType.class.getName() + ".theToolParameters");
      createCache(cm, br.ufpa.labes.spm.domain.Script.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Subroutine.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Subroutine.class.getName() + ".theToolParameters");
      createCache(cm, br.ufpa.labes.spm.domain.ToolDefinition.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.ToolDefinition.class.getName() + ".theArtifactTypes");
      createCache(cm, br.ufpa.labes.spm.domain.ToolParameter.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Type.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Type.class.getName() + ".sugToReqWorkGroups");
      createCache(cm, br.ufpa.labes.spm.domain.Type.class.getName() + ".instSugToTypes");
      createCache(cm, br.ufpa.labes.spm.domain.Type.class.getName() + ".subTypes");
      createCache(
          cm, br.ufpa.labes.spm.domain.Type.class.getName() + ".theToolDefinitionToArtifactTypes");
      createCache(cm, br.ufpa.labes.spm.domain.AbilityType.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.AbilityType.class.getName() + ".theAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.ActivityType.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ActivityType.class.getName() + ".theActivities");
      createCache(cm, br.ufpa.labes.spm.domain.ActivityType.class.getName() + ".theProcesses");
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactType.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactType.class.getName() + ".theArtifacts");
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactType.class.getName() + ".theArtifactCons");
      createCache(
          cm, br.ufpa.labes.spm.domain.ArtifactType.class.getName() + ".theInvolvedArtifacts");
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactType.class.getName() + ".theSubroutines");
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactType.class.getName() + ".theToolParameters");
      createCache(cm, br.ufpa.labes.spm.domain.ConnectionType.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ConnectionType.class.getName() + ".theConnections");
      createCache(cm, br.ufpa.labes.spm.domain.EventType.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.EventType.class.getName() + ".theEvents");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupType.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupType.class.getName() + ".theWorkGroups");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupType.class.getName() + ".theReqWorkGroups");
      createCache(cm, br.ufpa.labes.spm.domain.MetricType.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.MetricType.class.getName() + ".theMetricDefinitions");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceType.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.ResourceType.class.getName() + ".theRequiredResources");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceType.class.getName() + ".theResources");
      createCache(cm, br.ufpa.labes.spm.domain.RoleType.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.RoleType.class.getName() + ".theRoles");
      createCache(cm, br.ufpa.labes.spm.domain.ToolType.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.ToolType.class.getName() + ".theToolDefinitions");
      createCache(cm, br.ufpa.labes.spm.domain.Plugin.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Plugin.class.getName() + ".users");
      createCache(cm, br.ufpa.labes.spm.domain.Automatic.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.Automatic.class.getName() + ".theParameters");
      createCache(
          cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theBranchConCondToActivities");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".theBranchConCondToMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.BranchConCond.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.BranchConCond.class.getName() + ".theBranchConCondToActivities");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.BranchConCond.class.getName()
              + ".theBranchConCondToMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.BranchConCondToActivity.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.BranchConCondToMultipleCon.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theBranchConCondToActivities");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".theBranchConCondToMultipleCons");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.BranchConCond.class.getName() + ".theBranchConCondToActivities");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.BranchConCond.class.getName()
              + ".theBranchConCondToMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.BranchConCondToActivity.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.BranchConCondToMultipleCon.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.VCSRepository.class.getName() + ".theArtifacts");
      createCache(
          cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theModelingActivityEvents");
      createCache(
          cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theActivityEstimations");
      createCache(
          cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theBranchConCondToActivities");
      createCache(
          cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theActivityInstantiateds");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".theBranchConCondToMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".theBranchANDS");
      createCache(
          cm, br.ufpa.labes.spm.domain.CatalogEvent.class.getName() + ".theCatalogEventToCatalogs");
      createCache(
          cm, br.ufpa.labes.spm.domain.AgendaEvent.class.getName() + ".theCatalogEventToAgenda");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ConnectionEvent.class.getName()
              + ".theCatalogEventToConnections");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.GlobalActivityEvent.class.getName()
              + ".theCatalogEventToGlobalActivities");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ModelingActivityEvent.class.getName()
              + ".theCatalogEventToModelingActivities");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ProcessEvent.class.getName() + ".theCatalogEventToProcesses");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ProcessModelEvent.class.getName()
              + ".theCatalogEventToProcessModels");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.ResourceEvent.class.getName() + ".theCatalogEventToResources");
      createCache(
          cm, br.ufpa.labes.spm.domain.Organization.class.getName() + ".theOrganizationMetrics");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.Organization.class.getName() + ".theOrganizationEstimations");
      createCache(
          cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theInvolvedArtifactToNormals");
      createCache(
          cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theInvolvedArtifactsFromNormals");
      createCache(
          cm,
          br.ufpa.labes.spm.domain.RequiredPeople.class.getName()
              + ".theReqAgentRequiresAbilities");
      createCache(
          cm, br.ufpa.labes.spm.domain.ActivityInstantiated.class.getName() + ".theInstSugs");
      createCache(
          cm, br.ufpa.labes.spm.domain.InstantiationSuggestion.class.getName() + ".sugRsrcs");
      createCache(cm, br.ufpa.labes.spm.domain.InstantiationPolicyLog.class.getName());
      createCache(
          cm,
          br.ufpa.labes.spm.domain.InstantiationPolicyLog.class.getName()
              + ".theActivityInstantiateds");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceInstSug.class.getName());
      createCache(
          cm, br.ufpa.labes.spm.domain.PeopleInstSug.class.getName() + ".theAgentInstSugToAgents");
      createCache(
          cm, br.ufpa.labes.spm.domain.ProcessModel.class.getName() + ".theProcessModelEvents");
      createCache(
          cm, br.ufpa.labes.spm.domain.Template.class.getName() + ".theDerivedVersionDescriptions");
      createCache(
          cm, br.ufpa.labes.spm.domain.Template.class.getName() + ".theTemplateNewDescriptions");
      createCache(
          cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResourcePossibleUses");
      createCache(
          cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResourceEstimations");
      createCache(
          cm, br.ufpa.labes.spm.domain.Type.class.getName() + ".theToolDefinitionToArtifactTypes");
      createCache(
          cm, br.ufpa.labes.spm.domain.ArtifactType.class.getName() + ".theInvolvedArtifacts");
      createCache(
          cm, br.ufpa.labes.spm.domain.MetricType.class.getName() + ".theMetricDefinitions");
      createCache(
          cm, br.ufpa.labes.spm.domain.ResourceType.class.getName() + ".theRequiredResources");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theModelingActivityEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theActivityEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theBranchConCondToActivities");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".theActivityInstantiateds");
      createCache(cm, br.ufpa.labes.spm.domain.Ability.class.getName() + ".theReqAgentRequiresAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.Agent.class.getName() + ".theModelingActivityEvents");
      createCache(cm, br.ufpa.labes.spm.domain.EmailConfiguration.class.getName() + ".theProcesses");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".theWorkGroupEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".sugToChosenWorkGroups");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".theArtifactEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.RelationshipKind.class.getName() + ".theAssetRelationships");
      createCache(cm, br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".theBranchConCondToMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.BranchConCond.class.getName() + ".theBranchConCondToActivities");
      createCache(cm, br.ufpa.labes.spm.domain.BranchConCond.class.getName() + ".theBranchConCondToMultipleCons");
      createCache(cm, br.ufpa.labes.spm.domain.CatalogEvent.class.getName() + ".theCatalogEventToCatalogs");
      createCache(cm, br.ufpa.labes.spm.domain.AgendaEvent.class.getName() + ".theCatalogEventToAgenda");
      createCache(cm, br.ufpa.labes.spm.domain.ConnectionEvent.class.getName() + ".theCatalogEventToConnections");
      createCache(cm, br.ufpa.labes.spm.domain.GlobalActivityEvent.class.getName() + ".theCatalogEventToGlobalActivities");
      createCache(cm, br.ufpa.labes.spm.domain.ModelingActivityEvent.class.getName() + ".theCatalogEventToModelingActivities");
      createCache(cm, br.ufpa.labes.spm.domain.ProcessEvent.class.getName() + ".theCatalogEventToProcesses");
      createCache(cm, br.ufpa.labes.spm.domain.ProcessModelEvent.class.getName() + ".theCatalogEventToProcessModels");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceEvent.class.getName() + ".theCatalogEventToResources");
      createCache(cm, br.ufpa.labes.spm.domain.Organization.class.getName() + ".theOrganizationMetrics");
      createCache(cm, br.ufpa.labes.spm.domain.Organization.class.getName() + ".theOrganizationEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theInvolvedArtifactToNormals");
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theInvolvedArtifactsFromNormals");
      createCache(cm, br.ufpa.labes.spm.domain.RequiredPeople.class.getName() + ".theReqAgentRequiresAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.ActivityInstantiated.class.getName() + ".theInstSugs");
      createCache(cm, br.ufpa.labes.spm.domain.InstantiationSuggestion.class.getName() + ".sugRsrcs");
      createCache(cm, br.ufpa.labes.spm.domain.InstantiationPolicyLog.class.getName() + ".theActivityInstantiateds");
      createCache(cm, br.ufpa.labes.spm.domain.PeopleInstSug.class.getName() + ".theAgentInstSugToAgents");
      createCache(cm, br.ufpa.labes.spm.domain.MetricDefinition.class.getName() + ".theEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Description.class.getName() + ".descTemplateOriginalVersions");
      createCache(cm, br.ufpa.labes.spm.domain.ProcessModel.class.getName() + ".theProcessModelEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Template.class.getName() + ".theDerivedVersionDescriptions");
      createCache(cm, br.ufpa.labes.spm.domain.Template.class.getName() + ".theTemplateNewDescriptions");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResourcePossibleUses");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResourceEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.PrimitiveType.class.getName() + ".theToolParameters");
      createCache(cm, br.ufpa.labes.spm.domain.ToolDefinition.class.getName() + ".theArtifactTypes");
      createCache(cm, br.ufpa.labes.spm.domain.Type.class.getName() + ".theToolDefinitionToArtifactTypes");
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactType.class.getName() + ".theInvolvedArtifacts");
      createCache(cm, br.ufpa.labes.spm.domain.MetricType.class.getName() + ".theMetricDefinitions");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceType.class.getName() + ".theRequiredResources");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".activityEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Activity.class.getName() + ".fromBranchANDCons");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".theReqGroups");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroup.class.getName() + ".subGroups");
      createCache(cm, br.ufpa.labes.spm.domain.Role.class.getName() + ".commands");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".derivedFroms");
      createCache(cm, br.ufpa.labes.spm.domain.Artifact.class.getName() + ".belongsTos");
      createCache(cm, br.ufpa.labes.spm.domain.NotWorkingDay.class.getName());
      createCache(cm, br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".fromArtifactCons");
      createCache(cm, br.ufpa.labes.spm.domain.MultipleCon.class.getName() + ".theBranchANDCons");
      createCache(cm, br.ufpa.labes.spm.domain.BranchANDCon.class.getName() + ".toActivities");
      createCache(cm, br.ufpa.labes.spm.domain.CatalogEvent.class.getName() + ".theCatalogEvents");
      createCache(cm, br.ufpa.labes.spm.domain.AgendaEvent.class.getName() + ".theCatalogEvents");
      createCache(cm, br.ufpa.labes.spm.domain.ConnectionEvent.class.getName() + ".theCatalogEvents");
      createCache(cm, br.ufpa.labes.spm.domain.GlobalActivityEvent.class.getName() + ".theCatalogEvents");
      createCache(cm, br.ufpa.labes.spm.domain.ModelingActivityEvent.class.getName() + ".theCatalogEvents");
      createCache(cm, br.ufpa.labes.spm.domain.ProcessEvent.class.getName() + ".theCatalogEvents");
      createCache(cm, br.ufpa.labes.spm.domain.ProcessModelEvent.class.getName() + ".theCatalogEvents");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceEvent.class.getName() + ".theRequestorAgents");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceEvent.class.getName() + ".theCatalogEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Company.class.getName() + ".theOrganizationalUnits");
      createCache(cm, br.ufpa.labes.spm.domain.Company.class.getName() + ".theSystems");
      createCache(cm, br.ufpa.labes.spm.domain.CompanyUnit.class.getName() + ".theSubordinates");
      createCache(cm, br.ufpa.labes.spm.domain.CompanyUnit.class.getName() + ".theUnitAgents");
      createCache(cm, br.ufpa.labes.spm.domain.Project.class.getName() + ".theCalendars");
      createCache(cm, br.ufpa.labes.spm.domain.Structure.class.getName() + ".theRepositories");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".stats");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".assets");
      createCache(cm, br.ufpa.labes.spm.domain.Author.class.getName() + ".followers");
      createCache(cm, br.ufpa.labes.spm.domain.Organization.class.getName() + ".organizationMetrics");
      createCache(cm, br.ufpa.labes.spm.domain.Organization.class.getName() + ".organizationEstimations");
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".theAgendaEvents");
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".involvedArtifactToNormals");
      createCache(cm, br.ufpa.labes.spm.domain.Normal.class.getName() + ".involvedArtifactFromNormals");
      createCache(cm, br.ufpa.labes.spm.domain.ReqAgent.class.getName() + ".theReqAgentRequiresAbilities");
      createCache(cm, br.ufpa.labes.spm.domain.ActivityInstantiated.class.getName() + ".theInstantiationSuggestions");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceInstSug.class.getName() + ".resourceSuggesteds");
      createCache(cm, br.ufpa.labes.spm.domain.AgentInstSug.class.getName() + ".agentSuggesteds");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupInstSug.class.getName() + ".groupSuggesteds");
      createCache(cm, br.ufpa.labes.spm.domain.MetricDefinition.class.getName() + ".estimations");
      createCache(cm, br.ufpa.labes.spm.domain.MetricDefinition.class.getName() + ".metrics");
      createCache(cm, br.ufpa.labes.spm.domain.Process.class.getName() + ".theProcessAgenda");
      createCache(cm, br.ufpa.labes.spm.domain.Template.class.getName() + ".theInstances");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".theResourceInstSugs");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".possesses");
      createCache(cm, br.ufpa.labes.spm.domain.Resource.class.getName() + ".isRequireds");
      createCache(cm, br.ufpa.labes.spm.domain.Task.class.getName() + ".ocurrences");
      createCache(cm, br.ufpa.labes.spm.domain.ArtifactType.class.getName() + ".theToolDefinitions");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupType.class.getName() + ".theReqGroups");
      createCache(cm, br.ufpa.labes.spm.domain.WorkGroupType.class.getName() + ".theWorkGroupInstSugs");
      createCache(cm, br.ufpa.labes.spm.domain.ResourceType.class.getName() + ".theResourceInstSugs");
      // jhipster-needle-ehcache-add-entry
    };
  }

  private void createCache(javax.cache.CacheManager cm, String cacheName) {
    javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
    if (cache != null) {
      cm.destroyCache(cacheName);
    }
    cm.createCache(cacheName, jcacheConfiguration);
  }
}
