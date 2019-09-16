package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.BranchConCondToActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchConCondToActivity} and its DTO {@link
 * BranchConCondToActivityDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {ActivityMapper.class, BranchConCondMapper.class})
public interface BranchConCondToActivityMapper
    extends EntityMapper<BranchConCondToActivityDTO, BranchConCondToActivity> {

  @Mapping(source = "theActivity.id", target = "theActivityId")
  @Mapping(source = "theBranchConCond.id", target = "theBranchConCondId")
  BranchConCondToActivityDTO toDto(BranchConCondToActivity branchConCondToActivity);

  @Mapping(source = "theActivityId", target = "theActivity")
  @Mapping(source = "theBranchConCondId", target = "theBranchConCond")
  BranchConCondToActivity toEntity(BranchConCondToActivityDTO branchConCondToActivityDTO);

  default BranchConCondToActivity fromId(Long id) {
    if (id == null) {
      return null;
    }
    BranchConCondToActivity branchConCondToActivity = new BranchConCondToActivity();
    branchConCondToActivity.setId(id);
    return branchConCondToActivity;
  }
}
