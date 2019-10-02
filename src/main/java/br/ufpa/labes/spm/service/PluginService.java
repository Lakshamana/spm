package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Plugin;
import br.ufpa.labes.spm.repository.PluginRepository;
import br.ufpa.labes.spm.service.dto.PluginDTO;
import br.ufpa.labes.spm.service.mapper.PluginMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link Plugin}. */
@Service
@Transactional
public class PluginService {

  private final Logger log = LoggerFactory.getLogger(PluginService.class);

  private final PluginRepository pluginRepository;

  private final PluginMapper pluginMapper;

  public PluginService(PluginRepository pluginRepository, PluginMapper pluginMapper) {
    this.pluginRepository = pluginRepository;
    this.pluginMapper = pluginMapper;
  }

  /**
   * Save a plugin.
   *
   * @param pluginDTO the entity to save.
   * @return the persisted entity.
   */
  public PluginDTO save(PluginDTO pluginDTO) {
    log.debug("Request to save Plugin : {}", pluginDTO);
    Plugin plugin = pluginMapper.toEntity(pluginDTO);
    plugin = pluginRepository.save(plugin);
    return pluginMapper.toDto(plugin);
  }

  /**
   * Get all the plugins.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<PluginDTO> findAll() {
    log.debug("Request to get all Plugins");
    return pluginRepository.findAll().stream()
        .map(pluginMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the plugins where TheDriver is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<PluginDTO> findAllWhereTheDriverIsNull() {
    log.debug("Request to get all plugins where TheDriver is null");
    return StreamSupport.stream(pluginRepository.findAll().spliterator(), false)
        .filter(plugin -> plugin.getTheDriver() == null)
        .map(pluginMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one plugin by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<PluginDTO> findOne(Long id) {
    log.debug("Request to get Plugin : {}", id);
    return pluginRepository.findById(id).map(pluginMapper::toDto);
  }

  /**
   * Delete the plugin by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Plugin : {}", id);
    pluginRepository.deleteById(id);
  }
}
