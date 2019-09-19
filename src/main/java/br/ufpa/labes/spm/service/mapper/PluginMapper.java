package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.PluginDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plugin} and its DTO {@link PluginDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PluginMapper extends EntityMapper<PluginDTO, Plugin> {


    @Mapping(target = "removeUser", ignore = true)
    @Mapping(target = "theDriver", ignore = true)
    Plugin toEntity(PluginDTO pluginDTO);

    default Plugin fromId(Long id) {
        if (id == null) {
            return null;
        }
        Plugin plugin = new Plugin();
        plugin.setId(id);
        return plugin;
    }
}
