package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.DescriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Description} and its DTO {@link DescriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {TemplateMapper.class})
public interface DescriptionMapper extends EntityMapper<DescriptionDTO, Description> {

    @Mapping(source = "theTemplateOldVersion.id", target = "theTemplateOldVersionId")
    @Mapping(source = "theTemplateNewVersion.id", target = "theTemplateNewVersionId")
    DescriptionDTO toDto(Description description);

    @Mapping(source = "theTemplateOldVersionId", target = "theTemplateOldVersion")
    @Mapping(source = "theTemplateNewVersionId", target = "theTemplateNewVersion")
    @Mapping(target = "descTemplateOriginalVersions", ignore = true)
    @Mapping(target = "removeDescTemplateOriginalVersion", ignore = true)
    Description toEntity(DescriptionDTO descriptionDTO);

    default Description fromId(Long id) {
        if (id == null) {
            return null;
        }
        Description description = new Description();
        description.setId(id);
        return description;
    }
}
