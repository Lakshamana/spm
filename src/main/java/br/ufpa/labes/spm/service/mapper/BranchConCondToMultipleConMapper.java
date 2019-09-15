package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.BranchConCondToMultipleConDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchConCondToMultipleCon} and its DTO {@link BranchConCondToMultipleConDTO}.
 */
@Mapper(componentModel = "spring", uses = {MultipleConMapper.class, BranchConCondMapper.class})
public interface BranchConCondToMultipleConMapper extends EntityMapper<BranchConCondToMultipleConDTO, BranchConCondToMultipleCon> {

    @Mapping(source = "theMultipleCon.id", target = "theMultipleConId")
    @Mapping(source = "theBranchConCond.id", target = "theBranchConCondId")
    BranchConCondToMultipleConDTO toDto(BranchConCondToMultipleCon branchConCondToMultipleCon);

    @Mapping(source = "theMultipleConId", target = "theMultipleCon")
    @Mapping(source = "theBranchConCondId", target = "theBranchConCond")
    BranchConCondToMultipleCon toEntity(BranchConCondToMultipleConDTO branchConCondToMultipleConDTO);

    default BranchConCondToMultipleCon fromId(Long id) {
        if (id == null) {
            return null;
        }
        BranchConCondToMultipleCon branchConCondToMultipleCon = new BranchConCondToMultipleCon();
        branchConCondToMultipleCon.setId(id);
        return branchConCondToMultipleCon;
    }
}
