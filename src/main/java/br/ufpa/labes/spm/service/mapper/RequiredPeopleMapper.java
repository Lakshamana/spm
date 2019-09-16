package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.RequiredPeopleDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link RequiredPeople} and its DTO {@link RequiredPeopleDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {ReqWorkGroupMapper.class, ReqAgentMapper.class, NormalMapper.class})
public interface RequiredPeopleMapper extends EntityMapper<RequiredPeopleDTO, RequiredPeople> {

  @Mapping(source = "theReqWorkGroupSub.id", target = "theReqWorkGroupSubId")
  @Mapping(source = "theReqAgentSub.id", target = "theReqAgentSubId")
  @Mapping(source = "theNormal.id", target = "theNormalId")
  RequiredPeopleDTO toDto(RequiredPeople requiredPeople);

  @Mapping(source = "theReqWorkGroupSubId", target = "theReqWorkGroupSub")
  @Mapping(source = "theReqAgentSubId", target = "theReqAgentSub")
  @Mapping(source = "theNormalId", target = "theNormal")
  @Mapping(target = "theReqAgentRequiresAbilities", ignore = true)
  @Mapping(target = "removeTheReqAgentRequiresAbility", ignore = true)
  RequiredPeople toEntity(RequiredPeopleDTO requiredPeopleDTO);

  default RequiredPeople fromId(Long id) {
    if (id == null) {
      return null;
    }
    RequiredPeople requiredPeople = new RequiredPeople();
    requiredPeople.setId(id);
    return requiredPeople;
  }
}
