package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.VCSRepositoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link VCSRepository} and its DTO {@link VCSRepositoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {StructureMapper.class})
public interface VCSRepositoryMapper extends EntityMapper<VCSRepositoryDTO, VCSRepository> {

    @Mapping(source = "theStructure.id", target = "theStructureId")
    VCSRepositoryDTO toDto(VCSRepository vCSRepository);

    @Mapping(source = "theStructureId", target = "theStructure")
    @Mapping(target = "theArtifacts", ignore = true)
    @Mapping(target = "removeTheArtifact", ignore = true)
    VCSRepository toEntity(VCSRepositoryDTO vCSRepositoryDTO);

    default VCSRepository fromId(Long id) {
        if (id == null) {
            return null;
        }
        VCSRepository vCSRepository = new VCSRepository();
        vCSRepository.setId(id);
        return vCSRepository;
    }
}
