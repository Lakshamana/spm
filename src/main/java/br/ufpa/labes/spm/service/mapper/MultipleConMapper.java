package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MultipleConDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MultipleCon} and its DTO {@link MultipleConDTO}.
 */
@Mapper(componentModel = "spring", uses = {JoinConMapper.class, BranchConMapper.class, DependencyMapper.class})
public interface MultipleConMapper extends EntityMapper<MultipleConDTO, MultipleCon> {

    @Mapping(source = "theJoinConSub.id", target = "theJoinConSubId")
    @Mapping(source = "theBranchConSub.id", target = "theBranchConSubId")
    @Mapping(source = "theDependency.id", target = "theDependencyId")
    MultipleConDTO toDto(MultipleCon multipleCon);

    @Mapping(source = "theJoinConSubId", target = "theJoinConSub")
    @Mapping(source = "theBranchConSubId", target = "theBranchConSub")
    @Mapping(source = "theDependencyId", target = "theDependency")
    @Mapping(target = "theConnectionSuper", ignore = true)
    @Mapping(target = "toBranchCons", ignore = true)
    @Mapping(target = "removeToBranchCon", ignore = true)
    @Mapping(target = "theArtifactCons", ignore = true)
    @Mapping(target = "removeTheArtifactCon", ignore = true)
    @Mapping(target = "fromMultipleCons", ignore = true)
    @Mapping(target = "removeFromMultipleCon", ignore = true)
    @Mapping(target = "theJoinCons", ignore = true)
    @Mapping(target = "removeTheJoinCon", ignore = true)
    MultipleCon toEntity(MultipleConDTO multipleConDTO);

    default MultipleCon fromId(Long id) {
        if (id == null) {
            return null;
        }
        MultipleCon multipleCon = new MultipleCon();
        multipleCon.setId(id);
        return multipleCon;
    }
}
