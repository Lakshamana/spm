package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ResourceMetricDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ResourceMetric} and its DTO {@link ResourceMetricDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {ResourceMapper.class})
public interface ResourceMetricMapper extends EntityMapper<ResourceMetricDTO, ResourceMetric> {

  @Mapping(source = "theResource.id", target = "theResourceId")
  ResourceMetricDTO toDto(ResourceMetric resourceMetric);

  @Mapping(source = "theResourceId", target = "theResource")
  @Mapping(target = "theMetricSuper", ignore = true)
  ResourceMetric toEntity(ResourceMetricDTO resourceMetricDTO);

  default ResourceMetric fromId(Long id) {
    if (id == null) {
      return null;
    }
    ResourceMetric resourceMetric = new ResourceMetric();
    resourceMetric.setId(id);
    return resourceMetric;
  }
}
