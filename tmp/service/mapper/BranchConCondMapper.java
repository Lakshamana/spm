package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.BranchConCondDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchConCond} and its DTO {@link BranchConCondDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BranchConCondMapper extends EntityMapper<BranchConCondDTO, BranchConCond> {


    @Mapping(target = "theBranchConCondToActivities", ignore = true)
    @Mapping(target = "removeTheBranchConCondToActivity", ignore = true)
    @Mapping(target = "theBranchConCondToMultipleCons", ignore = true)
    @Mapping(target = "removeTheBranchConCondToMultipleCon", ignore = true)
    BranchConCond toEntity(BranchConCondDTO branchConCondDTO);

    default BranchConCond fromId(Long id) {
        if (id == null) {
            return null;
        }
        BranchConCond branchConCond = new BranchConCond();
        branchConCond.setId(id);
        return branchConCond;
    }
}
