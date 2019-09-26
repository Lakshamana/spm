package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.PluginService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.PluginDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Plugin}.
 */
@RestController
@RequestMapping("/api")
public class PluginResource {

    private final Logger log = LoggerFactory.getLogger(PluginResource.class);

    private static final String ENTITY_NAME = "plugin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PluginService pluginService;

    public PluginResource(PluginService pluginService) {
        this.pluginService = pluginService;
    }

    /**
     * {@code POST  /plugins} : Create a new plugin.
     *
     * @param pluginDTO the pluginDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pluginDTO, or with status {@code 400 (Bad Request)} if the plugin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plugins")
    public ResponseEntity<PluginDTO> createPlugin(@Valid @RequestBody PluginDTO pluginDTO) throws URISyntaxException {
        log.debug("REST request to save Plugin : {}", pluginDTO);
        if (pluginDTO.getId() != null) {
            throw new BadRequestAlertException("A new plugin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PluginDTO result = pluginService.save(pluginDTO);
        return ResponseEntity.created(new URI("/api/plugins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plugins} : Updates an existing plugin.
     *
     * @param pluginDTO the pluginDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pluginDTO,
     * or with status {@code 400 (Bad Request)} if the pluginDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pluginDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plugins")
    public ResponseEntity<PluginDTO> updatePlugin(@Valid @RequestBody PluginDTO pluginDTO) throws URISyntaxException {
        log.debug("REST request to update Plugin : {}", pluginDTO);
        if (pluginDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PluginDTO result = pluginService.save(pluginDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pluginDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plugins} : get all the plugins.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plugins in body.
     */
    @GetMapping("/plugins")
    public List<PluginDTO> getAllPlugins(@RequestParam(required = false) String filter) {
        if ("thedriver-is-null".equals(filter)) {
            log.debug("REST request to get all Plugins where theDriver is null");
            return pluginService.findAllWhereTheDriverIsNull();
        }
        log.debug("REST request to get all Plugins");
        return pluginService.findAll();
    }

    /**
     * {@code GET  /plugins/:id} : get the "id" plugin.
     *
     * @param id the id of the pluginDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pluginDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plugins/{id}")
    public ResponseEntity<PluginDTO> getPlugin(@PathVariable Long id) {
        log.debug("REST request to get Plugin : {}", id);
        Optional<PluginDTO> pluginDTO = pluginService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pluginDTO);
    }

    /**
     * {@code DELETE  /plugins/:id} : delete the "id" plugin.
     *
     * @param id the id of the pluginDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plugins/{id}")
    public ResponseEntity<Void> deletePlugin(@PathVariable Long id) {
        log.debug("REST request to delete Plugin : {}", id);
        pluginService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
