package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MultipleConDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link MultipleCon} and its DTO {@link MultipleConDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {DependencyMapper.class})
public interface MultipleConMapper extends EntityMapper<MultipleConDTO, MultipleCon> {

  @Mapping(source = "theDependency.id", target = "theDependencyId")
  MultipleConDTO toDto(MultipleCon multipleCon);

  @Mapping(source = "theDependencyId", target = "theDependency")
  @Mapping(target = "toBranchCons", ignore = true)
  @Mapping(target = "removeToBranchCon", ignore = true)
  @Mapping(target = "theBranchConCondToMultipleCons", ignore = true)
  @Mapping(target = "removeTheBranchConCondToMultipleCon", ignore = true)
  @Mapping(target = "theArtifactCons", ignore = true)
  @Mapping(target = "removeTheArtifactCon", ignore = true)
  @Mapping(target = "theBranchANDS", ignore = true)
  @Mapping(target = "removeTheBranchAND", ignore = true)
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
