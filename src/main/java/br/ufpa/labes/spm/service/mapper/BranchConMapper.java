package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.BranchConDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link BranchCon} and its DTO {@link BranchConDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {BranchANDConMapper.class, MultipleConMapper.class, ActivityMapper.class})
public interface BranchConMapper extends EntityMapper<BranchConDTO, BranchCon> {

  @Mapping(source = "theBranchANDConSub.id", target = "theBranchANDConSubId")
  @Mapping(source = "fromMultipleCon.id", target = "fromMultipleConId")
  @Mapping(source = "fromActivity.id", target = "fromActivityId")
  BranchConDTO toDto(BranchCon branchCon);

  @Mapping(source = "theBranchANDConSubId", target = "theBranchANDConSub")
  @Mapping(source = "fromMultipleConId", target = "fromMultipleCon")
  @Mapping(target = "theMultipleConSuper", ignore = true)
  @Mapping(source = "fromActivityId", target = "fromActivity")
  BranchCon toEntity(BranchConDTO branchConDTO);

  default BranchCon fromId(Long id) {
    if (id == null) {
      return null;
    }
    BranchCon branchCon = new BranchCon();
    branchCon.setId(id);
    return branchCon;
  }
}
