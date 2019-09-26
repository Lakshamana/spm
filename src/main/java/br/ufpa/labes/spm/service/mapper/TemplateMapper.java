package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.TemplateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Template} and its DTO {@link TemplateDTO}.
 */
@Mapper(componentModel = "spring", uses = {DescriptionMapper.class})
public interface TemplateMapper extends EntityMapper<TemplateDTO, Template> {

    @Mapping(source = "theOriginalVersionDescription.id", target = "theOriginalVersionDescriptionId")
    TemplateDTO toDto(Template template);

    @Mapping(target = "theInstances", ignore = true)
    @Mapping(target = "removeTheInstances", ignore = true)
    @Mapping(source = "theOriginalVersionDescriptionId", target = "theOriginalVersionDescription")
    @Mapping(target = "theDerivedVersionDescriptions", ignore = true)
    @Mapping(target = "removeTheDerivedVersionDescriptions", ignore = true)
    @Mapping(target = "theTemplateNewDescriptions", ignore = true)
    @Mapping(target = "removeTheTemplateNewDescriptions", ignore = true)
    Template toEntity(TemplateDTO templateDTO);

    default Template fromId(Long id) {
        if (id == null) {
            return null;
        }
        Template template = new Template();
        template.setId(id);
        return template;
    }
}
