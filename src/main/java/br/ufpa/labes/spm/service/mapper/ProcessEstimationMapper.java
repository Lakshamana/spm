package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ProcessEstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProcessEstimation} and its DTO {@link ProcessEstimationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProcessMapper.class})
public interface ProcessEstimationMapper extends EntityMapper<ProcessEstimationDTO, ProcessEstimation> {

    @Mapping(source = "theProcess.id", target = "theProcessId")
    ProcessEstimationDTO toDto(ProcessEstimation processEstimation);

    @Mapping(source = "theProcessId", target = "theProcess")
    @Mapping(target = "theEstimationSuper", ignore = true)
    ProcessEstimation toEntity(ProcessEstimationDTO processEstimationDTO);

    default ProcessEstimation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcessEstimation processEstimation = new ProcessEstimation();
        processEstimation.setId(id);
        return processEstimation;
    }
}
