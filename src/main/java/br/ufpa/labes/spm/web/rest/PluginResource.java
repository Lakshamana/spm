package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Plugin;
import br.ufpa.labes.spm.repository.PluginRepository;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;

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
import java.util.stream.Collectors;
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

    private final PluginRepository pluginRepository;

    public PluginResource(PluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    /**
     * {@code POST  /plugins} : Create a new plugin.
     *
     * @param plugin the plugin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plugin, or with status {@code 400 (Bad Request)} if the plugin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plugins")
    public ResponseEntity<Plugin> createPlugin(@Valid @RequestBody Plugin plugin) throws URISyntaxException {
        log.debug("REST request to save Plugin : {}", plugin);
        if (plugin.getId() != null) {
            throw new BadRequestAlertException("A new plugin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Plugin result = pluginRepository.save(plugin);
        return ResponseEntity.created(new URI("/api/plugins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plugins} : Updates an existing plugin.
     *
     * @param plugin the plugin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plugin,
     * or with status {@code 400 (Bad Request)} if the plugin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plugin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plugins")
    public ResponseEntity<Plugin> updatePlugin(@Valid @RequestBody Plugin plugin) throws URISyntaxException {
        log.debug("REST request to update Plugin : {}", plugin);
        if (plugin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Plugin result = pluginRepository.save(plugin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plugin.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plugins} : get all the plugins.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plugins in body.
     */
    @GetMapping("/plugins")
    public List<Plugin> getAllPlugins(@RequestParam(required = false) String filter) {
        if ("thedriver-is-null".equals(filter)) {
            log.debug("REST request to get all Plugins where theDriver is null");
            return StreamSupport
                .stream(pluginRepository.findAll().spliterator(), false)
                .filter(plugin -> plugin.getTheDriver() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Plugins");
        return pluginRepository.findAll();
    }

    /**
     * {@code GET  /plugins/:id} : get the "id" plugin.
     *
     * @param id the id of the plugin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plugin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plugins/{id}")
    public ResponseEntity<Plugin> getPlugin(@PathVariable Long id) {
        log.debug("REST request to get Plugin : {}", id);
        Optional<Plugin> plugin = pluginRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(plugin);
    }

    /**
     * {@code DELETE  /plugins/:id} : delete the "id" plugin.
     *
     * @param id the id of the plugin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plugins/{id}")
    public ResponseEntity<Void> deletePlugin(@PathVariable Long id) {
        log.debug("REST request to delete Plugin : {}", id);
        pluginRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
