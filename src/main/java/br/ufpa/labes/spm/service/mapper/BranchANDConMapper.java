package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.BranchANDConDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchANDCon} and its DTO {@link BranchANDConDTO}.
 */
@Mapper(componentModel = "spring", uses = {MultipleConMapper.class})
public interface BranchANDConMapper extends EntityMapper<BranchANDConDTO, BranchANDCon> {


    @Mapping(target = "removeToMultipleCon", ignore = true)
    @Mapping(target = "theBranchConSuper", ignore = true)
    @Mapping(target = "fromActivities", ignore = true)
    @Mapping(target = "removeFromActivity", ignore = true)
    BranchANDCon toEntity(BranchANDConDTO branchANDConDTO);

    default BranchANDCon fromId(Long id) {
        if (id == null) {
            return null;
        }
        BranchANDCon branchANDCon = new BranchANDCon();
        branchANDCon.setId(id);
        return branchANDCon;
    }
}
