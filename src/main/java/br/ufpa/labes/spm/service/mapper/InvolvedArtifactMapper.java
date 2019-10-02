package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.InvolvedArtifactDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link InvolvedArtifact} and its DTO {@link InvolvedArtifactDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {NormalMapper.class, ArtifactTypeMapper.class, ArtifactMapper.class})
public interface InvolvedArtifactMapper
    extends EntityMapper<InvolvedArtifactDTO, InvolvedArtifact> {

  @Mapping(source = "inInvolvedArtifacts.id", target = "inInvolvedArtifactsId")
  @Mapping(source = "outInvolvedArtifacts.id", target = "outInvolvedArtifactsId")
  @Mapping(source = "theArtifactType.id", target = "theArtifactTypeId")
  @Mapping(source = "theArtifact.id", target = "theArtifactId")
  InvolvedArtifactDTO toDto(InvolvedArtifact involvedArtifact);

  @Mapping(source = "inInvolvedArtifactsId", target = "inInvolvedArtifacts")
  @Mapping(source = "outInvolvedArtifactsId", target = "outInvolvedArtifacts")
  @Mapping(source = "theArtifactTypeId", target = "theArtifactType")
  @Mapping(source = "theArtifactId", target = "theArtifact")
  InvolvedArtifact toEntity(InvolvedArtifactDTO involvedArtifactDTO);

  default InvolvedArtifact fromId(Long id) {
    if (id == null) {
      return null;
    }
    InvolvedArtifact involvedArtifact = new InvolvedArtifact();
    involvedArtifact.setId(id);
    return involvedArtifact;
  }
}
