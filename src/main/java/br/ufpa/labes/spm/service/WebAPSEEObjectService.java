package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WebAPSEEObject;
import br.ufpa.labes.spm.repository.WebAPSEEObjectRepository;
import br.ufpa.labes.spm.service.dto.WebAPSEEObjectDTO;
import br.ufpa.labes.spm.service.mapper.WebAPSEEObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WebAPSEEObject}.
 */
@Service
@Transactional
public class WebAPSEEObjectService {

    private final Logger log = LoggerFactory.getLogger(WebAPSEEObjectService.class);

    private final WebAPSEEObjectRepository webAPSEEObjectRepository;

    private final WebAPSEEObjectMapper webAPSEEObjectMapper;

    public WebAPSEEObjectService(WebAPSEEObjectRepository webAPSEEObjectRepository, WebAPSEEObjectMapper webAPSEEObjectMapper) {
        this.webAPSEEObjectRepository = webAPSEEObjectRepository;
        this.webAPSEEObjectMapper = webAPSEEObjectMapper;
    }

    /**
     * Save a webAPSEEObject.
     *
     * @param webAPSEEObjectDTO the entity to save.
     * @return the persisted entity.
     */
    public WebAPSEEObjectDTO save(WebAPSEEObjectDTO webAPSEEObjectDTO) {
        log.debug("Request to save WebAPSEEObject : {}", webAPSEEObjectDTO);
        WebAPSEEObject webAPSEEObject = webAPSEEObjectMapper.toEntity(webAPSEEObjectDTO);
        webAPSEEObject = webAPSEEObjectRepository.save(webAPSEEObject);
        return webAPSEEObjectMapper.toDto(webAPSEEObject);
    }

    /**
     * Get all the webAPSEEObjects.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WebAPSEEObjectDTO> findAll() {
        log.debug("Request to get all WebAPSEEObjects");
        return webAPSEEObjectRepository.findAll().stream()
            .map(webAPSEEObjectMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one webAPSEEObject by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WebAPSEEObjectDTO> findOne(Long id) {
        log.debug("Request to get WebAPSEEObject : {}", id);
        return webAPSEEObjectRepository.findById(id)
            .map(webAPSEEObjectMapper::toDto);
    }

    /**
     * Delete the webAPSEEObject by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WebAPSEEObject : {}", id);
        webAPSEEObjectRepository.deleteById(id);
    }
}
