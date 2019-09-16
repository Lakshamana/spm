package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.RoleNeedsAbilityDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link RoleNeedsAbility} and its DTO {@link RoleNeedsAbilityDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {RoleMapper.class, AbilityMapper.class})
public interface RoleNeedsAbilityMapper
    extends EntityMapper<RoleNeedsAbilityDTO, RoleNeedsAbility> {

  @Mapping(source = "theRole.id", target = "theRoleId")
  @Mapping(source = "theAbility.id", target = "theAbilityId")
  RoleNeedsAbilityDTO toDto(RoleNeedsAbility roleNeedsAbility);

  @Mapping(source = "theRoleId", target = "theRole")
  @Mapping(source = "theAbilityId", target = "theAbility")
  RoleNeedsAbility toEntity(RoleNeedsAbilityDTO roleNeedsAbilityDTO);

  default RoleNeedsAbility fromId(Long id) {
    if (id == null) {
      return null;
    }
    RoleNeedsAbility roleNeedsAbility = new RoleNeedsAbility();
    roleNeedsAbility.setId(id);
    return roleNeedsAbility;
  }
}
