package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.BranchCondToActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BranchCondToActivity} and its DTO {@link BranchCondToActivityDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActivityMapper.class, BranchConCondMapper.class})
public interface BranchCondToActivityMapper extends EntityMapper<BranchCondToActivityDTO, BranchCondToActivity> {

    @Mapping(source = "theActivity.id", target = "theActivityId")
    @Mapping(source = "theBranchConCond.id", target = "theBranchConCondId")
    BranchCondToActivityDTO toDto(BranchCondToActivity branchCondToActivity);

    @Mapping(source = "theActivityId", target = "theActivity")
    @Mapping(source = "theBranchConCondId", target = "theBranchConCond")
    BranchCondToActivity toEntity(BranchCondToActivityDTO branchCondToActivityDTO);

    default BranchCondToActivity fromId(Long id) {
        if (id == null) {
            return null;
        }
        BranchCondToActivity branchCondToActivity = new BranchCondToActivity();
        branchCondToActivity.setId(id);
        return branchCondToActivity;
    }
}
