package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.CredentialDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Credential} and its DTO {@link CredentialDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CredentialMapper extends EntityMapper<CredentialDTO, Credential> {

    @Mapping(source = "user.id", target = "userId")
    CredentialDTO toDto(Credential credential);

    @Mapping(source = "userId", target = "user")
    Credential toEntity(CredentialDTO credentialDTO);

    default Credential fromId(Long id) {
        if (id == null) {
            return null;
        }
        Credential credential = new Credential();
        credential.setId(id);
        return credential;
    }
}
