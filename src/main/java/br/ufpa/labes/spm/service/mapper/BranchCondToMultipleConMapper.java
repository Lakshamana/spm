package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.BranchCondToMultipleConDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchCondToMultipleCon} and its DTO {@link BranchCondToMultipleConDTO}.
 */
@Mapper(componentModel = "spring", uses = {MultipleConMapper.class, BranchConCondMapper.class})
public interface BranchCondToMultipleConMapper extends EntityMapper<BranchCondToMultipleConDTO, BranchCondToMultipleCon> {

    @Mapping(source = "theMultipleCon.id", target = "theMultipleConId")
    @Mapping(source = "theBranchConCond.id", target = "theBranchConCondId")
    BranchCondToMultipleConDTO toDto(BranchCondToMultipleCon branchCondToMultipleCon);

    @Mapping(source = "theMultipleConId", target = "theMultipleCon")
    @Mapping(source = "theBranchConCondId", target = "theBranchConCond")
    BranchCondToMultipleCon toEntity(BranchCondToMultipleConDTO branchCondToMultipleConDTO);

    default BranchCondToMultipleCon fromId(Long id) {
        if (id == null) {
            return null;
        }
        BranchCondToMultipleCon branchCondToMultipleCon = new BranchCondToMultipleCon();
        branchCondToMultipleCon.setId(id);
        return branchCondToMultipleCon;
    }
}
