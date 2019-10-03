package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Credential;
import br.ufpa.labes.spm.repository.CredentialRepository;
import br.ufpa.labes.spm.service.dto.CredentialDTO;
import br.ufpa.labes.spm.service.mapper.CredentialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Credential}.
 */
@Service
@Transactional
public class CredentialService {

    private final Logger log = LoggerFactory.getLogger(CredentialService.class);

    private final CredentialRepository credentialRepository;

    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialRepository credentialRepository, CredentialMapper credentialMapper) {
        this.credentialRepository = credentialRepository;
        this.credentialMapper = credentialMapper;
    }

    /**
     * Save a credential.
     *
     * @param credentialDTO the entity to save.
     * @return the persisted entity.
     */
    public CredentialDTO save(CredentialDTO credentialDTO) {
        log.debug("Request to save Credential : {}", credentialDTO);
        Credential credential = credentialMapper.toEntity(credentialDTO);
        credential = credentialRepository.save(credential);
        return credentialMapper.toDto(credential);
    }

    /**
     * Get all the credentials.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CredentialDTO> findAll() {
        log.debug("Request to get all Credentials");
        return credentialRepository.findAll().stream()
            .map(credentialMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one credential by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CredentialDTO> findOne(Long id) {
        log.debug("Request to get Credential : {}", id);
        return credentialRepository.findById(id)
            .map(credentialMapper::toDto);
    }

    /**
     * Delete the credential by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Credential : {}", id);
        credentialRepository.deleteById(id);
    }
}
