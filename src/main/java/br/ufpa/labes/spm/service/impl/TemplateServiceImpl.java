package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.TemplateService;
import br.ufpa.labes.spm.domain.Template;
import br.ufpa.labes.spm.repository.TemplateRepository;
import br.ufpa.labes.spm.service.dto.TemplateDTO;
import br.ufpa.labes.spm.service.mapper.TemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link Template}.
 */
@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {

    private final Logger log = LoggerFactory.getLogger(TemplateServiceImpl.class);

    private final TemplateRepository templateRepository;

    private final TemplateMapper templateMapper;

    public TemplateServiceImpl(TemplateRepository templateRepository, TemplateMapper templateMapper) {
        this.templateRepository = templateRepository;
        this.templateMapper = templateMapper;
    }

    /**
     * Save a template.
     *
     * @param templateDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TemplateDTO save(TemplateDTO templateDTO) {
        log.debug("Request to save Template : {}", templateDTO);
        Template template = templateMapper.toEntity(templateDTO);
        template = templateRepository.save(template);
        return templateMapper.toDto(template);
    }

    /**
     * Get all the templates.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TemplateDTO> findAll() {
        log.debug("Request to get all Templates");
        return templateRepository.findAll().stream()
            .map(templateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the templates where TheProcessSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<TemplateDTO> findAllWhereTheProcessSuperIsNull() {
        log.debug("Request to get all templates where TheProcessSuper is null");
        return StreamSupport
            .stream(templateRepository.findAll().spliterator(), false)
            .filter(template -> template.getTheProcessSuper() == null)
            .map(templateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one template by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TemplateDTO> findOne(Long id) {
        log.debug("Request to get Template : {}", id);
        return templateRepository.findById(id)
            .map(templateMapper::toDto);
    }

    /**
     * Delete the template by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Template : {}", id);
        templateRepository.deleteById(id);
    }
}
