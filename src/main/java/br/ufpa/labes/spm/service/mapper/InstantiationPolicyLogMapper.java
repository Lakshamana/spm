package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.InstantiationPolicyLogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link InstantiationPolicyLog} and its DTO {@link InstantiationPolicyLogDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InstantiationPolicyLogMapper extends EntityMapper<InstantiationPolicyLogDTO, InstantiationPolicyLog> {


    @Mapping(target = "theActivityInstantiateds", ignore = true)
    @Mapping(target = "removeTheActivityInstantiated", ignore = true)
    InstantiationPolicyLog toEntity(InstantiationPolicyLogDTO instantiationPolicyLogDTO);

    default InstantiationPolicyLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        InstantiationPolicyLog instantiationPolicyLog = new InstantiationPolicyLog();
        instantiationPolicyLog.setId(id);
        return instantiationPolicyLog;
    }
}
