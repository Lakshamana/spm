package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.BranchConDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link BranchCon} and its DTO {@link BranchConDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {MultipleConMapper.class, ActivityMapper.class})
public interface BranchConMapper extends EntityMapper<BranchConDTO, BranchCon> {

  @Mapping(source = "fromMultipleConnection.id", target = "fromMultipleConnectionId")
  @Mapping(source = "fromActivity.id", target = "fromActivityId")
  BranchConDTO toDto(BranchCon branchCon);

  @Mapping(source = "fromMultipleConnectionId", target = "fromMultipleConnection")
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
